package PokemonIceFire.persistencia;

import PokemonIceFire.item.TipoPocao;
import PokemonIceFire.item.TipoRevive;
import PokemonIceFire.modelo.FabricaPokemons;
import PokemonIceFire.modelo.Pokemon;
import PokemonIceFire.treinador.Pokedex;
import PokemonIceFire.treinador.Treinador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

/**
 * Salva e carrega o progresso do jogador em disco, para que a jornada possa
 * ser retomada da próxima vez que o jogo for aberto, em vez de precisar
 * recomeçar do zero.
 *
 * <p>O estado é gravado como texto simples no formato "chave=valor" (via
 * {@link Properties}), num arquivo dentro da pasta pessoal do usuário — assim
 * o save sobrevive independente de onde o programa é executado no disco.
 * O que é salvo: nome do treinador, cada criatura da equipe (espécie, nome,
 * vida máxima/atual, ataque, defesa, nível e XP), o inventário de poções e
 * reviveres, e as espécies já registradas no Bestiário.
 *
 * <p>Esta classe só lida com leitura/escrita de disco; ela não sabe nada
 * sobre telas ou fluxo de jogo — quem decide QUANDO salvar/carregar é a
 * {@link PokemonIceFire.view.JanelaPrincipal}.
 */
public final class Persistencia {
    private static final File ARQUIVO_SAVE = new File(
            System.getProperty("user.home"), ".pokemoniceandfire/save.properties");

    private Persistencia() { }

    /** Existe uma jornada salva esperando para ser retomada? */
    public static boolean existeJornadaSalva() {
        return ARQUIVO_SAVE.isFile();
    }

    /**
     * Salva o estado atual da jornada (treinador + bestiário) em disco,
     * sobrescrevendo qualquer save anterior. Se o treinador for {@code null}
     * (nenhuma jornada em andamento), não faz nada. Problemas de I/O (disco
     * cheio, sem permissão de escrita, etc.) são registrados no console e
     * ignorados — uma falha ao salvar não deveria travar o jogo.
     */
    public static void salvar(Treinador treinador, Pokedex bestiario) {
        if (treinador == null) return;

        Properties prop = new Properties();
        prop.setProperty("treinador.nome", treinador.getNome());

        List<Pokemon> equipe = treinador.getEquipe();
        prop.setProperty("equipe.tamanho", String.valueOf(equipe.size()));
        for (int i = 0; i < equipe.size(); i++) {
            Pokemon p = equipe.get(i);
            String prefixo = "equipe." + i + ".";
            prop.setProperty(prefixo + "especie", p.getClass().getSimpleName());
            prop.setProperty(prefixo + "nome", p.getNome());
            prop.setProperty(prefixo + "vidaMaxima", String.valueOf(p.getVidaMaxima()));
            prop.setProperty(prefixo + "vidaAtual", String.valueOf(p.getVidaAtual()));
            prop.setProperty(prefixo + "ataque", String.valueOf(p.getAtaque()));
            prop.setProperty(prefixo + "defesa", String.valueOf(p.getDefesa()));
            prop.setProperty(prefixo + "nivel", String.valueOf(p.getNivel()));
            prop.setProperty(prefixo + "xp", String.valueOf(p.getXp()));
        }

        for (TipoPocao tipo : TipoPocao.values()) {
            int qtd = treinador.quantidadeDe(tipo);
            if (qtd > 0) prop.setProperty("pocao." + tipo.name(), String.valueOf(qtd));
        }
        for (TipoRevive tipo : TipoRevive.values()) {
            int qtd = treinador.quantidadeDeRevive(tipo);
            if (qtd > 0) prop.setProperty("revive." + tipo.name(), String.valueOf(qtd));
        }

        if (bestiario != null) {
            prop.setProperty("bestiario", String.join(",", bestiario.getRegistradas()));
        }

        try {
            File pasta = ARQUIVO_SAVE.getParentFile();
            if (pasta != null) pasta.mkdirs();
            try (Writer w = new OutputStreamWriter(new FileOutputStream(ARQUIVO_SAVE), StandardCharsets.UTF_8)) {
                prop.store(w, "Poke Ice and Fire - jornada salva");
            }
        } catch (IOException e) {
            System.err.println("[Persistencia] Não foi possível salvar a jornada: " + e.getMessage());
        }
    }

