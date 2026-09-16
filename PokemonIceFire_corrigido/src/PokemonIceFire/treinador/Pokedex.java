package PokemonIceFire.treinador;

import PokemonIceFire.modelo.Pokemon;
import java.util.List;
import java.util.ArrayList;

public class Pokedex {
    private List<String> registradas = new ArrayList<>();

    public boolean registrar(Pokemon c) {
        return registrarEspecie(c.getClass().getSimpleName());
    }

    /**
     * Registra uma espécie diretamente pelo nome, sem precisar de uma
     * instância de {@link Pokemon}. Usado ao restaurar uma jornada salva
     * (ver {@link PokemonIceFire.persistencia.Persistencia}), já que o
     * arquivo de save guarda apenas o nome da espécie, não o objeto inteiro.
     */
    public boolean registrarEspecie(String especie) {
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
