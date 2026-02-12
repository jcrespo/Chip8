import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Keyboard extends KeyAdapter {

    private final boolean[] keys = new boolean[16];
    private int lastKeyPressed = -1;

    public static final int[] KeyCodeMap = {
            KeyEvent.VK_X, // Key 0
            KeyEvent.VK_1, // Key 1
            KeyEvent.VK_2, // Key 2
            KeyEvent.VK_3, // Key 3
            KeyEvent.VK_Q, // Key 4
            KeyEvent.VK_W, // Key 5
            KeyEvent.VK_E, // Key 6
            KeyEvent.VK_A, // Key 7
            KeyEvent.VK_S, // Key 8
            KeyEvent.VK_D, // Key 9
            KeyEvent.VK_Z, // Key A
            KeyEvent.VK_C, // Key B
            KeyEvent.VK_4, // Key C
            KeyEvent.VK_R, // Key D
            KeyEvent.VK_F, // Key E
            KeyEvent.VK_V, // Key F
    };

    @Override
    public void keyPressed(KeyEvent e) {
        int key = mapKeycodeToChip8Key(e.getKeyCode());
        if (key != -1) {
            keys[key] = true;
            lastKeyPressed = key;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = mapKeycodeToChip8Key(e.getKeyCode());
        if (key != -1) {
            keys[key] = false;
        }
    }

    public int mapKeycodeToChip8Key(int keycode) {
        for (int i = 0; i < KeyCodeMap.length; i++) {
            if (KeyCodeMap[i] == keycode) {
                return i;
            }
        }
        return -1;
    }

    public boolean isKeyDown(int key) {
        if (key >= 0 && key < 16) {
            return keys[key];
        }
        return false;
    }

    public int getLastKeyPressed() {
        int key = lastKeyPressed;
        lastKeyPressed = -1;
        return key;
    }
}