    /**
     * Carrega a jornada salva em disco e reconstrói o treinador e o
     * bestiário a partir dela.
     *
     * @return o estado carregado, ou {@code null} se não houver save válido
     *         (nenhum arquivo, arquivo corrompido, ou dados inconsistentes) —
     *         nesse caso é como se não existisse jornada nenhuma para retomar.
     */
    public static EstadoSalvo carregar() {
        if (!existeJornadaSalva()) return null;

        Properties prop = new Properties();
        try (Reader r = new InputStreamReader(new FileInputStream(ARQUIVO_SAVE), StandardCharsets.UTF_8)) {
            prop.load(r);
        } catch (IOException e) {
            System.err.println("[Persistencia] Não foi possível ler a jornada salva: " + e.getMessage());
            return null;
        }

        try {
            String nomeTreinador = prop.getProperty("treinador.nome", "Treinador");
            Treinador treinador = new Treinador(nomeTreinador);

            int tamanhoEquipe = Integer.parseInt(prop.getProperty("equipe.tamanho", "0"));
            for (int i = 0; i < tamanhoEquipe; i++) {
                String prefixo = "equipe." + i + ".";
                String especie = prop.getProperty(prefixo + "especie");
                String nome = prop.getProperty(prefixo + "nome");
                int vidaMaxima = Integer.parseInt(prop.getProperty(prefixo + "vidaMaxima"));
                int vidaAtual = Integer.parseInt(prop.getProperty(prefixo + "vidaAtual"));
                int ataque = Integer.parseInt(prop.getProperty(prefixo + "ataque"));
                int defesa = Integer.parseInt(prop.getProperty(prefixo + "defesa"));
                int nivel = Integer.parseInt(prop.getProperty(prefixo + "nivel"));
                int xp = Integer.parseInt(prop.getProperty(prefixo + "xp"));

                Pokemon p = FabricaPokemons.criarPorEspecie(especie, nome, vidaMaxima, ataque, defesa);
                p.definirNivel(nivel);
                p.definirXp(xp);
                p.definirVidaAtual(vidaAtual);
                treinador.adicionarNaEquipe(p);
            }

            for (TipoPocao tipo : TipoPocao.values()) {
                String valor = prop.getProperty("pocao." + tipo.name());
                if (valor != null) treinador.adicionarItem(tipo, Integer.parseInt(valor));
            }
            for (TipoRevive tipo : TipoRevive.values()) {
                String valor = prop.getProperty("revive." + tipo.name());
                if (valor != null) treinador.adicionarItemRevive(tipo, Integer.parseInt(valor));
            }

            Pokedex bestiario = new Pokedex();
            String especiesSalvas = prop.getProperty("bestiario", "");
            if (!especiesSalvas.isEmpty()) {
                for (String especie : especiesSalvas.split(",")) {
                    bestiario.registrarEspecie(especie);
                }
            }

            return new EstadoSalvo(treinador, bestiario);
        } catch (RuntimeException e) {
            // Save corrompido ou de uma versao incompativel do jogo - melhor
            // ignorar e comecar do zero do que travar o jogo com uma excecao.
            System.err.println("[Persistencia] Jornada salva parece corrompida, ignorando: " + e.getMessage());
            return null;
        }
    }

    /**
     * Apaga a jornada salva, se houver. Chamado quando uma jornada chega ao
     * fim de verdade (derrota total) — não faz sentido continuar oferecendo
     * "retomar" uma jornada que já acabou.
     */
    public static void apagar() {
        if (ARQUIVO_SAVE.isFile()) ARQUIVO_SAVE.delete();
    }

    /** Par (treinador, bestiário) devolvido por {@link #carregar()}. */
    public static final class EstadoSalvo {
        public final Treinador treinador;
        public final Pokedex bestiario;

        private EstadoSalvo(Treinador treinador, Pokedex bestiario) {
            this.treinador = treinador;
            this.bestiario = bestiario;
        }
    }
}
