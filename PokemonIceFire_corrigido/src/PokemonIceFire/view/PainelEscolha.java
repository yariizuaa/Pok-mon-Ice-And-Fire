package PokemonIceFire.view;

import PokemonIceFire.modelo.TipoElemental;
import PokemonIceFire.modelo.FabricaPokemons;
import javax.swing.*;
import java.awt.*;

public class PainelEscolha extends JPanel {
    PainelEscolha(JanelaPrincipal janela) {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titulo = new JLabel("Escolha sua primeira criatura", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 26));
        titulo.setForeground(Constantes.PRETO);
        add(titulo, BorderLayout.NORTH);

        JPanel cartas = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 20));
        cartas.setOpaque(false);
        cartas.add(new CartaoPokemon(TipoElemental.FOGO, "Charmander", "Labareda: dano extra contra alvos enfraquecidos",
                () -> janela.iniciarJornada(FabricaPokemons.novaInicial(TipoElemental.FOGO))));
        cartas.add(new CartaoPokemon(TipoElemental.AGUA, "Squirtle", "Maré Cheia: dano extra com a vida acima de 50%",
                () -> janela.iniciarJornada(FabricaPokemons.novaInicial(TipoElemental.AGUA))));
        cartas.add(new CartaoPokemon(TipoElemental.PLANTA, "Bulbasaur", "Sugar Seiva: recupera vida a cada ataque",
                () -> janela.iniciarJornada(FabricaPokemons.novaInicial(TipoElemental.PLANTA))));
        add(cartas, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        UIUtil.pintarFundoTela(g2, getWidth(), getHeight());
        g2.dispose();
    }
}
