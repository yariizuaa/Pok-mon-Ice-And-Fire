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

    public static Pokemon selvagemAleatorio(int maiorNivelDoJogador) {
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
        // Nivel base = media entre o nivel padrao e o nivel mais alto da equipe do
        // jogador: conforme o jogador evolui, os selvagens ficam mais fortes junto
        // (mas nao pulam direto para o nivel do jogador — a media suaviza a curva).
        int nivelBase = (NIVEL_PADRAO + Math.max(NIVEL_PADRAO, maiorNivelDoJogador)) / 2;
        // Variacao aleatoria em torno do nivel base (ex.: base 15 -> 15 a 20).
        int nivel = nivelBase + (int) (Math.random() * (VARIACAO_MAX_NIVEL_SELVAGEM + 1));
        p.definirNivel(nivel);
        return p;
    }
}

// =========================================================================
// TELAS
// =========================================================================
