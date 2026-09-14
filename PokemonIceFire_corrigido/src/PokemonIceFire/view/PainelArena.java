package PokemonIceFire.view;

import PokemonIceFire.modelo.TipoElemental;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class PainelArena extends JPanel {
    private static final int DURACAO_DANO_MS = 1000;

    private TipoElemental tipoEsquerda, tipoDireita;
    private boolean desmaioEsquerda, desmaioDireita;

    private Integer danoEsquerda, danoDireita;
    private long inicioDanoEsquerda, inicioDanoDireita;
    private Timer timerDano;

    PainelArena() {
        setOpaque(false);
        setPreferredSize(new Dimension(760, 260));
    }

    void configurar(TipoElemental esquerda, boolean desmaioEsq, TipoElemental direita, boolean desmaioDir) {
        this.tipoEsquerda = esquerda; this.desmaioEsquerda = desmaioEsq;
        this.tipoDireita = direita; this.desmaioDireita = desmaioDir;
        repaint();
    }

    /**
     * Mostra um número de dano (ex.: "-10") subindo e desaparecendo ao lado
     * da criatura indicada. ladoEsquerdo = true para a criatura do jogador,
     * false para a criatura selvagem.
     */
    void mostrarDano(boolean ladoEsquerdo, int dano) {
        if (dano <= 0) return;
        long agora = System.currentTimeMillis();
        if (ladoEsquerdo) {
            danoEsquerda = dano;
            inicioDanoEsquerda = agora;
        } else {
            danoDireita = dano;
            inicioDanoDireita = agora;
        }
        garantirTimerDano();
    }

    private void garantirTimerDano() {
        if (timerDano != null && timerDano.isRunning()) return;
        timerDano = new Timer(16, e -> {
            long agora = System.currentTimeMillis();
            if (danoEsquerda != null && agora - inicioDanoEsquerda > DURACAO_DANO_MS) danoEsquerda = null;
            if (danoDireita != null && agora - inicioDanoDireita > DURACAO_DANO_MS) danoDireita = null;
            repaint();
            if (danoEsquerda == null && danoDireita == null) ((Timer) e.getSource()).stop();
        });
        timerDano.start();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();
        int arco = 22;
        Shape recorte = new RoundRectangle2D.Float(0, 0, w, h, arco, arco);
        g.clip(recorte);

        // fundo em degrade escuro (preto -> vinho), sem nenhum tom de azul
        int gramaY = h - 74;
        GradientPaint fundo = new GradientPaint(0, 0, new Color(26, 26, 28), 0, gramaY, new Color(46, 20, 20));
        g.setPaint(fundo);
        g.fillRect(0, 0, w, gramaY);

        // grande pokebola translucida ao fundo, como emblema da arena
        int pbR = (int) (gramaY * 0.62);
        int pbCx = w / 2, pbCy = (int) (gramaY * 0.4);
        g.setColor(new Color(214, 45, 32, 26));
        g.fillArc(pbCx - pbR, pbCy - pbR, pbR * 2, pbR * 2, 0, 180);
        g.setColor(new Color(255, 255, 255, 16));
        g.fillArc(pbCx - pbR, pbCy - pbR, pbR * 2, pbR * 2, 180, 180);
        g.setColor(new Color(255, 255, 255, 26));
        g.setStroke(new BasicStroke(3f));
        g.drawOval(pbCx - pbR, pbCy - pbR, pbR * 2, pbR * 2);
        g.drawLine(pbCx - pbR, pbCy, pbCx + pbR, pbCy);
        g.setColor(new Color(255, 255, 255, 30));
        g.fillOval(pbCx - 14, pbCy - 14, 28, 28);
        g.drawOval(pbCx - 14, pbCy - 14, 28, 28);

        // piso da arena em estilo "ringue", com aneis concentricos
        g.setColor(new Color(32, 20, 20));
        g.fillRect(0, gramaY, w, h - gramaY);
        g.setStroke(new BasicStroke(2f));
        for (int i = 0; i < 3; i++) {
            int raioX = 90 + i * 90;
            int raioY = raioX / 4;
            g.setColor(i % 2 == 0 ? new Color(255, 255, 255, 30) : new Color(214, 45, 32, 45));
            g.drawOval(w / 2 - raioX, h - 6 - raioY, raioX * 2, raioY * 2);
        }
        g.setColor(new Color(214, 45, 32, 210));
        g.fillRect(0, gramaY, w, 4);
        g.setColor(new Color(255, 255, 255, 140));
        g.fillRect(0, gramaY + 4, w, 2);

        // plataformas ovais sob cada criatura
        if (tipoEsquerda != null) {
            desenharPlataforma(g, 150, gramaY + 8, 140, tipoEsquerda.corPrincipal());
        }
        if (tipoDireita != null) {
            desenharPlataforma(g, w - 150, gramaY - 78, 110, tipoDireita.corPrincipal());
        }

        // "VS" central quando ha duelo
        if (tipoEsquerda != null && tipoDireita != null) {
            g.setFont(new Font("Serif", Font.BOLD, 26));
            String vs = "VS";
            FontMetrics fm = g.getFontMetrics();
            int vx = (w - fm.stringWidth(vs)) / 2;
            int vy = 44;
            g.setColor(new Color(0, 0, 0, 40));
            g.drawString(vs, vx + 2, vy + 2);
            g.setColor(new Color(255, 255, 255, 220));
            g.drawString(vs, vx, vy);
        }

        if (tipoEsquerda != null) CriaturaSprite.desenhar(g, tipoEsquerda, 150, gramaY - 12, 130, desmaioEsquerda);
        if (tipoDireita != null) CriaturaSprite.desenhar(g, tipoDireita, w - 150, gramaY - 96, 110, desmaioDireita);

        if (danoEsquerda != null) desenharDanoFlutuante(g, 150, gramaY - 12, 130, danoEsquerda, inicioDanoEsquerda);
        if (danoDireita != null) desenharDanoFlutuante(g, w - 150, gramaY - 96, 110, danoDireita, inicioDanoDireita);

        // moldura sutil da arena
        g.setClip(null);
        g.setColor(new Color(255, 255, 255, 90));
        g.setStroke(new BasicStroke(2f));
        g.draw(new RoundRectangle2D.Float(1, 1, w - 2, h - 2, arco, arco));

        g.dispose();
    }

    /** Desenha o valor de dano (ex.: "-10") subindo e desaparecendo ao lado da criatura em (cx, cy). */
    private void desenharDanoFlutuante(Graphics2D g, int cx, int cy, int tamanhoSprite, int dano, long inicio) {
        long decorrido = System.currentTimeMillis() - inicio;
        float progresso = Math.min(1f, decorrido / (float) DURACAO_DANO_MS);
        int deslocY = (int) (progresso * 34);
        int alfa = (int) (255 * (1 - progresso));
        if (alfa <= 0) return;

        int r = tamanhoSprite / 2;
        int tx = cx + r - 10;
        int ty = cy - r - deslocY;

        String texto = "-" + dano;
        g.setFont(new Font("SansSerif", Font.BOLD, 22));

        g.setColor(new Color(0, 0, 0, (int) (alfa * 0.35)));
        g.drawString(texto, tx + 1, ty + 1);
        g.setColor(new Color(220, 40, 40, alfa));
        g.drawString(texto, tx, ty);
    }

    private void desenharPlataforma(Graphics2D g, int cx, int cy, int largura, Color corTipo) {
        int alturaOval = largura / 4;
        g.setColor(new Color(0, 0, 0, 30));
        g.fill(new Ellipse2D.Double(cx - largura / 2.0, cy - alturaOval / 2.0 + 4, largura, alturaOval));
        Color clara = new Color(
                Math.min(255, corTipo.getRed() + 70),
                Math.min(255, corTipo.getGreen() + 70),
                Math.min(255, corTipo.getBlue() + 70), 120);
        g.setColor(clara);
        g.fill(new Ellipse2D.Double(cx - largura / 2.0, cy - alturaOval / 2.0, largura, alturaOval));
        g.setColor(new Color(255, 255, 255, 150));
        g.setStroke(new BasicStroke(2f));
        g.draw(new Ellipse2D.Double(cx - largura / 2.0, cy - alturaOval / 2.0, largura, alturaOval));
    }
}
