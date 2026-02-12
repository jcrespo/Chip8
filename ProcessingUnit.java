import java.util.Arrays;

/**
 * Handles the instruction execution cycle for the CHIP-8 emulator.
 */
public class ProcessingUnit {

	private final Cpu cpu;
	private final Keyboard keyboard;
	private final Display display;

	/**
	 * Constructs a ProcessingUnit with the necessary components.
	 *
	 * @param cpu      The CPU state.
	 * @param keyboard The keyboard input handler.
	 * @param display  The display output handler.
	 */
	public ProcessingUnit(Cpu cpu, Keyboard keyboard, Display display) {
		this.cpu = cpu;
		this.keyboard = keyboard;
		this.display = display;
	}

	/**
	 * Runs a single frame of instructions (usually 60Hz).
	 */
	public void run() {
		var instructionsPerFrame = 15;

		for (var i = 0; i < instructionsPerFrame; i++) {
			cycle();
		}

		updateTimers();

		if (display.drawFlag) {
			display.repaint();
			display.drawFlag = false;
		}

		try {
			Thread.sleep(1000 / 60);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Executes a single CHIP-8 instruction cycle.
	 */
	public void cycle() {
		var memory = cpu.getMemory();
		var pc = cpu.getPc();

		int opcode = ((memory[pc] & 0xFF) << 8) | (memory[pc + 1] & 0xFF);
		short nnn = (short) (opcode & 0x0FFF);
		short kk = (short) (opcode & 0xFF);
		short n = (short) (opcode & 0xF);
		short x = (short) ((opcode >> 8) & 0xF);
		short y = (short) ((opcode >> 4) & 0xF);
		short msb = (short) ((opcode >> 12) & 0xF);

		cpu.setPc((short) (pc + 2));

		var registers = cpu.getV();

		switch (msb) {
			case 0 -> {
				if (opcode == 0x00E0) {
					Arrays.fill(cpu.getScreen(), (short) 0);
					display.drawFlag = true;
				} else if (opcode == 0x00EE) {
					if (cpu.getSp() > 0) {
						cpu.setSp((short) (cpu.getSp() - 1));
					}
					cpu.setPc(cpu.getStack()[cpu.getSp()]);
				}
			}
			case 1 -> cpu.setPc(nnn);
			case 2 -> {
				cpu.getStack()[cpu.getSp()] = cpu.getPc();
				cpu.setSp((short) (cpu.getSp() + 1));
				cpu.setPc(nnn);
			}
			case 3 -> {
				if (registers[x] == kk)
					cpu.setPc((short) (cpu.getPc() + 2));
			}
			case 4 -> {
				if (registers[x] != kk)
					cpu.setPc((short) (cpu.getPc() + 2));
			}
			case 5 -> {
				if (registers[x] == registers[y])
					cpu.setPc((short) (cpu.getPc() + 2));
			}
			case 6 -> registers[x] = kk;
			case 7 -> registers[x] = (short) ((registers[x] + kk) & 0xFF);
			case 8 -> executeArithmetic(x, y, n);
			case 9 -> {
				if (registers[x] != registers[y])
					cpu.setPc((short) (cpu.getPc() + 2));
			}
			case 0xA -> cpu.setI(nnn);
			case 0xB -> cpu.setPc((short) ((registers[0] & 0xFF) + nnn));
			case 0xC -> registers[x] = (short) ((cpu.getRnd().nextInt(256) & kk) & 0xFF);
			case 0xD -> draw(x, y, n);
			case 0xE -> executeInput(x, kk);
			case 0xF -> executeMisc(x, kk);
		}
	}

	private void executeArithmetic(short x, short y, short n) {
		var registers = cpu.getV();
		switch (n) {
			case 0 -> registers[x] = registers[y];
			case 1 -> registers[x] = (short) (registers[x] | registers[y]);
			case 2 -> registers[x] = (short) (registers[x] & registers[y]);
			case 3 -> registers[x] = (short) (registers[x] ^ registers[y]);
			case 4 -> {
				var sum = (registers[x] & 0xFF) + (registers[y] & 0xFF);
				registers[0xF] = (short) (sum > 0xFF ? 1 : 0);
				registers[x] = (short) (sum & 0xFF);
			}
			case 5 -> {
				registers[0xF] = (short) ((registers[x] & 0xFF) > (registers[y] & 0xFF) ? 1 : 0);
				registers[x] = (short) ((registers[x] - registers[y]) & 0xFF);
			}
			case 6 -> {
				registers[0xF] = (short) (registers[x] & 0x1);
				registers[x] = (short) ((registers[x] & 0xFF) >>> 1);
			}
			case 7 -> {
				registers[0xF] = (short) ((registers[y] & 0xFF) > (registers[x] & 0xFF) ? 1 : 0);
				registers[x] = (short) ((registers[y] - registers[x]) & 0xFF);
			}
			case 0xE -> {
				registers[0xF] = (short) (((registers[x] & 0xFF) >>> 7) & 0x01);
				registers[x] = (short) ((registers[x] << 1) & 0xFF);
			}
		}
	}

	private void draw(short x, short y, short n) {
		var registers = cpu.getV();
		var vx = registers[x] & 0xFF;
		var vy = registers[y] & 0xFF;
		var screen = cpu.getScreen();
		var memory = cpu.getMemory();
		var iReg = cpu.getI();

		registers[0xF] = 0;
		for (var row = 0; row < n; row++) {
			var spriteByte = memory[iReg + row] & 0xFF;
			for (var col = 0; col < 8; col++) {
				if ((spriteByte & (0x80 >> col)) != 0) {
					var px = (vx + col) % 64;
					var py = (vy + row) % 32;
					var pos = px + (py * 64);

					if (screen[pos] == 1) {
						registers[0xF] = 1;
					}
					screen[pos] ^= 1;
				}
			}
		}
		display.drawFlag = true;
	}

	private void executeInput(short x, short kk) {
		var registers = cpu.getV();
		switch (kk) {
			case 0x9E -> {
				if (keyboard.isKeyDown(registers[x]))
					cpu.setPc((short) (cpu.getPc() + 2));
			}
			case 0xA1 -> {
				if (!keyboard.isKeyDown(registers[x]))
					cpu.setPc((short) (cpu.getPc() + 2));
			}
		}
	}

	private void executeMisc(short x, short kk) {
		var registers = cpu.getV();
		var memory = cpu.getMemory();
		var iReg = cpu.getI();

		switch (kk) {
			case 0x07 -> registers[x] = cpu.getDt();
			case 0x0A -> {
				var key = keyboard.getLastKeyPressed();
				if (key == -1) {
					cpu.setPc((short) (cpu.getPc() - 2));
				} else {
					registers[x] = (short) key;
				}
			}
			case 0x15 -> cpu.setDt(registers[x]);
			case 0x18 -> cpu.setSt(registers[x]);
			case 0x1E -> cpu.setI((short) (iReg + registers[x]));
			case 0x29 -> cpu.setI((short) (0x50 + (registers[x] & 0x0F) * 5));
			case 0x33 -> {
				var val = registers[x] & 0xFF;
				memory[iReg] = (short) (val / 100);
				memory[iReg + 1] = (short) ((val % 100) / 10);
				memory[iReg + 2] = (short) (val % 10);
			}
			case 0x55 -> {
				for (var reg = 0; reg <= x; reg++) {
					memory[iReg + reg] = registers[reg];
				}
			}
			case 0x65 -> {
				for (var reg = 0; reg <= x; reg++) {
					registers[reg] = memory[iReg + reg];
				}
			}
		}
	}

	private void updateTimers() {
		if (cpu.getDt() > 0)
			cpu.setDt((short) (cpu.getDt() - 1));
		if (cpu.getSt() > 0)
			cpu.setSt((short) (cpu.getSt() - 1));
	}
}
