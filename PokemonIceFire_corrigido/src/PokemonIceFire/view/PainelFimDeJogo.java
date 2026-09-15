package PokemonIceFire.view;

import PokemonIceFire.modelo.Pokemon;
import PokemonIceFire.treinador.Pokedex;
import PokemonIceFire.treinador.Treinador;
import javax.swing.*;
import java.awt.*;

/**
 * Tela dedicada de "fim de jogo", mostrada quando toda a equipe do jogador
 * desmaia (nenhuma criatura viva sobra para continuar a jornada). Diferente
 * das outras derrotas parciais (perder a criatura ativa mas ainda ter outra
 * na equipe), esta é a derrota TOTAL: não há mais nada a fazer além de
 * reiniciar a jornada ou voltar ao menu.
 *
 * Chamada via {@link JanelaPrincipal#mostrar(String)} com o identificador
 * "FIM_DE_JOGO" — ver {@link PainelBatalha} (é lá que a condição de derrota
 * total é detectada, ao final de um turno).
 */
public class PainelFimDeJogo extends JPanel {
    private final JanelaPrincipal janela;
    private final JLabel lblResumo = new JLabel(" ");
    private final JLabel lblEstatisticas = new JLabel(" ");

    PainelFimDeJogo(JanelaPrincipal janela) {
        this.janela = janela;
        setLayout(new GridBagLayout());
        setOpaque(true);
        setBackground(Constantes.PRETO);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;

        LogoDerrota titulo = new LogoDerrota("FIM DE JOGO");
        c.gridy = 0; c.insets = new Insets(70, 0, 0, 0); add(titulo, c);

        JLabel subtitulo = new JLabel("Todos os seus pokémon desmaiaram.");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitulo.setForeground(Constantes.BRANCO);
        c.gridy = 1; c.insets = new Insets(10, 0, 26, 0); add(subtitulo, c);

        c.gridy = 2; c.insets = new Insets(0, 0, 30, 0); add(criarCartaoResumo(), c);

        JButton btnReiniciar = UIUtil.botaoPill("Reiniciar Jornada", Constantes.VERMELHO, Constantes.BRANCO, 260, 46);
        btnReiniciar.addActionListener(e -> janela.reiniciarJornada());
        c.gridy = 3; c.insets = new Insets(0, 0, 12, 0); add(btnReiniciar, c);

        JButton btnMenu = UIUtil.botaoPill("Menu Principal", Constantes.CREME, Constantes.PRETO, 260, 46);
        btnMenu.addActionListener(e -> janela.voltarAoMenu());
        c.gridy = 4; c.insets = new Insets(0, 0, 10, 0); add(btnMenu, c);
    }

    /** Cartão central com o resumo da jornada encerrada (nome, criaturas capturadas, maior nível). */
    private JPanel criarCartaoResumo() {
        JPanel cartao = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                UIUtil.pintarCartao(g2, getWidth(), getHeight(), 18, Constantes.CARTAO, Constantes.BORDA);
                g2.dispose();
            }
        };
        cartao.setOpaque(false);
        cartao.setLayout(new BoxLayout(cartao, BoxLayout.Y_AXIS));
        cartao.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        cartao.setPreferredSize(new Dimension(320, 100));
        cartao.setMaximumSize(new Dimension(320, 100));

        lblResumo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblResumo.setForeground(Constantes.PRETO);
        lblResumo.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblEstatisticas.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblEstatisticas.setForeground(Constantes.MUTED);
        lblEstatisticas.setAlignmentX(Component.CENTER_ALIGNMENT);

        cartao.add(lblResumo);
        cartao.add(Box.createVerticalStrut(8));
        cartao.add(lblEstatisticas);
        return cartao;
    }

    /**
     * Preenche o resumo com os dados da jornada que acabou de terminar.
     * Precisa ser chamado ANTES de zerar o treinador/bestiário (ou seja,
     * antes de {@link JanelaPrincipal#reiniciarJornada()}/{@code voltarAoMenu()}),
     * já que é isso que ainda guarda os dados a serem exibidos.
     */
    void atualizar(Treinador treinador, Pokedex bestiario) {
        String nome = treinador != null ? treinador.getNome() : "Treinador";
        lblResumo.setText(nome + ", sua jornada chegou ao fim.");

        int capturadas = bestiario != null ? bestiario.total() : 0;
        int maiorNivel = maiorNivelAlcancado(treinador);
        lblEstatisticas.setText(capturadas + " espécie(s) registrada(s) no Bestiário  •  maior nível alcançado: " + maiorNivel);
    }

    private int maiorNivelAlcancado(Treinador treinador) {
        if (treinador == null) return 0;
        int maior = 0;
        for (Pokemon p : treinador.getEquipe()) {
            maior = Math.max(maior, p.getNivel());
        }
        return maior;
    }

    /** Fundo escuro (tema de derrota), similar ao gradiente + marca d'agua do menu, mas em tons vermelho/preto. */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();

        GradientPaint fundo = new GradientPaint(0, 0, Constantes.PRETO, 0, h, Constantes.PRETO2);
        g2.setPaint(fundo);
        g2.fillRect(0, 0, w, h);

        // faixa vermelha discreta no rodape, ecoando a tela de menu, mas mais apagada (tom de derrota)
        int faixaY = h - 26;
        g2.setColor(new Color(214, 45, 32, 40));
        g2.fillRect(0, faixaY, w, 6);
        g2.setColor(new Color(255, 255, 255, 30));
        g2.fillRect(0, faixaY + 6, w, 3);

        g2.dispose();
    }

    /** Titulo estilo "logo de jogo", mas em tom de derrota (cinza/vermelho escuro em vez do amarelo de vitoria). */
    private static class LogoDerrota extends JComponent {
        private final String texto;
        LogoDerrota(String texto) {
            this.texto = texto;
            setFont(new Font("Serif", Font.BOLD, 40));
            Dimension d = new Dimension(500, 70);
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

            g2.setColor(new Color(0, 0, 0, 140));
            g2.drawString(texto, x + 4, y + 5);

            g2.setColor(new Color(214, 45, 32));
            int[][] offsets = {{-3, 0}, {3, 0}, {0, -3}, {0, 3}, {-2, -2}, {2, -2}, {-2, 2}, {2, 2}};
            for (int[] o : offsets) {
                g2.drawString(texto, x + o[0], y + o[1]);
            }

            GradientPaint cinza = new GradientPaint(0, y - fm.getAscent(), new Color(235, 235, 235),
                    0, y, new Color(180, 180, 180));
            g2.setPaint(cinza);
            g2.drawString(texto, x, y);

            int barraW = fm.stringWidth(texto) / 2;
            g2.setColor(new Color(214, 45, 32));
            g2.fillRoundRect(getWidth() / 2 - barraW / 2, y + 10, barraW, 4, 4, 4);

            g2.dispose();
        }
    }
}
