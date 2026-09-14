package PokemonIceFire.modelo;

public class Bulbassaur extends Pokemon {
    public Bulbassaur(String nome, int vida, int ataque, int defesa) {
        super(nome, TipoElemental.PLANTA, vida, ataque, defesa);
    }
    protected double bonusEspecial(Pokemon alvo) { return 1.0; }
    protected void efeitoPosAtaque(Pokemon alvo, int danoAplicado) {
        curar((int) Math.round(danoAplicado * 0.2));
    }
    public String descricaoHabilidade() {
        return "Sugar Seiva: recupera 20% do dano causado como vida";
    }
}

// =========================================================================
// TREINADOR / BESTIARIO
// =========================================================================
