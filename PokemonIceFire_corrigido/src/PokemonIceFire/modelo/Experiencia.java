package PokemonIceFire.modelo;

/**
 * Calcula quanta XP o pokémon do jogador ganha ao vencer uma batalha, com
 * base no nível do pokémon selvagem derrotado (inimigos de nível mais alto
 * rendem mais experiência).
 */
public final class Experiencia {
    private static final int XP_BASE = 15;
    private static final int XP_POR_NIVEL_DO_INIMIGO = 8;

    private Experiencia() {}

    public static int calcularXpDeVitoria(Pokemon inimigoDerrotado) {
        if (inimigoDerrotado == null) return 0;
        return XP_BASE + inimigoDerrotado.getNivel() * XP_POR_NIVEL_DO_INIMIGO;
    }
}
