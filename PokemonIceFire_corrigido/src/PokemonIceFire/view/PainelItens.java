package PokemonIceFire.view;

import PokemonIceFire.modelo.Pokemon;
import PokemonIceFire.treinador.Treinador;
import PokemonIceFire.item.TipoPocao;
import PokemonIceFire.item.TipoRevive;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Aba "Itens": mostra as poções que o treinador já encontrou (vencendo
 * batalhas) e permite usá-las para curar uma criatura da equipe.
 */
public class PainelItens extends JPanel {
    private JanelaPrincipal janela;
    private JPanel listaItens = new JPanel();

    PainelItens(JanelaPrincipal janela) {
        this.janela = janela;
        setLayout(new BorderLayout(14, 14));
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel cabecalho = new JPanel();
        cabecalho.setOpaque(false);
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("Seus itens");
        titulo.setFont(new Font("Serif", Font.BOLD, 26));
        titulo.setForeground(Constantes.PRETO);
        JLabel subtitulo = new JLabel("Poções e reviveres encontrados ao vencer batalhas — cure ou acorde sua equipe");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(Constantes.MUTED);
        cabecalho.add(titulo);
        cabecalho.add(subtitulo);
        add(cabecalho, BorderLayout.NORTH);

        listaItens.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 16));
        listaItens.setOpaque(false);
        JScrollPane scroll = new JScrollPane(listaItens);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        add(scroll, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);
        rodape.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JButton btnEquipe = UIUtil.botaoPill("Ver equipe / Bestiário", Constantes.PRETO2, Color.WHITE, 220, 42);
        btnEquipe.addActionListener(e -> janela.mostrar("EQUIPE"));
        rodape.add(btnEquipe, BorderLayout.WEST);
        JButton btnVoltar = UIUtil.botaoPill("Voltar para a exploração", Constantes.PRETO, Color.WHITE, 240, 42);
        btnVoltar.addActionListener(e -> janela.mostrar("BATALHA"));
        rodape.add(btnVoltar, BorderLayout.EAST);
        add(rodape, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        UIUtil.pintarFundoTela(g2, getWidth(), getHeight());
        g2.dispose();
    }

    void atualizar() {
        listaItens.removeAll();
        for (TipoPocao tipo : TipoPocao.values()) {
            listaItens.add(criarCartaoPocao(tipo));
        }
        for (TipoRevive tipo : TipoRevive.values()) {
            listaItens.add(criarCartaoRevive(tipo));
        }
        listaItens.revalidate();
        listaItens.repaint();
    }

    private JPanel criarCartaoPocao(TipoPocao tipo) {
        int quantidade = janela.getTreinador().quantidadeDe(tipo);
        boolean possui = quantidade > 0;
        Color corTipo = tipo.corPrincipal();

        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                UIUtil.pintarCartao(g2, getWidth(), getHeight(), 16, Constantes.CARTAO, Constantes.BORDA);
                g2.setColor(possui ? corTipo : new Color(200, 205, 210));
                g2.fillRoundRect(0, 0, 6, getHeight(), 16, 16);
                g2.fillRect(3, 0, 4, getHeight());
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(260, 150));
        p.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 16));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel nome = new JLabel(tipo.getNomeExibicao());
        nome.setFont(new Font("SansSerif", Font.BOLD, 16));
        nome.setForeground(possui ? Constantes.PRETO : Constantes.MUTED);
        nome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descricao = new JLabel(tipo.descricao());
        descricao.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descricao.setForeground(possui ? corTipo : Constantes.MUTED);
        descricao.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel qtd = new JLabel("Quantidade: " + quantidade);
        qtd.setFont(new Font("SansSerif", Font.PLAIN, 12));
        qtd.setForeground(Constantes.MUTED);
        qtd.setAlignmentX(Component.LEFT_ALIGNMENT);

        info.add(nome);
        info.add(Box.createVerticalStrut(4));
        info.add(descricao);
        info.add(Box.createVerticalStrut(4));
        info.add(qtd);
        info.add(Box.createVerticalStrut(12));

        if (possui) {
            JButton btnUsar = UIUtil.botaoPill("Usar", corTipo, Color.WHITE, 130, 30);
            btnUsar.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnUsar.addActionListener(e -> usarPocao(tipo));
            info.add(btnUsar);
        } else {
            JLabel semEstoque = new JLabel("Vença batalhas para encontrar");
            semEstoque.setFont(new Font("SansSerif", Font.PLAIN, 11));
            semEstoque.setForeground(Constantes.MUTED);
            semEstoque.setAlignmentX(Component.LEFT_ALIGNMENT);
            info.add(semEstoque);
        }

        p.add(info, BorderLayout.CENTER);
        return p;
    }

    private JPanel criarCartaoRevive(TipoRevive tipo) {
        int quantidade = janela.getTreinador().quantidadeDeRevive(tipo);
        boolean possui = quantidade > 0;
        Color corTipo = tipo.corPrincipal();

        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                UIUtil.pintarCartao(g2, getWidth(), getHeight(), 16, Constantes.CARTAO, Constantes.BORDA);
                g2.setColor(possui ? corTipo : new Color(200, 205, 210));
                g2.fillRoundRect(0, 0, 6, getHeight(), 16, 16);
                g2.fillRect(3, 0, 4, getHeight());
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(260, 150));
        p.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 16));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel nome = new JLabel(tipo.getNomeExibicao());
        nome.setFont(new Font("SansSerif", Font.BOLD, 16));
        nome.setForeground(possui ? Constantes.PRETO : Constantes.MUTED);
        nome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descricao = new JLabel(tipo.descricao());
        descricao.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descricao.setForeground(possui ? corTipo : Constantes.MUTED);
        descricao.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel qtd = new JLabel("Quantidade: " + quantidade);
        qtd.setFont(new Font("SansSerif", Font.PLAIN, 12));
        qtd.setForeground(Constantes.MUTED);
        qtd.setAlignmentX(Component.LEFT_ALIGNMENT);

        info.add(nome);
        info.add(Box.createVerticalStrut(4));
        info.add(descricao);
        info.add(Box.createVerticalStrut(4));
        info.add(qtd);
        info.add(Box.createVerticalStrut(12));

        if (possui) {
            JButton btnUsar = UIUtil.botaoPill("Usar", corTipo, Color.WHITE, 130, 30);
            btnUsar.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnUsar.addActionListener(e -> usarRevive(tipo));
            info.add(btnUsar);
        } else {
            JLabel semEstoque = new JLabel("Vença batalhas para encontrar");
            semEstoque.setFont(new Font("SansSerif", Font.PLAIN, 11));
            semEstoque.setForeground(Constantes.MUTED);
            semEstoque.setAlignmentX(Component.LEFT_ALIGNMENT);
            info.add(semEstoque);
        }

        p.add(info, BorderLayout.CENTER);
        return p;
    }

    /** Pede ao jogador qual criatura curar e aplica a poção escolhida. */
    private void usarPocao(TipoPocao tipo) {
        Treinador treinador = janela.getTreinador();
        List<Pokemon> candidatos = new ArrayList<>();
        for (Pokemon c : treinador.getEquipe()) {
            if (c.estaViva() && c.getVidaAtual() < c.getVidaMaxima()) candidatos.add(c);
        }
        if (candidatos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Toda a sua equipe já está com a vida cheia!",
                    "Nada para curar", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] opcoes = new String[candidatos.size()];
        for (int i = 0; i < candidatos.size(); i++) {
            Pokemon c = candidatos.get(i);
            opcoes[i] = c.getNome() + "  (" + c.getVidaAtual() + "/" + c.getVidaMaxima() + " HP)";
        }
        String escolhida = (String) JOptionPane.showInputDialog(this,
                "Em qual criatura usar " + tipo.getNomeExibicao() + " (" + tipo.descricao() + ")?",
                "Usar poção", JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]);
        if (escolhida == null) return;

        Pokemon alvo = candidatos.get(Arrays.asList(opcoes).indexOf(escolhida));
        boolean usada = treinador.usarPocao(tipo, alvo);
        if (usada) {
            JOptionPane.showMessageDialog(this, alvo.getNome() + " recuperou vida! Agora está com "
                    + alvo.getVidaAtual() + "/" + alvo.getVidaMaxima() + " HP.",
                    "Poção usada", JOptionPane.INFORMATION_MESSAGE);
            atualizar();
        }
    }

    /**
     * Pede ao jogador qual criatura desmaiada acordar e aplica o revive
     * escolhido (só oferece criaturas da equipe que estão desmaiadas — vida
     * zerada; poções normais não funcionam nelas, só o revive acorda).
     */
    private void usarRevive(TipoRevive tipo) {
        Treinador treinador = janela.getTreinador();
        List<Pokemon> candidatos = new ArrayList<>();
        for (Pokemon c : treinador.getEquipe()) {
            if (!c.estaViva()) candidatos.add(c);
        }
        if (candidatos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma criatura da sua equipe está desmaiada agora!",
                    "Nada para acordar", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] opcoes = new String[candidatos.size()];
        for (int i = 0; i < candidatos.size(); i++) {
            Pokemon c = candidatos.get(i);
            opcoes[i] = c.getNome() + "  (desmaiado)";
        }
        String escolhida = (String) JOptionPane.showInputDialog(this,
                "Em qual criatura usar " + tipo.getNomeExibicao() + " (" + tipo.descricao() + ")?",
                "Usar revive", JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]);
        if (escolhida == null) return;

        Pokemon alvo = candidatos.get(Arrays.asList(opcoes).indexOf(escolhida));
        boolean usado = treinador.usarRevive(tipo, alvo);
        if (usado) {
            JOptionPane.showMessageDialog(this, alvo.getNome() + " acordou! Agora está com "
                    + alvo.getVidaAtual() + "/" + alvo.getVidaMaxima() + " HP.",
                    "Pokémon acordado", JOptionPane.INFORMATION_MESSAGE);
            atualizar();
        }
    }
}
