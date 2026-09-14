package PokemonIceFire.item;

import java.awt.Color;

/**
 * Tipos de revive que podem ser encontrados aleatoriamente após vencer uma
 * batalha (ver {@link Loot}). Diferente de {@link TipoPocao} — que só cura
 * uma criatura viva — o revive é o único item capaz de acordar uma criatura
 * desmaiada (vida zerada), devolvendo uma porcentagem da vida máxima dela.
 */
public enum TipoRevive {
    REVIVE("Revive", 50, 20),
    REVIVE_MAX("Revive Max", 100, 8);

    private final String nomeExibicao;
    private final int percentualVida; // % da vida maxima com que a criatura acorda
    private final int peso;

    TipoRevive(String nomeExibicao, int percentualVida, int peso) {
        this.nomeExibicao = nomeExibicao;
        this.percentualVida = percentualVida;
        this.peso = peso;
    }

    public String getNomeExibicao() { return nomeExibicao; }
    /** Porcentagem da vida máxima com que a criatura acorda ao usar este item. */
    public int getPercentualVida() { return percentualVida; }
    /** Peso relativo no sorteio de itens (quanto maior, mais comum). */
    public int getPeso() { return peso; }

    public String descricao() {
        return "Acorda um pokémon desmaiado com " + percentualVida + "% da vida";
    }

    public Color corPrincipal() {
        switch (this) {
            case REVIVE:     return new Color(150, 120, 220);
            case REVIVE_MAX: return new Color(230, 190, 60);
            default:         return Color.GRAY;
        }
    }
}
