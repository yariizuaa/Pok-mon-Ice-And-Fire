package PokemonIceFire.treinador;

import PokemonIceFire.modelo.Pokemon;
import PokemonIceFire.modelo.Squirtle;
import PokemonIceFire.item.TipoPocao;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TreinadorTest {

    @Test
    void naoAdicionaAlemDoLimiteDeSeis() {
        Treinador t = new Treinador("Ana");
        for (int i = 0; i < 6; i++) {
            assertTrue(t.adicionarNaEquipe(new Squirtle("C" + i, 20, 5, 5)));
        }
        boolean adicionouSetima = t.adicionarNaEquipe(new Squirtle("Setima", 20, 5, 5));
        assertFalse(adicionouSetima, "Nao deve ser possivel adicionar uma setima criatura na equipe");
        assertEquals(6, t.getEquipe().size());
    }

    @Test
    void getAtivaRetornaPrimeiraCriaturaViva() {
        Treinador t = new Treinador("Ana");
        Pokemon desmaiada = new Squirtle("Desmaiada", 20, 5, 5);
        desmaiada.receberDano(20);
        Pokemon viva = new Squirtle("Viva", 20, 5, 5);
        t.adicionarNaEquipe(desmaiada);
        t.adicionarNaEquipe(viva);
        assertEquals(viva, t.getAtiva(), "getAtiva deve pular criaturas desmaiadas e retornar a primeira viva");
    }

    @Test
    void usarPocaoCuraEConsomeUmaUnidadeDoInventario() {
        Treinador t = new Treinador("Ana");
        Pokemon ferida = new Squirtle("Ferida", 100, 5, 5);
        ferida.receberDano(60); // fica com 40/100 HP
        t.adicionarNaEquipe(ferida);
        t.adicionarItem(TipoPocao.MEDIA, 2);

        boolean usou = t.usarPocao(TipoPocao.MEDIA, ferida);

        assertTrue(usou);
        assertEquals(90, ferida.getVidaAtual(), "Poção Média deve curar 50 HP (40 + 50 = 90)");
        assertEquals(1, t.quantidadeDe(TipoPocao.MEDIA), "Deve consumir exatamente uma unidade da poção");
    }

    @Test
    void naoUsaPocaoQueNaoPossuiNoInventario() {
        Treinador t = new Treinador("Ana");
        Pokemon ferida = new Squirtle("Ferida", 100, 5, 5);
        ferida.receberDano(60);
        t.adicionarNaEquipe(ferida);

        boolean usou = t.usarPocao(TipoPocao.GRANDE, ferida);

        assertFalse(usou, "Nao deve ser possivel usar uma pocao que o treinador nao possui");
        assertEquals(40, ferida.getVidaAtual(), "A vida nao deve mudar quando a pocao nao pode ser usada");
    }
}
