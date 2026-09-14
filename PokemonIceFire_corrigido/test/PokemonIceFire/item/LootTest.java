package PokemonIceFire.item;

import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class LootTest {

    @Test
    void naoEncontraItemQuandoSorteioFicaAcimaDaChanceDeDrop() {
        // nextDouble() fixo em 0.99 -> maior que a chance de drop (0.45) -> sem item
        Random semDrop = new Random() {
            @Override public double nextDouble() { return 0.99; }
        };
        assertNull(Loot.sortearAposVitoria(semDrop));
    }

    @Test
    void encontraPocaoPequenaQuandoSorteioCaiNaPrimeiraFaixaDePeso() {
        // nextDouble() fixo em 0.0 -> dentro da chance de drop -> sorteia um tipo
        // nextInt(pesoTotal) fixo em 0 -> cai na primeira faixa (PEQUENA)
        Random rng = new Random() {
            @Override public double nextDouble() { return 0.0; }
            @Override public int nextInt(int bound) { return 0; }
        };
        assertEquals(TipoPocao.PEQUENA, Loot.sortearAposVitoria(rng));
    }

    @Test
    void encontraPocaoGrandeQuandoSorteioCaiNaUltimaFaixaDePeso() {
        int pesoTotal = TipoPocao.PEQUENA.getPeso() + TipoPocao.MEDIA.getPeso() + TipoPocao.GRANDE.getPeso();
        Random rng = new Random() {
            @Override public double nextDouble() { return 0.0; }
            @Override public int nextInt(int bound) { return pesoTotal - 1; }
        };
        assertEquals(TipoPocao.GRANDE, Loot.sortearAposVitoria(rng));
    }
}
