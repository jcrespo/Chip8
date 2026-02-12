import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Handles keyboard input by mapping physical keys to the CHIP-8 keypad.
 */
public class Keyboard extends KeyAdapter {

    private final boolean[] keys = new boolean[16];
    private int lastKeyPressed = -1;

    /**
     * Map of CHIP-8 keys (0x0 to 0xF) to physical KeyEvent codes.
     */
    public static final int[] KEY_CODE_MAP = {
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
        var key = mapKeycodeToChip8Key(e.getKeyCode());
        if (key != -1) {
            keys[key] = true;
            lastKeyPressed = key;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        var key = mapKeycodeToChip8Key(e.getKeyCode());
        if (key != -1) {
            keys[key] = false;
        }
    }

    /**
     * Maps a physical keycode to a CHIP-8 key index (0-15).
     *
     * @param keycode The physical keycode from KeyEvent.
     * @return The CHIP-8 key index, or -1 if not mapped.
     */
    public int mapKeycodeToChip8Key(int keycode) {
        for (var i = 0; i < KEY_CODE_MAP.length; i++) {
            if (KEY_CODE_MAP[i] == keycode) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if a specific CHIP-8 key is currently pressed.
     *
     * @param key The CHIP-8 key index (0-15).
     * @return True if the key is down, false otherwise.
     */
    public boolean isKeyDown(int key) {
        if (key >= 0 && key < 16) {
            return keys[key];
        }
        return false;
    }

    /**
     * Returns the index of the last key pressed and resets the state.
     *
     * @return The CHIP-8 key index, or -1 if no key was pressed since last call.
     */
    public int getLastKeyPressed() {
        var key = lastKeyPressed;
        lastKeyPressed = -1;
        return key;
    }
}
