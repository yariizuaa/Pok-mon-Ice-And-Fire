package PokemonIceFire.modelo;

public abstract class Pokemon {
    /** Nível padrão de uma criatura recém-criada, caso ninguém defina outro (ver {@link #definirNivel}). */
    private static final int NIVEL_PADRAO = 5;
    private static final int XP_POR_NIVEL_ATUAL = 12; // XP necessario para subir de nivel = nivel atual * este valor (reduzido de 30 para 12: sobe de nivel bem mais rapido)
    private static final int GANHO_VIDA_POR_NIVEL = 8;
    private static final int GANHO_ATAQUE_POR_NIVEL = 2;
    private static final int GANHO_DEFESA_POR_NIVEL = 1;

    protected String nome;
    protected TipoElemental tipo;
    private int vidaMaxima;
    private int vidaAtual;
    protected int ataque;
    protected int defesa;
    private int nivel = NIVEL_PADRAO;
    private int xp;

    protected Pokemon(String nome, TipoElemental tipo, int vidaMaxima, int ataque, int defesa) {
        this.nome = nome;
        this.tipo = tipo;
        this.vidaMaxima = vidaMaxima;
        this.vidaAtual = vidaMaxima;
        this.ataque = ataque;
        this.defesa = defesa;
    }

    public String getNome() { return nome; }
    public TipoElemental getTipo() { return tipo; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getVidaAtual() { return vidaAtual; }
    public int getAtaque() { return ataque; }
    public int getDefesa() { return defesa; }
    public boolean estaViva() { return vidaAtual > 0; }
    public double percentualVida() { return (double) vidaAtual / vidaMaxima; }

    public void receberDano(int dano) { vidaAtual = Math.max(0, vidaAtual - dano); }
    public void curar(int quantidade) { vidaAtual = Math.min(vidaMaxima, vidaAtual + quantidade); }

    public int calcularDano(Pokemon alvo) {
        int base = Math.max(1, ataque - alvo.defesa / 2);
        double vantagem = tipo.vantagemSobre(alvo.tipo);
        double bonus = bonusEspecial(alvo);
        return Math.max(1, (int) Math.round(base * vantagem * bonus));
    }

    public int atacar(Pokemon alvo) {
        int dano = calcularDano(alvo);
        alvo.receberDano(dano);
        efeitoPosAtaque(alvo, dano);
        return dano;
    }

    protected abstract double bonusEspecial(Pokemon alvo);
    protected void efeitoPosAtaque(Pokemon alvo, int danoAplicado) { /* no-op por padrao */ }
    public abstract String descricaoHabilidade();

    // ---------------------------------------------------------------------
    // Nível e experiência (XP)
    // ---------------------------------------------------------------------

    public int getNivel() { return nivel; }
    public int getXp() { return xp; }
    /** Quanto de XP falta acumular para a criatura subir para o próximo nível. */
    public int getXpParaProximoNivel() { return nivel * XP_POR_NIVEL_ATUAL; }

    /**
     * Define o nível da criatura diretamente, sem passar por XP (usado pela
     * {@link FabricaPokemons} ao criar um pokémon inicial — nível padrão — ou
     * um pokémon selvagem — nível aleatório).
     */
    public void definirNivel(int nivel) {
        this.nivel = Math.max(1, nivel);
    }

    /**
     * Ajusta a vida atual diretamente para um valor específico (limitado
     * entre 0 e a vida máxima). Usado apenas ao restaurar uma jornada salva
     * (ver {@link PokemonIceFire.persistencia.Persistencia}) — durante uma
     * batalha normal, use {@link #receberDano} ou {@link #curar}.
     */
    public void definirVidaAtual(int vidaAtual) {
        this.vidaAtual = Math.max(0, Math.min(vidaMaxima, vidaAtual));
    }

    /**
     * Ajusta o XP acumulado diretamente para um valor específico, sem
     * disparar a lógica de subida de nível de {@link #ganharXp} (o nível já
     * é restaurado separadamente via {@link #definirNivel}). Usado apenas
     * ao restaurar uma jornada salva.
     */
    public void definirXp(int xp) {
        this.xp = Math.max(0, xp);
    }

    /**
     * Adiciona XP à criatura (ex.: recompensa por vencer uma batalha) e sobe
     * de nível automaticamente sempre que o XP acumulado atinge o necessário
     * — podendo subir mais de um nível de uma vez. Cada nível ganho aumenta
     * levemente ataque, defesa e vida máxima (a vida atual cresce junto, sem
     * "curar de graça" o restante do dano já sofrido).
     *
     * @return quantos níveis a criatura subiu com este ganho de XP
     */
    public int ganharXp(int quantidade) {
        if (quantidade <= 0) return 0;
        xp += quantidade;
        int niveisGanhos = 0;
        while (xp >= getXpParaProximoNivel()) {
            xp -= getXpParaProximoNivel();
            subirDeNivel();
            niveisGanhos++;
        }
        return niveisGanhos;
    }

    private void subirDeNivel() {
        nivel++;
        vidaMaxima += GANHO_VIDA_POR_NIVEL;
        vidaAtual += GANHO_VIDA_POR_NIVEL;
        ataque += GANHO_ATAQUE_POR_NIVEL;
        defesa += GANHO_DEFESA_POR_NIVEL;
    }
}
