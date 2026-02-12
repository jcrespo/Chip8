import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.JFrame;

/**
 * Main entry point for the CHIP-8 emulator.
 */
public class Chip8 {

	private static Cpu cpu;
	private static Display display;
	private static Keyboard keyboard;
	private static ProcessingUnit pu;
	private static Sprite sprites;

	public static void main(String[] args) {
		cpu = new Cpu();
		display = new Display();
		display.setScreen(cpu.getScreen());
		keyboard = new Keyboard();
		pu = new ProcessingUnit(cpu, keyboard, display);
		sprites = new Sprite();

		init();

		var romPath = "roms/INVADERS";
		if (args.length > 0) {
			romPath = args[0];
		} else {
			System.out.println("No ROM specified. Loading default: roms/INVADERS");
			System.out.println("Usage: java Chip8 [path_to_rom]");
		}

		loadRom(romPath);
		prepareGUI();

		while (true) {
			pu.run();
		}
	}

	private static void init() {
		cpu.setSp((short) 0);
		cpu.setI((short) 0);
		cpu.setDt((short) 0);
		cpu.setSt((short) 0);
		cpu.setPc((short) 0x200);

		var memory = cpu.getMemory();
		var screen = cpu.getScreen();
		var stack = cpu.getStack();
		var registers = cpu.getV();

		for (var i = 0; i < memory.length; i++)
			memory[i] = 0x00;
		for (var i = 0; i < screen.length; i++)
			screen[i] = 0x00;
		for (var i = 0; i < stack.length; i++)
			stack[i] = 0x00;
		for (var i = 0; i < registers.length; i++)
			registers[i] = 0x00;

		System.arraycopy(sprites.data(), 0, memory, 0x50, sprites.data().length);
		display.addKeyListener(keyboard);
	}

	private static void loadRom(String romPathString) {
		var path = Path.of(romPathString);
		try {
			var data = Files.readAllBytes(path);
			var memory = cpu.getMemory();
			var currentOffset = 0x200;

			for (var b : data) {
				if (currentOffset >= memory.length)
					break;
				memory[currentOffset] = (short) (b & 0xFF);
				currentOffset++;
			}
			System.out.println("Successfully loaded ROM: " + romPathString);
		} catch (IOException e) {
			System.err.println("Error loading ROM: " + e.getMessage());
		}
	}

	private static void prepareGUI() {
		var frame = new JFrame("CHIP-8 emulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display.setFocusable(true);
		frame.add(display);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.addKeyListener(keyboard);
		display.requestFocusInWindow();
	}
}
