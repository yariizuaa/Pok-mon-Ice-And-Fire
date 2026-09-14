package PokemonIceFire.batalha;

import PokemonIceFire.modelo.Pokemon;
import PokemonIceFire.modelo.Squirtle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CapturaTest {

    @Test
    void chanceEhMaiorQuandoAlvoEstaComPoucaVida() {
        Pokemon vidaCheia = new Squirtle("A", 50, 10, 5);
        Pokemon vidaBaixa = new Squirtle("B", 50, 10, 5);
        vidaBaixa.receberDano(45); // vida: 5/50 = 10%
        double chanceCheia = Captura.calcularChance(vidaCheia);
        double chanceBaixa = Captura.calcularChance(vidaBaixa);
        assertTrue(chanceBaixa > chanceCheia,
                "A chance de captura deve aumentar conforme a vida do alvo diminui");
    }

    @Test
    void chanceNuncaFicaAbaixoDoMinimo() {
        Pokemon vidaCheia = new Squirtle("Cheia", 50, 10, 5);
        double chance = Captura.calcularChance(vidaCheia);
        assertTrue(chance >= 0.15, "A chance minima de captura deve ser 15%, mesmo com o alvo em vida cheia");
    }

    @Test
    void chanceNuncaUltrapassaOMaximo() {
        Pokemon quaseMorta = new Squirtle("QuaseMorta", 100, 10, 5);
        quaseMorta.receberDano(99);
        double chance = Captura.calcularChance(quaseMorta);
        assertTrue(chance <= 0.95, "A chance maxima de captura deve ser 95%, mesmo com o alvo quase derrotado");
    }
}
