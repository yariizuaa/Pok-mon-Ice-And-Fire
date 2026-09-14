package PokemonIceFire.treinador;

import PokemonIceFire.modelo.Charmander;
import PokemonIceFire.modelo.Squirtle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PokedexTest {

    @Test
    void naoRegistraAMesmaEspecieDuasVezes() {
        Pokedex b = new Pokedex();
        assertTrue(b.registrar(new Charmander("Um", 30, 10, 5)));
        assertFalse(b.registrar(new Charmander("Outro", 40, 12, 6)),
                "Duas criaturas da mesma especie (mesmo com nomes diferentes) nao devem ser registradas duas vezes");
        assertEquals(1, b.total());
    }

    @Test
    void registraEspeciesDiferentesSeparadamente() {
        Pokedex b = new Pokedex();
        b.registrar(new Charmander("Fogo", 30, 10, 5));
        b.registrar(new Squirtle("Agua", 30, 10, 5));
        assertEquals(2, b.total());
    }
}
