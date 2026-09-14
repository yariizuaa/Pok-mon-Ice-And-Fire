package PokemonIceFire.view;

import PokemonIceFire.modelo.TipoElemental;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class CriaturaSprite {

    // Nome do arquivo de imagem esperado para cada tipo, dentro da pasta
    // "sprites" (ao lado desta classe: view/sprites/*.png).
    private static final Map<TipoElemental, String> ARQUIVOS = new EnumMap<>(TipoElemental.class);
    static {
        ARQUIVOS.put(TipoElemental.FOGO, "charmander.png");
        ARQUIVOS.put(TipoElemental.AGUA, "squirtle.png");
        ARQUIVOS.put(TipoElemental.PLANTA, "bulbassaur.png");
    }

    // Cache: carrega cada imagem uma única vez.
    private static final Map<TipoElemental, BufferedImage> CACHE = new EnumMap<>(TipoElemental.class);
    private static boolean avisoJaImpresso = false;

    static void desenhar(Graphics2D g, TipoElemental tipo, int cx, int cy, int tamanho, boolean desmaiado) {
        BufferedImage img = carregar(tipo);

        Object oldHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Object oldInterp = g.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // sombra no chao (mantida igual, funciona bem com qualquer sprite)
        int r = tamanho / 2;
        g.setColor(new Color(0, 0, 0, 40));
        g.fillOval(cx - r + 6, cy + r - 8, tamanho - 12, 18);

        if (img != null) {
            if (desmaiado) img = paraCinza(img);
            g.drawImage(img, cx - r, cy - r, tamanho, tamanho, null);
        } else {
            desenharPlaceholder(g, tipo, cx, cy, tamanho, desmaiado);
        }

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint == null ? RenderingHints.VALUE_ANTIALIAS_DEFAULT : oldHint);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldInterp == null ? RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR : oldInterp);
    }

    /** Carrega e armazena em cache a imagem .png do tipo (ou null se não existir). */
    private static BufferedImage carregar(TipoElemental tipo) {
        if (CACHE.containsKey(tipo)) return CACHE.get(tipo);

        String arquivo = ARQUIVOS.get(tipo);
        BufferedImage img = null;
        try (InputStream in = CriaturaSprite.class.getResourceAsStream("sprites/" + arquivo)) {
            if (in != null) {
                img = ImageIO.read(in);
            } else if (!avisoJaImpresso) {
                System.out.println("[CriaturaSprite] Imagem nao encontrada: view/sprites/" + arquivo
                        + " (usando desenho padrao). Veja o README para saber onde colocar os .png.");
                avisoJaImpresso = true;
            }
        } catch (IOException e) {
            System.out.println("[CriaturaSprite] Erro ao ler " + arquivo + ": " + e.getMessage());
        }
        CACHE.put(tipo, img);
        return img;
    }

    /** Converte a imagem para tons de cinza (usado quando a criatura esta desmaiada). */
    private static BufferedImage paraCinza(BufferedImage original) {
        BufferedImageOp op = new ColorConvertOp(ColorSpace_GRAY(), null);
        BufferedImage cinza = op.filter(original, null);
        return cinza;
    }

    private static java.awt.color.ColorSpace ColorSpace_GRAY() {
        return java.awt.color.ColorSpace.getInstance(java.awt.color.ColorSpace.CS_GRAY);
    }

    // =====================================================================
    // Desenho antigo (vetorial), usado apenas como reserva quando a imagem
    // .png correspondente ainda nao foi colocada na pasta view/sprites/.
    // =====================================================================
    private static void desenharPlaceholder(Graphics2D g, TipoElemental tipo, int cx, int cy, int tamanho, boolean desmaiado) {
        Color cor = desmaiado ? new Color(160, 160, 165) : tipo.corPrincipal();
        int r = tamanho / 2;

        g.setColor(cor);
        g.fillOval(cx - r, cy - r, tamanho, tamanho);
        g.setColor(cor.darker());
        g.setStroke(new BasicStroke(3f));
        g.drawOval(cx - r, cy - r, tamanho, tamanho);

        g.setColor(new Color(255, 255, 255, 70));
        g.fillOval(cx - r / 2, cy - r / 6, r, (int) (r * 0.9));

        int eyeR = Math.max(6, tamanho / 11);
        int eyeY = cy - r / 6;
        g.setColor(Color.WHITE);
        g.fillOval(cx - r / 2, eyeY - eyeR, eyeR * 2, eyeR * 2);
        g.fillOval(cx + r / 2 - eyeR * 2, eyeY - eyeR, eyeR * 2, eyeR * 2);
        int pupilR = Math.max(3, eyeR / 2);
        g.setColor(desmaiado ? new Color(90, 90, 95) : Color.BLACK);
        if (desmaiado) {
            g.setStroke(new BasicStroke(2.5f));
            drawX(g, cx - r / 2 + eyeR, eyeY, pupilR + 2);
            drawX(g, cx + r / 2 - eyeR, eyeY, pupilR + 2);
        } else {
            g.fillOval(cx - r / 2 + eyeR - pupilR, eyeY - pupilR, pupilR * 2, pupilR * 2);
            g.fillOval(cx + r / 2 - eyeR - pupilR, eyeY - pupilR, pupilR * 2, pupilR * 2);
        }

        if (!desmaiado) {
            switch (tipo) {
                case FOGO: desenharChama(g, cx, cy - r, r); break;
                case AGUA: desenharOndas(g, cx, cy, r); break;
                case PLANTA: desenharFolhas(g, cx, cy - r, r); break;
            }
        }
    }

    private static void drawX(Graphics2D g, int cx, int cy, int s) {
        g.drawLine(cx - s, cy - s, cx + s, cy + s);
        g.drawLine(cx - s, cy + s, cx + s, cy - s);
    }

    private static void desenharChama(Graphics2D g, int cx, int topoY, int r) {
        int fw = (int) (r * 0.55);
        int fh = (int) (r * 0.7);
        GeneralPath p = new GeneralPath();
        p.moveTo(cx, topoY - fh);
        p.curveTo(cx + fw, topoY - fh * 0.5, cx + fw * 0.7, topoY, cx, topoY + fh * 0.15);
        p.curveTo(cx - fw * 0.7, topoY, cx - fw, topoY - fh * 0.5, cx, topoY - fh);
        p.closePath();
        g.setColor(new Color(255, 196, 61));
        g.fill(p);
        g.setColor(new Color(230, 92, 46));
        g.setStroke(new BasicStroke(2f));
        g.draw(p);
    }

    private static void desenharOndas(Graphics2D g, int cx, int cy, int r) {
        g.setColor(new Color(255, 255, 255, 160));
        g.setStroke(new BasicStroke(2.5f));
        for (int i = -1; i <= 1; i++) {
            int y = cy + i * (r / 3);
            g.drawArc(cx - r / 3, y, r / 3, r / 6, 200, 140);
            g.drawArc(cx, y, r / 3, r / 6, 200, 140);
        }
    }

    private static void desenharFolhas(Graphics2D g, int cx, int topoY, int r) {
        g.setColor(new Color(140, 210, 110));
        int lw = (int) (r * 0.5), lh = (int) (r * 0.7);
        g.fillOval(cx - lw, topoY - lh, lw, lh);
        g.fillOval(cx, topoY - lh + 6, lw, lh);
        g.setColor(new Color(60, 120, 50));
        g.setStroke(new BasicStroke(1.8f));
        g.drawLine(cx - lw / 2, topoY - lh / 2, cx - lw / 2, topoY - 4);
        g.drawLine(cx + lw / 2, topoY - lh / 2 + 6, cx + lw / 2, topoY + 2);
    }
}
