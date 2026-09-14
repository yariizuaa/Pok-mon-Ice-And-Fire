package PokemonIceFire.modelo;

public class FabricaPokemons {

    /** Nível padrão de todo pokémon inicial escolhido pelo treinador. */
    public static final int NIVEL_PADRAO = 10;
    /** Quantos níveis, no máximo, um pokémon selvagem pode ter a mais que o nível padrão. */
    private static final int VARIACAO_MAX_NIVEL_SELVAGEM = 5;

    public static Pokemon novaInicial(TipoElemental tipo) {
        Pokemon p;
        switch (tipo) {
            case FOGO: p = new Charmander("Charmander", 42, 13, 8); break;
            case AGUA: p = new Squirtle("Squirtle", 46, 11, 10); break;
            case PLANTA: p = new Bulbassaur("Bulbasaur", 44, 11, 9); break;
            default: throw new IllegalArgumentException("Tipo desconhecido");
        }
        p.definirNivel(NIVEL_PADRAO);
        return p;
    }

    public static Pokemon selvagemAleatorio() {
        TipoElemental[] tipos = TipoElemental.values();
        TipoElemental tipo = tipos[(int) (Math.random() * tipos.length)];
        int vida = 24 + (int) (Math.random() * 18);
        int ataque = 7 + (int) (Math.random() * 7);
        int defesa = 5 + (int) (Math.random() * 6);
        Pokemon p;
        switch (tipo) {
            case FOGO: p = new Charmander("Charmander", vida, ataque, defesa); break;
            case AGUA: p = new Squirtle("Squirtle", vida, ataque, defesa); break;
            default: p = new Bulbassaur("Bulbasaur", vida, ataque, defesa); break;
        }
        // Nivel aleatorio entre o nivel padrao e o nivel padrao + variacao maxima (ex.: 10 a 15).
        int nivel = NIVEL_PADRAO + (int) (Math.random() * (VARIACAO_MAX_NIVEL_SELVAGEM + 1));
        p.definirNivel(nivel);
        return p;
    }
}

// =========================================================================
// TELAS
// =========================================================================
