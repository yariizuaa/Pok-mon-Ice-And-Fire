package PokemonIceFire.view;

import PokemonIceFire.modelo.TipoElemental;
import javax.swing.*;
import java.awt.*;

public class CartaoPokemon extends JPanel {
    CartaoPokemon(TipoElemental tipo, String nome, String habilidade, Runnable aoEscolher) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(14, 14, 16, 14));
        setPreferredSize(new Dimension(230, 300));

        Color corTipo = tipo.corPrincipal();

        JPanel spritePanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // aureola suave da cor do tipo atras da criatura, em vez de fundo branco liso
                int w = getWidth(), h = getHeight();
                g2.setColor(new Color(corTipo.getRed(), corTipo.getGreen(), corTipo.getBlue(), 26));
                g2.fillOval(w / 2 - 62, h / 2 - 42, 124, 124);
                CriaturaSprite.desenhar(g2, tipo, w / 2, h / 2 + 10, 120, false);
            }
        };
        spritePanel.setPreferredSize(new Dimension(230, 150));
        spritePanel.setOpaque(false);
        add(spritePanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(4, 16, 8, 16));

        JLabel lblNome = new JLabel(nome);
        lblNome.setFont(new Font("Serif", Font.BOLD, 20));
        lblNome.setForeground(Constantes.PRETO);
        lblNome.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblTipo = new JLabel("Tipo " + tipo.nomeExibicao());
        lblTipo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTipo.setForeground(corTipo);
        lblTipo.setAlignmentX(CENTER_ALIGNMENT);

        JTextArea lblHabilidade = new JTextArea(habilidade);
        lblHabilidade.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblHabilidade.setForeground(Constantes.MUTED);
        lblHabilidade.setLineWrap(true);
        lblHabilidade.setWrapStyleWord(true);
        lblHabilidade.setOpaque(false);
        lblHabilidade.setEditable(false);
        lblHabilidade.setFocusable(false);
        lblHabilidade.setAlignmentX(CENTER_ALIGNMENT);
        lblHabilidade.setMaximumSize(new Dimension(190, 60));

        infoPanel.add(lblNome);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(lblTipo);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(lblHabilidade);
        add(infoPanel, BorderLayout.CENTER);

        JButton btn = UIUtil.botaoPill("Escolher", corTipo, Color.WHITE, 190, 40);
        JPanel wrapBtn = new JPanel(new BorderLayout());
        wrapBtn.setOpaque(false);
        wrapBtn.setBorder(BorderFactory.createEmptyBorder(6, 4, 0, 4));
        wrapBtn.add(btn, BorderLayout.CENTER);
        btn.addActionListener(e -> aoEscolher.run());
        add(wrapBtn, BorderLayout.SOUTH);
    }

    /** Cartao arredondado em tom marfim quente, com sombra suave, no lugar do antigo retangulo branco chapado. */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        UIUtil.pintarCartao(g2, getWidth(), getHeight(), 20, Constantes.CARTAO, Constantes.BORDA);
        g2.dispose();
    }
}
