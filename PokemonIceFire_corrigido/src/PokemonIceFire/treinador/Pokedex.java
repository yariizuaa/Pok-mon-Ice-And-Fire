package PokemonIceFire.treinador;

import PokemonIceFire.modelo.Pokemon;
import java.util.List;
import java.util.ArrayList;

public class Pokedex {
    private List<String> registradas = new ArrayList<>();

    public boolean registrar(Pokemon c) {
        String especie = c.getClass().getSimpleName();
        if (registradas.contains(especie)) return false;
        registradas.add(especie);
        return true;
    }
    public List<String> getRegistradas() { return registradas; }
    public int total() { return registradas.size(); }
}

// =========================================================================
// BATALHA / CAPTURA
// =========================================================================
