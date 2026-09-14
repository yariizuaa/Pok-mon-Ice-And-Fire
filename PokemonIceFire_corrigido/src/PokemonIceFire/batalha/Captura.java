package PokemonIceFire.batalha;

import PokemonIceFire.modelo.Pokemon;

public class Captura {
    public static double calcularChance(Pokemon alvo) {
        double chance = 1.0 - alvo.percentualVida() * 0.75;
        return Math.max(0.15, Math.min(0.95, chance));
    }

    public static boolean tentar(Pokemon alvo) {
        return Math.random() < calcularChance(alvo);
    }
}
