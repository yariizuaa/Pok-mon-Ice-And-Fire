package PokemonIceFire.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PokemonTest {

    @Test
    void danoSemVantagemDeTipo() {
        Pokemon a = new Charmander("A", 50, 20, 10);
        Pokemon b = new Charmander("B", 50, 20, 10); // fogo vs fogo = neutro (1.0x)
        int dano = a.calcularDano(b);
        int base = Math.max(1, a.getAtaque() - b.getDefesa() / 2); // 20 - 5 = 15
        assertEquals(base, dano, "Sem vantagem de tipo, o dano deve ser igual ao valor base");
    }

    @Test
    void danoComVantagemDeTipo() {
        Pokemon fogo = new Charmander("Fogo", 50, 20, 10);
        Pokemon planta = new Bulbassaur("Planta", 50, 20, 20); // vida cheia: nao aciona o bonus especial do fogo
        int dano = fogo.calcularDano(planta);
        int base = Math.max(1, fogo.getAtaque() - planta.getDefesa() / 2); // 20 - 10 = 10
        int esperado = (int) Math.round(base * 1.5); // fogo vence planta
        assertEquals(esperado, dano, "Fogo contra Planta deve aplicar 1.5x de dano");
    }

    @Test
    void danoComDesvantagemDeTipo() {
        Pokemon planta = new Bulbassaur("Planta", 50, 20, 10);
        Pokemon fogo = new Charmander("Fogo", 50, 20, 20);
        int dano = planta.calcularDano(fogo);
        int base = Math.max(1, planta.getAtaque() - fogo.getDefesa() / 2); // 20 - 10 = 10
        int esperado = (int) Math.round(base * 0.67); // planta perde para fogo
        assertEquals(esperado, dano, "Planta contra Fogo deve aplicar o multiplicador de desvantagem (0.67x)");
    }

    @Test
    void danoNuncaFicaAbaixoDeUm() {
        Pokemon fraca = new Squirtle("Fraca", 30, 1, 5);
        Pokemon tanque = new Charmander("Tanque", 100, 5, 100);
        int dano = fraca.calcularDano(tanque);
        assertTrue(dano >= 1, "O dano minimo garantido deve ser 1, mesmo contra defesa muito alta");
    }

    @Test
    void criaturaMorreQuandoVidaChegaAZero() {
        Pokemon c = new Squirtle("Teste", 20, 10, 5);
        assertTrue(c.estaViva());
        c.receberDano(30);
        assertEquals(0, c.getVidaAtual());
        assertFalse(c.estaViva());
    }

    @Test
    void vidaNuncaUltrapassaOMaximoAoCurar() {
        Pokemon c = new Bulbassaur("Teste", 50, 10, 5);
        c.receberDano(5); // vida: 45/50
        c.curar(100);
        assertEquals(50, c.getVidaAtual(), "curar() nao pode deixar a vida atual acima da vida maxima");
    }

    @Test
    void braseiroCausaBonusContraAlvoEnfraquecido() {
        Pokemon braseiro = new Charmander("Charmander", 50, 20, 0);
        Pokemon alvoFraco = new Bulbassaur("AlvoFraco", 100, 10, 0);
        alvoFraco.receberDano(80); // vida: 20/100 = 20%, abaixo de 30%
        Pokemon alvoCheio = new Bulbassaur("AlvoCheio", 100, 10, 0);
        int danoContraFraco = braseiro.calcularDano(alvoFraco);
        int danoContraCheio = braseiro.calcularDano(alvoCheio);
        assertTrue(danoContraFraco > danoContraCheio,
                "Labareda deve causar mais dano contra um alvo com vida abaixo de 30%");
    }

    @Test
    void folharalCuraAoAtacar() {
        Pokemon folharal = new Bulbassaur("Bulbassaur", 50, 15, 5);
        folharal.receberDano(20); // vida: 30/50
        Pokemon alvo = new Squirtle("Alvo", 50, 5, 5);
        int vidaAntes = folharal.getVidaAtual();
        folharal.atacar(alvo);
        assertTrue(folharal.getVidaAtual() > vidaAntes,
                "Sugar Seiva deve curar o Bulbassaur apos ele atacar");
    }

    @Test
    void definirNivelAlteraApenasONivelExibido() {
        Pokemon c = new Squirtle("Teste", 30, 10, 5);
        c.definirNivel(12);
        assertEquals(12, c.getNivel());
        assertEquals(30, c.getVidaMaxima(), "definirNivel nao deve alterar os atributos da criatura");
    }

    @Test
    void ganharXpSuficienteSobeDeNivelEAumentaAtributos() {
        Pokemon c = new Squirtle("Teste", 30, 10, 5);
        c.definirNivel(5);
        int vidaMaximaAntes = c.getVidaMaxima();
        int ataqueAntes = c.getAtaque();
        int xpNecessario = c.getXpParaProximoNivel();

        int niveisGanhos = c.ganharXp(xpNecessario);

        assertEquals(1, niveisGanhos);
        assertEquals(6, c.getNivel());
        assertTrue(c.getVidaMaxima() > vidaMaximaAntes, "Subir de nivel deve aumentar a vida maxima");
        assertTrue(c.getAtaque() > ataqueAntes, "Subir de nivel deve aumentar o ataque");
    }

    @Test
    void ganharXpInsuficienteNaoSobeDeNivel() {
        Pokemon c = new Squirtle("Teste", 30, 10, 5);
        c.definirNivel(5);
        int niveisGanhos = c.ganharXp(1);
        assertEquals(0, niveisGanhos);
        assertEquals(5, c.getNivel());
    }
}
