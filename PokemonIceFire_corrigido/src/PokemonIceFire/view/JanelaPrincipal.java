package PokemonIceFire.view;

import PokemonIceFire.modelo.Pokemon;
import PokemonIceFire.treinador.Treinador;
import PokemonIceFire.treinador.Pokedex;
import javax.swing.*;
import java.awt.*;

public class JanelaPrincipal extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cartas = new JPanel(cardLayout);
    private Treinador treinador;
    private Pokedex bestiario = new Pokedex();
    private PainelBatalha painelBatalha;
    private PainelEquipe painelEquipe;
    private PainelItens painelItens;
    private PainelFimDeJogo painelFimDeJogo;

    public JanelaPrincipal() {
        setTitle("Poke Ice and Fire");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        painelBatalha = new PainelBatalha(this);
        painelEquipe = new PainelEquipe(this);
        painelItens = new PainelItens(this);
        painelFimDeJogo = new PainelFimDeJogo(this);

        cartas.add(new PainelMenu(this), "MENU");
        cartas.add(new PainelEscolha(this), "ESCOLHA");
        cartas.add(painelBatalha, "BATALHA");
        cartas.add(painelEquipe, "EQUIPE");
        cartas.add(painelItens, "ITENS");
        cartas.add(painelFimDeJogo, "FIM_DE_JOGO");

        add(cartas);
        mostrar("MENU");
    }

    void mostrar(String nome) {
        if (nome.equals("EQUIPE")) painelEquipe.atualizar();
        if (nome.equals("ITENS")) painelItens.atualizar();
        if (nome.equals("BATALHA")) painelBatalha.atualizarVidaExibida();
        if (nome.equals("FIM_DE_JOGO")) painelFimDeJogo.atualizar(treinador, bestiario);
        cardLayout.show(cartas, nome);
    }

    void iniciarJornada(Pokemon inicial) {
        treinador = new Treinador("Treinador");
        treinador.adicionarNaEquipe(inicial);
        bestiario.registrar(inicial);
        painelBatalha.iniciar(treinador);
        mostrar("BATALHA");
    }

    /**
     * Chamado pela tela de Fim de Jogo quando o jogador clica em "Reiniciar
     * Jornada" após uma derrota completa (toda a equipe desmaiada). Zera o
     * treinador e o bestiário e volta para a tela de escolha do pokémon
     * inicial, como se o jogo estivesse começando do zero.
     */
    void reiniciarJornada() {
        treinador = null;
        bestiario = new Pokedex();
        mostrar("ESCOLHA");
    }

    /**
     * Chamado pela tela de Fim de Jogo quando o jogador clica em "Menu
     * Principal" em vez de reiniciar direto. Também zera o treinador e o
     * bestiário (a jornada anterior já terminou), mas volta para a tela
     * inicial em vez de pular direto para a escolha do pokémon inicial.
     */
    void voltarAoMenu() {
        treinador = null;
        bestiario = new Pokedex();
        mostrar("MENU");
    }

    Treinador getTreinador() { return treinador; }
    Pokedex getBestiario() { return bestiario; }

    /** Diz à aba Equipe/Bestiário se ela deve exibir os botões de seleção de criatura. */
    boolean podeTrocarPokemon() { return painelBatalha.podeTrocarPokemon(); }

    /** Chamado pela aba Equipe/Bestiário quando o treinador escolhe uma criatura para entrar em campo. */
    void selecionarPokemon(Pokemon p) {
        painelBatalha.trocarPokemonAtivo(p);
        mostrar("BATALHA");
    }
}

// =========================================================================
