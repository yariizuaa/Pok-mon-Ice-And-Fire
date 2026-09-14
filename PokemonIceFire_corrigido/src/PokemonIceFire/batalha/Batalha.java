package PokemonIceFire.batalha;

import PokemonIceFire.modelo.Pokemon;
import java.util.List;
import java.util.ArrayList;

public class Batalha {
    private Pokemon doJogador;
    private Pokemon selvagem;
    private List<String> log = new ArrayList<>();
    private int ultimoDanoCausado;   // dano que o jogador causou no ultimo turno
    private int ultimoDanoSofrido;   // dano que o jogador sofreu no ultimo turno (0 se o selvagem foi derrotado antes de revidar)

    public Batalha(Pokemon doJogador, Pokemon selvagem) {
        this.doJogador = doJogador;
        this.selvagem = selvagem;
        log.add("Um " + selvagem.getNome() + " selvagem apareceu!");
    }

    public Pokemon getDoJogador() { return doJogador; }
    public Pokemon getSelvagem() { return selvagem; }
    public List<String> getLog() { return log; }

    /** Dano que o jogador causou no ultimo turno (usado para animar a barra de vida/numero flutuante). */
    public int getUltimoDanoCausado() { return ultimoDanoCausado; }
    /** Dano que o jogador sofreu no ultimo turno (0 se o selvagem foi derrotado antes de revidar). */
    public int getUltimoDanoSofrido() { return ultimoDanoSofrido; }

    /**
     * Troca a criatura do jogador em campo por outra da equipe (usado quando
     * o treinador escolhe uma nova criatura na aba Equipe/Bestiário durante a batalha).
     */
    public void trocarPokemonJogador(Pokemon novo) {
        if (novo == null || !novo.estaViva() || novo == doJogador) return;
        log.add(doJogador.getNome() + " foi recolhido. Vai, " + novo.getNome() + "!");
        this.doJogador = novo;
    }

    public void turno() {
        if (terminou()) return;
        ultimoDanoSofrido = 0;
        int dano1 = doJogador.atacar(selvagem);
        ultimoDanoCausado = dano1;
        log.add(doJogador.getNome() + " atacou e causou " + dano1 + " de dano!");
        if (!selvagem.estaViva()) {
            log.add(selvagem.getNome() + " selvagem foi derrotado!");
            return;
        }
        int dano2 = selvagem.atacar(doJogador);
        ultimoDanoSofrido = dano2;
        log.add(selvagem.getNome() + " revidou e causou " + dano2 + " de dano!");
        if (!doJogador.estaViva()) {
            log.add(doJogador.getNome() + " não pode continuar!");
        }
    }

    public boolean terminou() { return !doJogador.estaViva() || !selvagem.estaViva(); }
}

// =========================================================================
// VIEW — desenho das criaturas
// =========================================================================
