package PokemonIceFire.view;

import PokemonIceFire.modelo.Pokemon;
import PokemonIceFire.modelo.FabricaPokemons;
import PokemonIceFire.modelo.Experiencia;
import PokemonIceFire.treinador.Treinador;
import PokemonIceFire.batalha.Batalha;
import PokemonIceFire.batalha.Captura;
import PokemonIceFire.item.TipoPocao;
import PokemonIceFire.item.TipoRevive;
import PokemonIceFire.item.Loot;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PainelBatalha extends JPanel {
    private JanelaPrincipal janela;
    private PainelArena arena = new PainelArena();
    private BarraVida barraJogador, barraSelvagem;
    private JTextArea logArea = new JTextArea();
    private JButton btnAtacar, btnCapturar, btnFugir, btnExplorar;
    private JLabel lblTreinador = new JLabel(" ");
    private JLabel lblEquipe = new JLabel(" ");
    private Batalha batalhaAtual;

    PainelBatalha(JanelaPrincipal janela) {
        this.janela = janela;
        setLayout(new BorderLayout(14, 14));
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        add(criarCabecalho(), BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(10, 10));
        centro.setOpaque(false);
        centro.add(arena, BorderLayout.CENTER);

        JPanel painelBarras = new JPanel(new BorderLayout());
        painelBarras.setOpaque(false);
        painelBarras.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        barraJogador = new BarraVida(null);
        barraSelvagem = new BarraVida(null);
        JPanel esqBarra = new JPanel(); esqBarra.setOpaque(false); esqBarra.add(barraJogador);
        JPanel dirBarra = new JPanel(); dirBarra.setOpaque(false); dirBarra.add(barraSelvagem);
        painelBarras.add(esqBarra, BorderLayout.WEST);
        painelBarras.add(dirBarra, BorderLayout.EAST);
        centro.add(painelBarras, BorderLayout.SOUTH);
        add(centro, BorderLayout.CENTER);

        add(criarPainelLateral(), BorderLayout.EAST);
        atualizarBotoes();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        UIUtil.pintarFundoTela(g2, getWidth(), getHeight());
        g2.dispose();
    }

    /** Cartao superior com nome do treinador e acesso a equipe/bestiario. */
    private JPanel criarCabecalho() {
        JPanel topo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                UIUtil.pintarCartao(g2, getWidth(), getHeight(), 16, Constantes.PRETO, null);
                g2.dispose();
            }
        };
        topo.setOpaque(false);
        topo.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 14));
        topo.setPreferredSize(new Dimension(10, 60));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        lblTreinador.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTreinador.setForeground(Color.WHITE);
        lblEquipe.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblEquipe.setForeground(Constantes.VERMELHO);
        textos.add(lblTreinador);
        textos.add(lblEquipe);
        topo.add(textos, BorderLayout.WEST);

        JButton btnEquipe = UIUtil.botaoPill("Equipe / Bestiário", Constantes.VERMELHO, Constantes.PRETO, 190, 36);
        btnEquipe.addActionListener(e -> janela.mostrar("EQUIPE"));
        JButton btnItens = UIUtil.botaoPill("Itens", Constantes.CREME, Constantes.PRETO, 110, 36);
        btnItens.addActionListener(e -> janela.mostrar("ITENS"));
        JPanel wrapBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        wrapBtn.setOpaque(false);
        wrapBtn.add(btnEquipe);
        wrapBtn.add(btnItens);
        topo.add(wrapBtn, BorderLayout.EAST);

        return topo;
    }

    /** Cartao lateral com o registro de batalha e os botoes de acao. */
    private JPanel criarPainelLateral() {
        JPanel lado = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                UIUtil.pintarCartao(g2, getWidth(), getHeight(), 18, Constantes.CARTAO, Constantes.BORDA);
                g2.dispose();
            }
        };
        lado.setOpaque(false);
        lado.setLayout(new BoxLayout(lado, BoxLayout.Y_AXIS));
        lado.setPreferredSize(new Dimension(250, 10));
        lado.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel lblLog = new JLabel("Registro de batalha");
        lblLog.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblLog.setForeground(Constantes.PRETO);
        lblLog.setAlignmentX(Component.LEFT_ALIGNMENT);
        lado.add(lblLog);
        lado.add(Box.createVerticalStrut(8));

        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        logArea.setBackground(Constantes.PRETO);
        logArea.setForeground(Color.WHITE);
        logArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setPreferredSize(new Dimension(218, 150));
        scroll.setMaximumSize(new Dimension(218, 150));
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setBorder(BorderFactory.createLineBorder(Constantes.PRETO, 1, true));
        lado.add(scroll);
        lado.add(Box.createVerticalStrut(16));

        btnExplorar = UIUtil.botaoPill("Explorar", Constantes.VERMELHO, Constantes.PRETO, 218, 42);
        btnExplorar.addActionListener(e -> explorar());
        btnAtacar = UIUtil.botaoPill("Atacar", Constantes.CREME, Constantes.PRETO, 218, 42);
        btnAtacar.addActionListener(e -> atacar());
        btnCapturar = UIUtil.botaoPill("Tentar Capturar", Constantes.VERMELHO, Constantes.PRETO, 218, 42);
        btnCapturar.addActionListener(e -> capturar());
        btnFugir = UIUtil.botaoPill("Fugir", Constantes.PRETO2, Color.WHITE, 218, 42);
        btnFugir.addActionListener(e -> fugir());

        for (JButton b : new JButton[]{btnExplorar, btnAtacar, btnCapturar, btnFugir}) {
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            lado.add(b);
            lado.add(Box.createVerticalStrut(10));
        }

        return lado;
    }

    /**
     * Re-sincroniza a barra de vida do jogador com o estado atual da criatura
     * ativa. Chamado ao retornar à tela de Batalha (ex.: depois de usar uma
     * poção na aba Itens), já que a cura altera o mesmo objeto Pokemon mas a
     * barra só é repintada quando alguém manda explicitamente.
     */
    void atualizarVidaExibida() {
        Pokemon ativa = janela.getTreinador().getAtiva();
        if (ativa != null) barraJogador.setCriatura(ativa);
    }

    void iniciar(Treinador t) {
        batalhaAtual = null;
        Pokemon ativa = t.getAtiva();
        barraJogador.setCriatura(ativa);
        arena.configurar(ativa != null ? ativa.getTipo() : null, ativa != null && !ativa.estaViva(), null, false);
        lblTreinador.setText("Treinador: " + t.getNome());
        lblEquipe.setText("Equipe: " + t.getEquipe().size() + "/" + Treinador.getTamanhoMaxEquipe());
        logArea.setText("Sua jornada começa! Clique em Explorar para encontrar uma criatura selvagem.\n");
        atualizarBotoes();
    }


    private void explorar() {
        Pokemon ativa = janela.getTreinador().getAtiva();
        if (ativa == null) {
            logArea.append("Sua equipe não tem nenhuma criatura em condições de batalhar!\n");
            atualizarBotoes();
            return;
        }
        Pokemon selvagem = FabricaPokemons.selvagemAleatorio(janela.getTreinador().getMaiorNivelDaEquipe());
        batalhaAtual = new Batalha(ativa, selvagem);
        barraJogador.setCriatura(ativa);
        barraSelvagem.setCriatura(selvagem);
        arena.configurar(ativa.getTipo(), false, selvagem.getTipo(), false);
        logArea.setText("");
        for (String linha : batalhaAtual.getLog()) logArea.append(linha + "\n");
        atualizarBotoes();
    }

    private void atacar() {
        if (batalhaAtual == null || batalhaAtual.terminou()) return;
        batalhaAtual.turno();
        processarResultadoDoTurno();
    }

    /**
     * Depois de um turno (seja por "Atacar" ou por uma tentativa de captura
     * fracassada — que também gasta o turno), atualiza log/arena e trata o
     * desfecho: vitória (selvagem derrotado), derrota total (toda a equipe
     * desmaiada) ou apenas o pokémon ativo desmaiado (aguardando o jogador
     * escolher outro na aba Equipe — o selvagem continua em campo).
     */
    private void processarResultadoDoTurno() {
        atualizarLog();
        exibirAnimacoesDeDano();
        arena.configurar(batalhaAtual.getDoJogador().getTipo(), !batalhaAtual.getDoJogador().estaViva(),
                batalhaAtual.getSelvagem().getTipo(), !batalhaAtual.getSelvagem().estaViva());
        if (batalhaAtual.terminou()) {
            if (!batalhaAtual.getSelvagem().estaViva()) {
                janela.getBestiario().registrar(batalhaAtual.getSelvagem());
                logArea.append("Você venceu a batalha!\n");
                concederXpDeVitoria();
                sortearItemDeVitoria();
                barraJogador.setCriatura(batalhaAtual.getDoJogador()); // resincroniza a barra (vida maxima pode ter aumentado ao subir de nivel)
            } else if (janela.getTreinador().getAtiva() == null) {
                logArea.append("Todos os seus pokémon desmaiaram! Sua jornada chega ao fim...\n");
                agendarTransicaoParaFimDeJogo();
            } else {
                logArea.append("Escolha outra criatura na aba Equipe/Bestiário para continuar a batalha contra "
                        + batalhaAtual.getSelvagem().getNome() + "!\n");
            }
            atualizarBotoes();
        }
    }

    /**
     * Espera a animação de dano na tela de batalha terminar (ver
     * {@link BarraVida#animarDano}) antes de levar o jogador para a tela
     * dedicada de Fim de Jogo — assim ele ainda vê o golpe final acontecer,
     * em vez de a tela trocar de repente no meio da animação.
     */
    private void agendarTransicaoParaFimDeJogo() {
        Timer atraso = new Timer(1300, e -> janela.mostrar("FIM_DE_JOGO"));
        atraso.setRepeats(false);
        atraso.start();
    }

    /**
     * Anima a redução das barras de vida e mostra o dano perdido (ex.: "-10")
     * ao lado de cada criatura, com base no último turno registrado em Batalha.
     */
    private void exibirAnimacoesDeDano() {
        int danoNoSelvagem = batalhaAtual.getUltimoDanoCausado();
        if (danoNoSelvagem > 0) {
            barraSelvagem.animarDano(danoNoSelvagem);
            arena.mostrarDano(false, danoNoSelvagem);
        }
        int danoNoJogador = batalhaAtual.getUltimoDanoSofrido();
        if (danoNoJogador > 0) {
            barraJogador.animarDano(danoNoJogador);
            arena.mostrarDano(true, danoNoJogador);
        }
    }

    /**
     * Concede XP ao pokémon do jogador com base no nível do inimigo derrotado
     * (ver {@link Experiencia}) e avisa no registro de batalha, inclusive
     * quando o ganho de XP faz o pokémon subir de nível.
     */
    private void concederXpDeVitoria() {
        Pokemon vencedor = batalhaAtual.getDoJogador();
        int xpGanho = Experiencia.calcularXpDeVitoria(batalhaAtual.getSelvagem());
        int niveisGanhos = vencedor.ganharXp(xpGanho);
        logArea.append(vencedor.getNome() + " ganhou " + xpGanho + " XP!\n");
        if (niveisGanhos > 0) {
            logArea.append(vencedor.getNome() + " subiu para o nível " + vencedor.getNivel() + "!\n");
            avisarEvolucao(vencedor);
        }
    }

    /**
     * Mostra em tela um aviso de que a criatura evoluiu ao subir de nível
     * (chamado logo depois que {@link Pokemon#ganharXp} confirma o(s)
     * nível(is) ganho(s)).
     */
    private void avisarEvolucao(Pokemon vencedor) {
        JOptionPane.showMessageDialog(this,
                vencedor.getNome() + " está evoluindo!\nAgora está no nível " + vencedor.getNivel()
                        + " e ficou mais forte (mais vida, ataque e defesa).",
                "Seu pokémon evoluiu!", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Sorteia, após uma vitória, se o treinador encontra uma poção e/ou um
     * revive (ver {@link Loot}) e, em caso positivo, adiciona ao inventário
     * e avisa no registro de batalha. Os dois sorteios são independentes:
     * a mesma vitória pode render poção, revive, os dois, ou nenhum.
     */
    private void sortearItemDeVitoria() {
        TipoPocao pocaoEncontrada = Loot.sortearAposVitoria();
        if (pocaoEncontrada != null) {
            janela.getTreinador().adicionarItem(pocaoEncontrada, 1);
            logArea.append("Você encontrou 1x " + pocaoEncontrada.getNomeExibicao()
                    + " (" + pocaoEncontrada.descricao() + ")! Guardada na aba Itens.\n");
        }

        TipoRevive reviveEncontrado = Loot.sortearReviveAposVitoria();
        if (reviveEncontrado != null) {
            janela.getTreinador().adicionarItemRevive(reviveEncontrado, 1);
            logArea.append("Você encontrou 1x " + reviveEncontrado.getNomeExibicao()
                    + " (" + reviveEncontrado.descricao() + ")! Guardado na aba Itens.\n");
        }
    }

    private void capturar() {
        if (batalhaAtual == null || batalhaAtual.terminou()) return;
        Pokemon selvagem = batalhaAtual.getSelvagem();
        if (Captura.tentar(selvagem)) {
            logArea.append("Você capturou " + selvagem.getNome() + "!\n");
            janela.getBestiario().registrar(selvagem);
            if (!janela.getTreinador().adicionarNaEquipe(selvagem)) {
                logArea.append("(Equipe cheia — a criatura foi registrada no Bestiário, mas não coube no time)\n");
            }
            batalhaAtual = null;
            Pokemon ativa = janela.getTreinador().getAtiva();
            arena.configurar(ativa != null ? ativa.getTipo() : null, false, null, false);
            atualizarBotoes();
        } else {
            logArea.append(selvagem.getNome() + " escapou da captura!\n");
            batalhaAtual.turno();
            processarResultadoDoTurno();
        }
    }

    private void fugir() {
        logArea.append("Você fugiu da batalha.\n");
        batalhaAtual = null;
        arena.configurar(janela.getTreinador().getAtiva().getTipo(), false, null, false);
        atualizarBotoes();
    }

    private void atualizarLog() {
        List<String> log = batalhaAtual.getLog();
        logArea.append(log.get(log.size() - 1) + "\n");
    }

    /**
     * Alterna a visibilidade dos botões de ação com base no estado atual
     * (calculado sozinho a partir de batalhaAtual e do treinador — não
     * recebe mais parâmetro):
     * - Nenhuma batalha em andamento (ou o selvagem já foi derrotado/fugiu):
     *   só "Explorar".
     * - Batalha em andamento, com o pokémon ativo vivo: "Atacar",
     *   "Tentar Capturar" e "Fugir".
     * - O selvagem ainda está em campo mas o pokémon ativo desmaiou (e a
     *   equipe ainda tem outra criatura viva): nenhum botão — o jogador
     *   precisa ir à aba Equipe/Bestiário escolher outra criatura.
     * - Toda a equipe desmaiada (derrota completa): nenhum botão aqui —
     *   {@link #agendarTransicaoParaFimDeJogo()} já leva o jogador para a
     *   tela dedicada de Fim de Jogo.
     */
    private void atualizarBotoes() {
        boolean derrotaTotal = janela.getTreinador() != null && janela.getTreinador().getAtiva() == null;
        boolean selvagemEmCampo = !derrotaTotal && batalhaAtual != null && batalhaAtual.getSelvagem().estaViva();
        boolean emBatalha = selvagemEmCampo && batalhaAtual.getDoJogador().estaViva();
        boolean semBatalha = !derrotaTotal && !selvagemEmCampo;

        btnExplorar.setVisible(semBatalha);
        btnAtacar.setVisible(emBatalha);
        btnCapturar.setVisible(emBatalha);
        btnFugir.setVisible(emBatalha);
    }

    /**
     * Indica se, no momento, o treinador pode escolher outra criatura na aba
     * Equipe/Bestiário: isso acontece quando a criatura ativa foi abatida
     * (a batalha terminou em derrota) ou quando a batalha está em andamento
     * (é a vez do jogador atacar/agir).
     */
    boolean podeTrocarPokemon() {
        if (batalhaAtual == null) return false;
        if (!batalhaAtual.terminou()) return true; // sua vez de atacar
        return !batalhaAtual.getDoJogador().estaViva(); // pokemon foi abatido
    }

    /**
     * Chamado a partir da aba Equipe/Bestiário quando o treinador seleciona
     * uma criatura para entrar em campo. Se o selvagem ainda estiver vivo
     * (batalha em andamento, ou o pokémon do jogador acabou de desmaiar), a
     * MESMA batalha continua contra o mesmo selvagem — ele só sai de campo
     * quando é realmente derrotado (ou o jogador foge/captura).
     */
    void trocarPokemonAtivo(Pokemon novo) {
        if (novo == null || !novo.estaViva()) return;
        janela.getTreinador().definirAtiva(novo);
        barraJogador.setCriatura(novo);

        if (batalhaAtual != null && batalhaAtual.getSelvagem().estaViva()) {
            if (batalhaAtual.getDoJogador() != novo) {
                batalhaAtual.trocarPokemonJogador(novo);
                atualizarLog();
            }
            arena.configurar(novo.getTipo(), false, batalhaAtual.getSelvagem().getTipo(), false);
        } else if (batalhaAtual != null) {
            // O selvagem ja foi derrotado (vitoria) — a troca aqui so prepara a cena para a proxima exploracao.
            if (batalhaAtual.getDoJogador() != novo) {
                logArea.append(novo.getNome() + " entrou em campo, pronto para continuar a jornada!\n");
            }
            batalhaAtual = null;
            arena.configurar(novo.getTipo(), false, null, false);
        }
        atualizarBotoes();
        barraJogador.repaint();
    }
}
