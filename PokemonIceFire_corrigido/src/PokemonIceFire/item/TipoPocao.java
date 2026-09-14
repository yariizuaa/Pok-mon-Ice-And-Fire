package PokemonIceFire.item;

import java.awt.Color;

/**
 * Tipos de poção que podem ser encontrados aleatoriamente após vencer uma
 * batalha. Cada tipo cura uma quantidade fixa de HP; quanto maior a cura,
 * menor o peso (chance relativa) de ela ser sorteada — ver {@link Loot}.
 */
public enum TipoPocao {
    PEQUENA("Poção Pequena", 20, 50),
    MEDIA("Poção Média", 50, 30),
    GRANDE("Poção Grande", 100, 15);

    private final String nomeExibicao;
    private final int cura;
    private final int peso;

    TipoPocao(String nomeExibicao, int cura, int peso) {
        this.nomeExibicao = nomeExibicao;
        this.cura = cura;
        this.peso = peso;
    }

    public String getNomeExibicao() { return nomeExibicao; }
    /** Quantidade de HP restaurada ao usar uma unidade desta poção. */
    public int getCura() { return cura; }
    /** Peso relativo no sorteio de itens (quanto maior, mais comum). */
    public int getPeso() { return peso; }

    public String descricao() { return "Restaura " + cura + " HP"; }

    public Color corPrincipal() {
        switch (this) {
            case PEQUENA: return new Color(120, 190, 120);
            case MEDIA:   return new Color(240, 173, 61);
            case GRANDE:  return new Color(220, 90, 90);
            default:      return Color.GRAY;
        }
    }
}
