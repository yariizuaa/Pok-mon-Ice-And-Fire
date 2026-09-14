package PokemonIceFire.view;

import PokemonIceFire.modelo.Pokemon;
import javax.swing.*;
import java.awt.*;

public class PainelEquipe extends JPanel {
    private JanelaPrincipal janela;
    private JPanel listaEquipe = new JPanel();
    private JLabel subtitulo;

    PainelEquipe(JanelaPrincipal janela) {
        this.janela = janela;
        setLayout(new BorderLayout(14, 14));
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel cabecalho = new JPanel();
        cabecalho.setOpaque(false);
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("Sua equipe");
        titulo.setFont(new Font("Serif", Font.BOLD, 26));
        titulo.setForeground(Constantes.PRETO);
        subtitulo = new JLabel("As criaturas que viajam com você");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(Constantes.MUTED);
        cabecalho.add(titulo);
        cabecalho.add(subtitulo);
        add(cabecalho, BorderLayout.NORTH);

        listaEquipe.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 16));
        listaEquipe.setOpaque(false);
        JScrollPane scroll = new JScrollPane(listaEquipe);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        add(scroll, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);
        rodape.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        rodape.add(criarChipBestiario(), BorderLayout.WEST);
        JPanel botoesRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botoesRodape.setOpaque(false);
        JButton btnItens = UIUtil.botaoPill("Ver itens", Constantes.CREME, Constantes.PRETO, 150, 42);
        btnItens.addActionListener(e -> janela.mostrar("ITENS"));
        JButton btnVoltar = UIUtil.botaoPill("Voltar para a exploração", Constantes.PRETO, Color.WHITE, 240, 42);
        btnVoltar.addActionListener(e -> janela.mostrar("BATALHA"));
        botoesRodape.add(btnItens);
        botoesRodape.add(btnVoltar);
        rodape.add(botoesRodape, BorderLayout.EAST);
        add(rodape, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        UIUtil.pintarFundoTela(g2, getWidth(), getHeight());
        g2.dispose();
    }

    private JLabel chipBestiario;

    private JComponent criarChipBestiario() {
        chipBestiario = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                UIUtil.pintarCartao(g2, getWidth(), getHeight(), getHeight(), Constantes.PRETO2, null);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), 16, getHeight() / 2 + fm.getAscent() / 2 - 2);
                g2.dispose();
            }
        };
        chipBestiario.setFont(new Font("SansSerif", Font.BOLD, 13));
        chipBestiario.setPreferredSize(new Dimension(260, 34));
        return chipBestiario;
    }

    void atualizar() {
        listaEquipe.removeAll();
        boolean podeTrocar = janela.podeTrocarPokemon();
        if (podeTrocar) {
            subtitulo.setText("Escolha qual criatura deve entrar em campo");
            subtitulo.setForeground(Constantes.VERMELHO);
        } else {
            subtitulo.setText("As criaturas que viajam com você");
            subtitulo.setForeground(Constantes.MUTED);
        }
        Pokemon ativa = janela.getTreinador().getAtiva();
        for (Pokemon c : janela.getTreinador().getEquipe()) {
            listaEquipe.add(criarMiniCartao(c, c == ativa, podeTrocar));
        }
        chipBestiario.setText("Bestiário: " + janela.getBestiario().total() + " espécie(s) registrada(s)");
        listaEquipe.revalidate();
        listaEquipe.repaint();
    }

    private JPanel criarMiniCartao(Pokemon c, boolean ativoAtual, boolean podeTrocar) {
        Color corTipo = c.getTipo().corPrincipal();
        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                UIUtil.pintarCartao(g2, getWidth(), getHeight(), 16, Constantes.CARTAO, Constantes.BORDA);
                // faixa colorida a esquerda, indicando o tipo elemental
                g2.setColor(corTipo);
                g2.fillRoundRect(0, 0, 6, getHeight(), 16, 16);
                g2.fillRect(3, 0, 4, getHeight());
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(290, 146));

        JPanel spriteP = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(corTipo.getRed(), corTipo.getGreen(), corTipo.getBlue(), 30));
                g2.fillOval(10, getHeight() / 2 - 38, 76, 76);
                CriaturaSprite.desenhar(g2, c.getTipo(), getWidth() / 2 + 4, getHeight() / 2, 74, !c.estaViva());
            }
        };
        spriteP.setOpaque(false);
        spriteP.setPreferredSize(new Dimension(100, 146));
        p.add(spriteP, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(BorderFactory.createEmptyBorder(16, 4, 12, 14));

        JLabel nome = new JLabel(c.getNome() + "  Nv." + c.getNivel());
        nome.setFont(new Font("SansSerif", Font.BOLD, 16));
        nome.setForeground(Constantes.PRETO);
        nome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tipo = new JLabel(c.getTipo().nomeExibicao() + (c.estaViva() ? "" : "  ·  Desmaiado"));
        tipo.setFont(new Font("SansSerif", Font.BOLD, 12));
        tipo.setForeground(corTipo);
        tipo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComponent miniBarra = new JComponent() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = 10;
                g2.setColor(new Color(224, 228, 233));
                g2.fillRoundRect(0, 0, w, h, 8, 8);
                double pct = Math.max(0, c.percentualVida());
                Color corBarra = pct > 0.5 ? new Color(76, 187, 105) : (pct > 0.2 ? new Color(240, 173, 61) : new Color(220, 70, 70));
                int fillW = (int) (w * pct);
                if (fillW > 0) {
                    g2.setColor(corBarra);
                    g2.fillRoundRect(0, 0, fillW, h, 8, 8);
                }
            }
        };
        miniBarra.setPreferredSize(new Dimension(150, 10));
        miniBarra.setMaximumSize(new Dimension(150, 10));
        miniBarra.setOpaque(false);
        miniBarra.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel vida = new JLabel(c.getVidaAtual() + "/" + c.getVidaMaxima() + " HP");
        vida.setFont(new Font("SansSerif", Font.PLAIN, 11));
        vida.setForeground(Constantes.MUTED);
        vida.setAlignmentX(Component.LEFT_ALIGNMENT);

        info.add(nome);
        info.add(Box.createVerticalStrut(3));
        info.add(tipo);
        info.add(Box.createVerticalStrut(10));
        info.add(miniBarra);
        info.add(Box.createVerticalStrut(3));
        info.add(vida);
        info.add(Box.createVerticalStrut(8));
        info.add(criarRodapeSelecao(c, corTipo, ativoAtual, podeTrocar));
        p.add(info, BorderLayout.CENTER);
        return p;
    }

    /**
     * Cria o botão "Selecionar" (para escolher a criatura para entrar em campo)
     * ou o selo "Em campo" quando a criatura já é a ativa.
     * O botão só aparece quando: o pokémon ativo foi abatido ou é a vez do
     * jogador atacar (batalha em andamento) — ver JanelaPrincipal.podeTrocarPokemon().
     */
    private JComponent criarRodapeSelecao(Pokemon c, Color corTipo, boolean ativoAtual, boolean podeTrocar) {
        if (ativoAtual) {
            JLabel emCampo = new JLabel("● Em campo");
            emCampo.setFont(new Font("SansSerif", Font.BOLD, 11));
            emCampo.setForeground(new Color(76, 187, 105));
            emCampo.setAlignmentX(Component.LEFT_ALIGNMENT);
            return emCampo;
        }
        if (c.estaViva() && podeTrocar) {
            JButton btnSelecionar = UIUtil.botaoPill("Selecionar", corTipo, Color.WHITE, 128, 28);
            btnSelecionar.setFont(new Font("SansSerif", Font.BOLD, 12));
            btnSelecionar.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnSelecionar.addActionListener(e -> janela.selecionarPokemon(c));
            return btnSelecionar;
        }
        JLabel indisponivel = new JLabel(c.estaViva() ? " " : "Desmaiado — não pode entrar em campo");
        indisponivel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        indisponivel.setForeground(Constantes.MUTED);
        indisponivel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return indisponivel;
    }
}

// =========================================================================
// JANELA PRINCIPAL
// =========================================================================
