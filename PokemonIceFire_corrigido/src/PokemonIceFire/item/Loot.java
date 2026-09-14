package PokemonIceFire.item;

import java.util.Random;

/**
 * Sorteia, após cada batalha vencida, se o jogador encontra itens e, em caso
 * positivo, quais tipos — usando o peso de cada {@link TipoPocao}/{@link
 * TipoRevive} como chance relativa dentro do próprio grupo. Itens mais
 * fortes são mais raros. Poção e revive são sorteados de forma independente,
 * ou seja, uma mesma vitória pode render os dois ao mesmo tempo.
 */
public final class Loot {
    /** Chance de encontrar uma poção ao vencer uma batalha (aumentada de 45% para 75%). */
    private static final double CHANCE_DE_DROP = 0.75;

    /** Chance de encontrar um revive ao vencer uma batalha (item novo, mais raro que poção). */
    private static final double CHANCE_DE_DROP_REVIVE = 0.35;

    private static final Random RNG = new Random();

    private Loot() {}

    /**
     * @return o tipo de poção encontrada, ou {@code null} se, dessa vez,
     * nenhuma poção foi encontrada.
     */
    public static TipoPocao sortearAposVitoria() {
        return sortearAposVitoria(RNG);
    }

    /** Sobrecarga que recebe o gerador de números aleatórios (facilita testes). */
    static TipoPocao sortearAposVitoria(Random rng) {
        if (rng.nextDouble() >= CHANCE_DE_DROP) return null;

        int pesoTotal = 0;
        for (TipoPocao tipo : TipoPocao.values()) pesoTotal += tipo.getPeso();

        int sorteio = rng.nextInt(pesoTotal);
        int acumulado = 0;
        for (TipoPocao tipo : TipoPocao.values()) {
            acumulado += tipo.getPeso();
            if (sorteio < acumulado) return tipo;
        }
        return TipoPocao.values()[0]; // nunca deveria chegar aqui
    }

    /**
     * @return o tipo de revive encontrado, ou {@code null} se, dessa vez,
     * nenhum revive foi encontrado. Sorteado de forma independente da poção.
     */
    public static TipoRevive sortearReviveAposVitoria() {
        return sortearReviveAposVitoria(RNG);
    }

    /** Sobrecarga que recebe o gerador de números aleatórios (facilita testes). */
    static TipoRevive sortearReviveAposVitoria(Random rng) {
        if (rng.nextDouble() >= CHANCE_DE_DROP_REVIVE) return null;

        int pesoTotal = 0;
        for (TipoRevive tipo : TipoRevive.values()) pesoTotal += tipo.getPeso();

        int sorteio = rng.nextInt(pesoTotal);
        int acumulado = 0;
        for (TipoRevive tipo : TipoRevive.values()) {
            acumulado += tipo.getPeso();
            if (sorteio < acumulado) return tipo;
        }
        return TipoRevive.values()[0]; // nunca deveria chegar aqui
    }
}
