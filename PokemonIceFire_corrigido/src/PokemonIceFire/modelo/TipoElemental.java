package PokemonIceFire.modelo;

import java.awt.Color;

public enum TipoElemental {
    FOGO, AGUA, PLANTA;

    public double vantagemSobre(TipoElemental outro) {
        if (this == FOGO && outro == PLANTA) return 1.5;
        if (this == PLANTA && outro == AGUA) return 1.5;
        if (this == AGUA && outro == FOGO) return 1.5;
        if (this == PLANTA && outro == FOGO) return 0.67;
        if (this == AGUA && outro == PLANTA) return 0.67;
        if (this == FOGO && outro == AGUA) return 0.67;
        return 1.0;
    }

    public Color corPrincipal() {
        switch (this) {
            case FOGO: return new Color(230, 92, 46);
            case AGUA: return new Color(58, 130, 214);
            case PLANTA: return new Color(87, 168, 74);
            default: return Color.GRAY;
        }
    }

    public String nomeExibicao() {
        switch (this) {
            case FOGO: return "Fogo";
            case AGUA: return "Água";
            case PLANTA: return "Planta";
            default: return "?";
        }
    }
}
