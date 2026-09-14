package PokemonIceFire.view;

import PokemonIceFire.modelo.Pokemon;
import javax.swing.*;
import java.awt.*;

public class BarraVida extends JPanel {
    private static final int DURACAO_TEXTO_DANO_MS = 1100;

    private Pokemon criatura;
    private double vidaExibida;      // valor de HP mostrado na barra, que se aproxima aos poucos do valor real
    private Integer ultimoDano;      // ultimo dano a ser exibido como texto flutuante (-N), ou null
    private long inicioAnimacaoDano;
    private Timer timer;

    public BarraVida(Pokemon criatura) {
        this.criatura = criatura;
        this.vidaExibida = criatura != null ? criatura.getVidaAtual() : 0;
        setOpaque(false);
        setPreferredSize(new Dimension(240, 68));
    }

    /** Define a criatura exibida sem animação (usado ao trocar de criatura ou iniciar uma nova batalha). */
    public void setCriatura(Pokemon c) {
        this.criatura = c;
        this.vidaExibida = c != null ? c.getVidaAtual() : 0;
        this.ultimoDano = null;
        if (timer != null) timer.stop();
        repaint();
    }

    /**
     * Anima a redução da barra de vida de forma gradual (em vez de instantânea)
     * até o valor atual da criatura, e exibe por instantes o quanto foi perdido
     * (ex.: "-10") ao lado da barra.
     */
    public void animarDano(int dano) {
        if (criatura == null || dano <= 0) return;
        ultimoDano = dano;
        inicioAnimacaoDano = System.currentTimeMillis();
        if (timer != null && timer.isRunning()) timer.stop();
        timer = new Timer(16, e -> {
            double alvo = criatura.getVidaAtual();
            double diferenca = vidaExibida - alvo;
            if (Math.abs(diferenca) > 0.5) {
                double passo = Math.max(0.6, Math.abs(diferenca) * 0.10);
                vidaExibida -= Math.signum(diferenca) * passo;
            } else {
                vidaExibida = alvo;
            }
            boolean vidaChegouAoAlvo = Math.abs(vidaExibida - alvo) < 0.5;
            boolean textoJaSumiu = System.currentTimeMillis() - inicioAnimacaoDano > DURACAO_TEXTO_DANO_MS;
            repaint();
            if (vidaChegouAoAlvo && textoJaSumiu) {
                vidaExibida = alvo;
                ultimoDano = null;
                timer.stop();
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (criatura == null) return;
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();
        UIUtil.pintarCartao(g, w, h, 16, new Color(255, 255, 255, 235), new Color(225, 232, 237));

        int pad = 12;
        g.setFont(new Font("SansSerif", Font.BOLD, 15));
        g.setColor(Constantes.PRETO);
        g.drawString(criatura.getNome() + "  Nv." + criatura.getNivel(), pad, 22);

        UIUtil.pintarChip(g, criatura.getTipo().nomeExibicao(),
                w - pad - chipLargura(g, criatura.getTipo().nomeExibicao()), 6,
                criatura.getTipo().corPrincipal(), Color.WHITE, new Font("SansSerif", Font.BOLD, 10));

        int barW = w - pad * 2, barH = 13, barY = 32;
        g.setColor(new Color(224, 228, 233));
        g.fillRoundRect(pad, barY, barW, barH, 10, 10);

        double pct = Math.max(0, Math.min(1, vidaExibida / criatura.getVidaMaxima()));
        int fillW = (int) (barW * pct);
        Color corBase = pct > 0.5 ? new Color(76, 187, 105) : (pct > 0.2 ? new Color(240, 173, 61) : new Color(220, 70, 70));
        if (fillW > 0) {
            GradientPaint grad = new GradientPaint(pad, barY, corBase.brighter(), pad, barY + barH, corBase);
            g.setPaint(grad);
            g.fillRoundRect(pad, barY, fillW, barH, 10, 10);
        }
        g.setColor(new Color(190, 195, 202, 160));
        g.setStroke(new BasicStroke(1f));
        g.drawRoundRect(pad, barY, barW, barH, 10, 10);

        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g.setColor(Constantes.MUTED);
        String hpTexto = criatura.getVidaAtual() + " / " + criatura.getVidaMaxima() + " HP";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(hpTexto, w - pad - fm.stringWidth(hpTexto), barY + barH + 15);

        if (ultimoDano != null) {
            desenharDanoFlutuante(g, pad, barY);
        }

        g.dispose();
    }

    /** Desenha o valor perdido (ex.: "-10") subindo e desaparecendo ao lado da barra de vida. */
    private void desenharDanoFlutuante(Graphics2D g, int pad, int barY) {
        long decorrido = System.currentTimeMillis() - inicioAnimacaoDano;
        float progresso = Math.min(1f, decorrido / (float) DURACAO_TEXTO_DANO_MS);
        int deslocY = (int) (progresso * 26);
        int alfa = (int) (255 * (1 - progresso));
        if (alfa <= 0) return;
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        String texto = "-" + ultimoDano;
        g.setColor(new Color(220, 60, 60, alfa));
        g.drawString(texto, pad + 4, barY - 6 - deslocY);
    }

    private int chipLargura(Graphics2D g, String texto) {
        Font antiga = g.getFont();
        g.setFont(new Font("SansSerif", Font.BOLD, 10));
        int largura = g.getFontMetrics().stringWidth(texto) + 16;
        g.setFont(antiga);
        return largura;
    }
}
