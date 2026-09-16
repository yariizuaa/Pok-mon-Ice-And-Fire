package PokemonIceFire.view;

import PokemonIceFire.modelo.TipoElemental;
import PokemonIceFire.persistencia.Persistencia;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class PainelMenu extends JPanel {

    // Imagem de fundo da tela inicial (view/backgrounds/menu.jpg), carregada
    // uma unica vez. Se o arquivo nao existir, cai de volta no gradiente
    // solido + marca d'agua de pokebolas (ver paintComponent).
    private static final BufferedImage FUNDO = carregarFundo();

    private JButton btnContinuar;

    private static BufferedImage carregarFundo() {
        try (InputStream in = PainelMenu.class.getResourceAsStream("backgrounds/menu.jpg")) {
            if (in != null) return ImageIO.read(in);
            System.out.println("[PainelMenu] Imagem de fundo nao encontrada: view/backgrounds/menu.jpg (usando gradiente padrao).");
        } catch (IOException e) {
            System.out.println("[PainelMenu] Erro ao carregar imagem de fundo: " + e.getMessage());
        }
        return null;
    }

    PainelMenu(JanelaPrincipal janela) {
        setLayout(new GridBagLayout());
        setOpaque(true);
        setBackground(Constantes.PRETO);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.insets = new Insets(10, 0, 10, 0);

        // ---- Logo estilo Pokémon (amarelo com contorno azul e sombra) ----
        LogoPokemon titulo = new LogoPokemon("POKE ICE AND FIRE");
        c.gridy = 0; c.insets = new Insets(60, 0, 0, 0); add(titulo, c);

        JLabel subtitulo = new JLabel("Capture. Treine. Batalhe.");
        subtitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        subtitulo.setForeground(Constantes.VERMELHO);
        c.gridy = 1; c.insets = new Insets(6, 0, 10, 0); add(subtitulo, c);

        // ---- Trio de iniciais desenhado abaixo do titulo ----
        c.gridy = 2; c.insets = new Insets(10, 0, 10, 0); add(new TrioIniciais(), c);

        btnContinuar = criarBotaoPokebola("Continuar Jornada", Constantes.VERMELHO, true);
        btnContinuar.addActionListener(e -> janela.continuarJornada());
        c.gridy = 3; c.insets = new Insets(20, 0, 10, 0); add(btnContinuar, c);

        JButton btnJogar = criarBotaoPokebola("Nova Jornada", Constantes.PRETO2, false);
        btnJogar.addActionListener(e -> janela.mostrar("ESCOLHA"));
        c.gridy = 4; c.insets = new Insets(10, 0, 10, 0); add(btnJogar, c);

        JButton btnSair = criarBotaoPokebola("Sair", Constantes.PRETO2, false);
        btnSair.addActionListener(e -> System.exit(0));
        c.gridy = 5; c.insets = new Insets(10, 0, 10, 0); add(btnSair, c);
    }

    /**
     * Mostra ou esconde o botão "Continuar Jornada" conforme exista ou não
     * uma jornada salva em disco. Chamado por
     * {@link JanelaPrincipal#mostrar} toda vez que o menu é exibido, já que
     * o save pode ter sido criado ou apagado desde a última vez.
     */
    void atualizar() {
        btnContinuar.setVisible(Persistencia.existeJornadaSalva());
    }

    /** Fundo com a imagem da floresta (modo "cover") + camada escura para manter o texto legivel. */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();

        if (FUNDO != null) {
            desenharFundoCover(g2, FUNDO, w, h);
            // camada escura translucida por cima da foto, para o texto/botoes continuarem legiveis
            g2.setColor(new Color(18, 14, 14, 165));
            g2.fillRect(0, 0, w, h);
        } else {
            GradientPaint fundo = new GradientPaint(0, 0, Constantes.PRETO, 0, h, Constantes.PRETO2);
            g2.setPaint(fundo);
            g2.fillRect(0, 0, w, h);

            // marca d'agua de pokebolas espalhadas em grade (so faz sentido sobre o gradiente solido)
            g2.setColor(new Color(255, 255, 255, 14));
            int passo = 110;
            int raio = 34;
            for (int y = -raio; y < h + raio; y += passo) {
                for (int x = -raio; x < w + raio; x += passo) {
                    int off = ((y / passo) % 2 == 0) ? 0 : passo / 2;
                    desenharPokebolaSilhueta(g2, x + off, y, raio);
                }
            }
        }

        // linha de "arena" vermelha/branca no rodape, como base de uma pokebola gigante
        int faixaY = h - 26;
        g2.setColor(new Color(214, 45, 32, 60));
        g2.fillRect(0, faixaY, w, 6);
        g2.setColor(new Color(255, 255, 255, 60));
        g2.fillRect(0, faixaY + 6, w, 3);

        g2.dispose();
    }

    /** Desenha a imagem cobrindo todo o painel (equivalente a CSS background-size: cover), centralizada e sem distorcer. */
    private void desenharFundoCover(Graphics2D g2, BufferedImage img, int w, int h) {
        double escala = Math.max((double) w / img.getWidth(), (double) h / img.getHeight());
        int iw = (int) Math.ceil(img.getWidth() * escala);
        int ih = (int) Math.ceil(img.getHeight() * escala);
        int x = (w - iw) / 2;
        int y = (h - ih) / 2;
        Object oldInterp = g2.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, x, y, iw, ih, null);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldInterp == null ? RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR : oldInterp);
    }

    private void desenharPokebolaSilhueta(Graphics2D g2, int cx, int cy, int r) {
        g2.fillOval(cx - r, cy - r, r * 2, r * 2);
    }

    private JButton criarBotaoPokebola(String texto, Color cor, boolean destaque) {
        JButton b = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                Color base = getModel().isRollover() ? cor.brighter() : cor;
                g2.setColor(base);
                g2.fillRoundRect(0, 0, w, h, h, h);
                g2.setColor(new Color(0, 0, 0, 60));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, w - 3, h - 3, h, h);

                // mini pokebola a esquerda do texto
                int pr = 16;
                int px = 22, py = h / 2;
                Color corTexto = destaque ? Constantes.PRETO : Color.WHITE;
                desenharMiniPokebola(g2, px, py, pr, corTexto);

                g2.setColor(corTexto);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = px + pr + 14;
                int ty = h / 2 + fm.getAscent() / 2 - 2;
                g2.drawString(texto, tx, ty);
                g2.dispose();
            }
        };
        b.setFont(new Font("SansSerif", Font.BOLD, 16));
        b.setForeground(destaque ? Constantes.PRETO : Color.WHITE);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(240, 48));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void desenharMiniPokebola(Graphics2D g2, int cx, int cy, int r, Color corBorda) {
        g2.setColor(new Color(214, 45, 32));
        g2.fillArc(cx - r, cy - r, r * 2, r * 2, 0, 180);
        g2.setColor(Color.WHITE);
        g2.fillArc(cx - r, cy - r, r * 2, r * 2, 180, 180);
        g2.setColor(corBorda);
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval(cx - r, cy - r, r * 2, r * 2);
        g2.drawLine(cx - r, cy, cx + r, cy);
        int botao = r / 2;
        g2.setColor(Color.WHITE);
        g2.fillOval(cx - botao / 2, cy - botao / 2, botao, botao);
        g2.drawOval(cx - botao / 2, cy - botao / 2, botao, botao);
    }

    /** Titulo estilo "logo de jogo Pokemon": amarelo, contorno azul escuro e sombra. */
    private static class LogoPokemon extends JComponent {
        private final String texto;
        LogoPokemon(String texto) {
            this.texto = texto;
            setFont(new Font("Serif", Font.BOLD, 40));
            Dimension d = new Dimension(820, 70);
            setPreferredSize(d);
            setMinimumSize(d);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(texto)) / 2;
            int y = getHeight() / 2 + fm.getAscent() / 2 - 6;

            // sombra
            g2.setColor(new Color(0, 0, 0, 120));
            g2.drawString(texto, x + 4, y + 5);

            // contorno preto (varias copias ao redor para simular "stroke")
            g2.setColor(new Color(15, 15, 15));
            int[][] offsets = {{-3,0},{3,0},{0,-3},{0,3},{-2,-2},{2,-2},{-2,2},{2,2}};
            for (int[] o : offsets) {
                g2.drawString(texto, x + o[0], y + o[1]);
            }

            // preenchimento branco com leve gradiente para dar volume
            GradientPaint branco = new GradientPaint(0, y - fm.getAscent(), Color.WHITE,
                    0, y, new Color(232, 232, 230));
            g2.setPaint(branco);
            g2.drawString(texto, x, y);

            // barrinha vermelha de destaque abaixo do titulo, estilo faixa de pokebola
            int barraW = fm.stringWidth(texto) / 2;
            g2.setColor(new Color(214, 45, 32));
            g2.fillRoundRect(getWidth() / 2 - barraW / 2, y + 10, barraW, 4, 4, 4);

            g2.dispose();
        }
    }

    /** Desenha os tres iniciais (fogo, agua, planta) lado a lado, sobre suas pokebolas. */
    private static class TrioIniciais extends JComponent {
        TrioIniciais() {
            setPreferredSize(new Dimension(420, 130));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            TipoElemental[] tipos = { TipoElemental.AGUA, TipoElemental.FOGO, TipoElemental.PLANTA };
            int n = tipos.length;
            int largura = getWidth();
            int espaco = largura / n;
            int tamanho = 88;
            int cy = getHeight() - 30;

            for (int i = 0; i < n; i++) {
                int cx = espaco * i + espaco / 2;

                // plataforma circular translucida sob a criatura
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fill(new Ellipse2D.Double(cx - tamanho / 2.0, cy + tamanho / 2.0 - 12, tamanho, 20));

                CriaturaSprite.desenhar(g2, tipos[i], cx, cy, tamanho, false);
            }
            g2.dispose();
        }
    }
}
