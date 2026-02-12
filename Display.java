//package es.javiercrespo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends JPanel {

    private static final long serialVersionUID = -5606672764531745977L;
    private int SCALE = 10;
    private int FIL = 32;
    private int COL = 64;
    private short[] screen;
    boolean drawFlag;

    public Display() {
        setBackground(Color.BLACK);
    }

    public void setScreen(short[] screen) {
        this.screen = screen;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COL * SCALE, FIL * SCALE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (screen == null) return;

        for (int y = 0; y < FIL; y++) {
            for (int x = 0; x < COL; x++) {
                if ((screen[x + (y * COL)] & 0xFF) == 1) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
            }
        }
    }
}
