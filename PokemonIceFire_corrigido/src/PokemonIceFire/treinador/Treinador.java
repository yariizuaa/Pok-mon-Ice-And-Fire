package PokemonIceFire.treinador;

import PokemonIceFire.modelo.Pokemon;
import PokemonIceFire.item.TipoPocao;
import PokemonIceFire.item.TipoRevive;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.EnumMap;
import java.util.Collections;

public class Treinador {
    private static final int TAMANHO_MAX_EQUIPE = 6;
    private String nome;
    private List<Pokemon> equipe = new ArrayList<>();
    private Pokemon ativaEscolhida;
    private Map<TipoPocao, Integer> inventario = new EnumMap<>(TipoPocao.class);
    private Map<TipoRevive, Integer> inventarioRevive = new EnumMap<>(TipoRevive.class);

    public Treinador(String nome) { this.nome = nome; }
    public String getNome() { return nome; }
    public List<Pokemon> getEquipe() { return equipe; }
    public static int getTamanhoMaxEquipe() { return TAMANHO_MAX_EQUIPE; }

    public boolean adicionarNaEquipe(Pokemon c) {
        if (equipe.size() >= TAMANHO_MAX_EQUIPE) return false;
        equipe.add(c);
        return true;
    }

    /**
     * Retorna a criatura ativa no momento. Se o jogador escolheu manualmente
     * uma criatura (via aba Equipe/Bestiário) e ela ainda estiver viva, ela
     * tem prioridade; caso contrário cai para a primeira criatura viva da equipe.
     */
    public Pokemon getAtiva() {
        if (ativaEscolhida != null && ativaEscolhida.estaViva()) return ativaEscolhida;
        for (Pokemon c : equipe) if (c.estaViva()) return c;
        return null;
    }

    /**
     * Define manualmente qual criatura da equipe deve entrar em campo.
     * Só é permitido escolher uma criatura que pertença à equipe e esteja viva.
     */
    public boolean definirAtiva(Pokemon c) {
        if (c == null || !equipe.contains(c) || !c.estaViva()) return false;
        ativaEscolhida = c;
        return true;
    }

    /**
     * Maior nível entre TODAS as criaturas da equipe (inclusive as
     * desmaiadas) — usado para calibrar a dificuldade dos encontros
     * selvagens conforme o jogador avança (ver
     * {@link PokemonIceFire.modelo.FabricaPokemons#selvagemAleatorio}).
     * Retorna 0 se a equipe estiver vazia.
     */
    public int getMaiorNivelDaEquipe() {
        int maior = 0;
        for (Pokemon c : equipe) {
            maior = Math.max(maior, c.getNivel());
        }
        return maior;
    }

    // ---------------------------------------------------------------------
    // Inventário de itens (poções)
    // ---------------------------------------------------------------------

    /** Adiciona {@code quantidade} unidade(s) de uma poção ao inventário (ex.: item encontrado após vencer uma batalha). */
    public void adicionarItem(TipoPocao tipo, int quantidade) {
        if (tipo == null || quantidade <= 0) return;
        inventario.merge(tipo, quantidade, Integer::sum);
    }

    /** Quantidade de unidades de uma poção que o treinador possui. */
    public int quantidadeDe(TipoPocao tipo) {
        return inventario.getOrDefault(tipo, 0);
    }

    /** Inventário completo (apenas leitura), na ordem declarada em {@link TipoPocao}. */
    public Map<TipoPocao, Integer> getInventario() {
        return Collections.unmodifiableMap(inventario);
    }

    /**
     * Usa uma unidade da poção informada para curar o alvo, desde que o
     * treinador possua a poção e o alvo esteja vivo. Retorna {@code true}
     * se a poção foi de fato consumida.
     */
    public boolean usarPocao(TipoPocao tipo, Pokemon alvo) {
        if (tipo == null || alvo == null || !alvo.estaViva()) return false;
        if (quantidadeDe(tipo) <= 0) return false;
        alvo.curar(tipo.getCura());
        inventario.merge(tipo, -1, Integer::sum);
        return true;
    }

    // ---------------------------------------------------------------------
    // Inventário de itens (reviveres) — únicos capazes de acordar uma
    // criatura desmaiada (vida zerada); poções normais não funcionam nela.
    // ---------------------------------------------------------------------

    /** Adiciona {@code quantidade} unidade(s) de um revive ao inventário (ex.: item encontrado após vencer uma batalha). */
    public void adicionarItemRevive(TipoRevive tipo, int quantidade) {
        if (tipo == null || quantidade <= 0) return;
        inventarioRevive.merge(tipo, quantidade, Integer::sum);
    }

    /** Quantidade de unidades de um revive que o treinador possui. */
    public int quantidadeDeRevive(TipoRevive tipo) {
        return inventarioRevive.getOrDefault(tipo, 0);
    }

    /** Inventário de reviveres (apenas leitura), na ordem declarada em {@link TipoRevive}. */
    public Map<TipoRevive, Integer> getInventarioRevive() {
        return Collections.unmodifiableMap(inventarioRevive);
    }

    /**
     * Usa uma unidade do revive informado para acordar o alvo desmaiado
     * (vida zerada), devolvendo a porcentagem de vida máxima definida pelo
     * tipo de revive. Retorna {@code true} se o revive foi de fato consumido.
     */
    public boolean usarRevive(TipoRevive tipo, Pokemon alvo) {
        if (tipo == null || alvo == null || alvo.estaViva()) return false;
        if (quantidadeDeRevive(tipo) <= 0) return false;
        int vidaDevolvida = (int) Math.round(alvo.getVidaMaxima() * (tipo.getPercentualVida() / 100.0));
        alvo.curar(vidaDevolvida);
        inventarioRevive.merge(tipo, -1, Integer::sum);
        return true;
    }
}
