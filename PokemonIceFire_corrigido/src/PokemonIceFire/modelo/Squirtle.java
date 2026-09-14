package PokemonIceFire.modelo;

public class Squirtle extends Pokemon {
    public Squirtle(String nome, int vida, int ataque, int defesa) {
        super(nome, TipoElemental.AGUA, vida, ataque, defesa);
    }
    protected double bonusEspecial(Pokemon alvo) {
        return percentualVida() > 0.5 ? 1.15 : 1.0;
    }
    public String descricaoHabilidade() {
        return "Maré Cheia: +15% de dano enquanto a própria vida estiver acima de 50%";
    }
}
