package PokemonIceFire.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/** Pequenos utilitarios de desenho para dar consistencia visual entre as telas. */
final class UIUtil {
    private UIUtil() {}

    /**
     * Pinta o fundo das telas "claras" (Escolha, Batalha, Equipe, Itens) com
     * um degrade quente (pergaminho -> areia tostada), em vez de uma cor
     * solida quase-branca, alem de uma marca d'agua bem sutil de pokebolas
     * no canto, para dar um pouco mais de textura e vida sem atrapalhar a
     * leitura do conteudo por cima.
     */
    static void pintarFundoTela(Graphics2D g2, int w, int h) {
        Object oldHint = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint fundo = new GradientPaint(0, 0, Constantes.LIGHT, w * 0.6f, h, Constantes.LIGHT2);
        g2.setPaint(fundo);
        g2.fillRect(0, 0, w, h);

        // pokebola gigante e bem discreta no canto inferior direito, so como textura
        int r = (int) (Math.min(w, h) * 0.55);
        int cx = w + r / 4, cy = h + r / 3;
        g2.setColor(new Color(214, 45, 32, 14));
        g2.fillArc(cx - r, cy - r, r * 2, r * 2, 0, 180);
        g2.setColor(new Color(255, 255, 255, 40));
        g2.fillArc(cx - r, cy - r, r * 2, r * 2, 180, 180);
        g2.setColor(new Color(20, 20, 22, 18));
        g2.setStroke(new BasicStroke(3f));
        g2.drawOval(cx - r, cy - r, r * 2, r * 2);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint == null ? RenderingHints.VALUE_ANTIALIAS_DEFAULT : oldHint);
    }

    /** Desenha um "cartao" arredondado com sombra suave por baixo. */
    static void pintarCartao(Graphics2D g2, int w, int h, int arco, Color fundo, Color borda) {
        Object oldHint = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // sombra (varias camadas com opacidade decrescente)
        for (int i = 6; i >= 1; i--) {
            g2.setColor(new Color(15, 25, 35, 5));
            g2.fill(new RoundRectangle2D.Float(i, i + 2, w - i, h - i, arco, arco));
        }

        g2.setColor(fundo);
        g2.fill(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, arco, arco));
        if (borda != null) {
            g2.setColor(borda);
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 2, h - 2, arco, arco));
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint == null ? RenderingHints.VALUE_ANTIALIAS_DEFAULT : oldHint);
    }

    /** Botao em formato "pill" (cantos totalmente arredondados), com destaque no hover. */
    static JButton botaoPill(String texto, Color fundo, Color corTexto, int larguraPreferida, int alturaPreferida) {
        JButton b = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                Color cor = fundo;
                if (getModel().isPressed()) cor = fundo.darker();
                else if (getModel().isRollover()) cor = fundo.brighter();
                g2.setColor(cor);
                g2.fillRoundRect(0, 0, w, h, h, h);
                g2.setColor(new Color(0, 0, 0, 45));
                g2.setStroke(new BasicStroke(1.4f));
                g2.drawRoundRect(1, 1, w - 3, h - 3, h, h);

                g2.setColor(corTexto);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = (w - fm.stringWidth(texto)) / 2;
                int ty = h / 2 + fm.getAscent() / 2 - 2;
                g2.drawString(texto, tx, ty);
                g2.dispose();
            }
        };
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(false);
        if (larguraPreferida > 0) {
            Dimension d = new Dimension(larguraPreferida, alturaPreferida);
            b.setPreferredSize(d);
            b.setMaximumSize(d);
        }
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    /** Pequeno selo/chip colorido com texto (ex.: tipo elemental, contadores). */
    static void pintarChip(Graphics2D g2, String texto, int x, int y, Color fundo, Color corTexto, Font fonte) {
        g2.setFont(fonte);
        FontMetrics fm = g2.getFontMetrics();
        int padH = 8, padV = 3;
        int w = fm.stringWidth(texto) + padH * 2;
        int h = fm.getHeight() + padV;
        g2.setColor(fundo);
        g2.fillRoundRect(x, y, w, h, h, h);
        g2.setColor(corTexto);
        g2.drawString(texto, x + padH, y + fm.getAscent() + padV / 2 - 1);
    }
}
