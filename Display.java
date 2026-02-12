import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Handles the graphical output for the CHIP-8 emulator.
 */
public class Display extends JPanel {

    private static final long serialVersionUID = -5606672764531745977L;
    private static final int SCALE = 10;
    private static final int FIL = 32;
    private static final int COL = 64;

    private short[] screen;

    /** Flag indicating that the screen needs to be redrawn. */
    public boolean drawFlag;

    public Display() {
        setBackground(Color.BLACK);
    }

    /**
     * Sets the screen memory buffer.
     *
     * @param screen The screen buffer to use for rendering.
     */
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

        if (screen == null)
            return;

        for (var y = 0; y < FIL; y++) {
            for (var x = 0; x < COL; x++) {
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
