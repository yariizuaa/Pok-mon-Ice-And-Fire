package PokemonIceFire.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExperienciaTest {

    @Test
    void inimigoDeNivelMaiorRendeMaisXp() {
        Pokemon inimigoFraco = new Squirtle("Fraco", 30, 5, 5);
        inimigoFraco.definirNivel(5);
        Pokemon inimigoForte = new Squirtle("Forte", 30, 5, 5);
        inimigoForte.definirNivel(15);

        int xpFraco = Experiencia.calcularXpDeVitoria(inimigoFraco);
        int xpForte = Experiencia.calcularXpDeVitoria(inimigoForte);

        assertTrue(xpForte > xpFraco, "Derrotar um inimigo de nivel mais alto deve render mais XP");
    }

    @Test
    void semInimigoNaoRendeXp() {
        assertEquals(0, Experiencia.calcularXpDeVitoria(null));
    }
}
