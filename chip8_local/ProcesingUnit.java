import java.util.Arrays;

public class ProcesingUnit {

	private Cpu cpu;
	private Keyboard keyboard;
	private Display display;

	public ProcesingUnit(Cpu cpu, Keyboard keyboard, Display display) {
		this.cpu = cpu;
		this.keyboard = keyboard;
		this.display = display;
	}

	public void run() {
		int instructionsPerFrame = 15;
		
		for (int i = 0; i < instructionsPerFrame; i++) {
			cycle();
		}

		updateTimers();
	
		if (display.drawFlag) {
       		display.paint(display.getGraphics(), cpu.screen);
       		display.drawFlag = false;
    	}

		try {
			Thread.sleep(1000 / 60);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}



	public void cycle() {

		int opcode = (int) ((((cpu.memory[cpu.pc] & 0xFF) << 8) + (cpu.memory[cpu.pc + 1] & 0xFF)) & 0xFFFF);
		short nnn = (short) (opcode & 0x0FFF);
		short kk = (short) (opcode & 0xFF);
		short n = (short) (opcode & 0xF);
		short x = (short) (opcode >> 8 & 0xF);
		short y = (short) (opcode >> 4 & 0xF);
		short msb = (short) (opcode >> 12 & 0xF);

		cpu.pc += 0x2; // Increment program counter
		// if (cpu.pc >= 4096) cpu.pc = 0x200;
		
		/*System.out.println("opcode: " + String.format("0x%04X",opcode));
		System.out.println("nnn: " + String.format("%03X",nnn));
		System.out.println("kk: " + String.format("%02X",kk));
		System.out.println("n: " + String.format("%01X",n));
		System.out.println("x: " + String.format("%01X",x));
		System.out.println("y: " + String.format("%01X",y));
		System.out.println("msb: " + String.format("%01X",msb));*/
		

		switch (msb) {
			case 0:
				if (opcode == 0x00E0) {
					Arrays.fill(cpu.screen, (short) 0);
					display.drawFlag = true;

				} else if (opcode == 0X00EE) {
					if (cpu.sp > 0) cpu.sp--;
					cpu.pc = cpu.stack[cpu.sp];
				}
				break;

			case 1:
				cpu.pc = (short) nnn;
				break;

			case 2:
				// CALL nnn
				cpu.stack[cpu.sp] = cpu.pc;
				cpu.sp++;
				cpu.pc = (short) nnn;
				break;

			case 3:
				if (cpu.v[x] == kk) {
					cpu.pc = (short) (cpu.pc + 2);
				}
				break;

			case 4:
				if (cpu.v[x] != kk) {
					cpu.pc = (short) (cpu.pc + 2);
				}
				break;

			case 5:
				if (cpu.v[x] == cpu.v[y]) {
					cpu.pc = (short) (cpu.pc + 2);
				}
				break;

			case 6:
				cpu.v[x] = (short) (kk);
				break;

			case 7:
				cpu.v[x] = (short) (cpu.v[x] + kk);
				break;

			case 8:
				switch (n) {
					case 0:
						cpu.v[x] = cpu.v[y];
						break;

					case 1:
						cpu.v[x] = (short) (cpu.v[x] | cpu.v[y]);
						break;

					case 2:
						cpu.v[x] = (short) (cpu.v[x] & cpu.v[y]);
						break;

					case 3:
						cpu.v[x] = (short) (cpu.v[x] ^ cpu.v[y]);
						break;

					case 4:
						int sum = cpu.v[x] + cpu.v[y];

						if (sum > 0xFF) {
							cpu.v[0xF] = (byte) 0x1;
						} else {
							cpu.v[0xF] = (byte) 0x0;
						}

						cpu.v[x] = (short) (sum & 0xFF);

						break;

					case 5:

						if ((cpu.v[x] & 0xFF) > (cpu.v[y] & 0xFF)) {
							cpu.v[0xF] = (byte) 0x1;
						} else {
							cpu.v[0xF] = (byte) 0x0;
						}

						cpu.v[x] = (short) ((cpu.v[x] - cpu.v[y]) & 0xFF);

						break;

					case 6:
						cpu.v[0xF] = (short) (cpu.v[x] & 0x1); // Carry
						cpu.v[x] >>>= 1 & 0xFF; // & 0xFF? Not sure
						break;

					case 7:
						if (cpu.v[y] > cpu.v[x]) {
							cpu.v[0xF] = (byte) 0x1;
						} else {
							cpu.v[0xF] = (byte) 0x0;
						}

						cpu.v[x] = (short) ((cpu.v[y] - cpu.v[x]) & 0xFF);
						break;

					case 0xE:

						byte b = (byte) (cpu.v[x] >>> 7 & 0x01);
						cpu.v[0xF] = (short) (b & 0x01);
						cpu.v[x] = (short) ((cpu.v[x] << 1) & 0xFF);

						break;
				}
				break;

			case 9:
				if (cpu.v[x] != cpu.v[y]) {
					cpu.pc += 2;
				}
				break;

			case 0xA:
				cpu.i = nnn;
				break;

			case 0xB:
				cpu.pc = (short) ((cpu.v[0] & 0xFF) + nnn);
				break;

			case 0xC:
				cpu.v[x] = (short) ((cpu.rnd.nextInt(256) & kk) & 0xFF);
				break;
			/*
			case 0xD: // DRW

				cpu.v[15] = 0;
				for (int j = 0; j < n; j++) {
					short sprite = cpu.memory[cpu.i + j];

					for (int i = 0; i < 8; i++) {
						int px = (cpu.v[x] + i) & 63;
						int py = (cpu.v[y] + j) & 31;
						int pos = ((64 * py + px));

						boolean isActive = (sprite & (1 << (7 - i))) != 0;

						if (isActive & (cpu.screen[pos] == 1))
							cpu.v[15] = 1;

						cpu.screen[pos] ^= (short) (isActive ? 1 : 0);
					}
				}

				display.drawFlag = true;

				break;

			*/

			case 0xD: // DRW Vx, Vy, n
				int vx = cpu.v[x] & 0xFF; // Coordenada X inicial
				int vy = cpu.v[y] & 0xFF; // Coordenada Y inicial
				cpu.v[0xF] = 0;           // Reset del flag de colisión

				for (int row = 0; row < n; row++) {
					// Leemos el byte del sprite desde la memoria (8 píxeles horizontales)
					int spriteByte = cpu.memory[cpu.i + row] & 0xFF;

					for (int col = 0; col < 8; col++) {
						// Si el bit actual del sprite es 1, intentamos dibujar
						if ((spriteByte & (0x80 >> col)) != 0) {
							// Coordenadas con "wrapping" (envolventes) para no salir de pantalla
							int px = (vx + col) % 64;
							int py = (vy + row) % 32;
							int pos = px + (py * 64);

							// Colisión: si el píxel ya estaba encendido (1), ponemos VF = 1
							if (cpu.screen[pos] == 1) {
							cpu.v[0xF] = 1;
							}

							// XOR: Encender/Apagar el píxel
							cpu.screen[pos] ^= 1;
					}
				}
			}
			display.drawFlag = true;
			break;

			case 0xE:

				switch (kk) {
					case 0x9E:
						if (keyboard.isKeyDown(cpu.v[x])) {
							cpu.pc += 2;
						}
						break;

					case 0xA1:
						if (!keyboard.isKeyDown(cpu.v[x])) {
							cpu.pc += 2;
						}
						break;
				}
				break;

			case 0xF:
				switch (kk) {

					case 0x07:
						cpu.v[x] = cpu.dt;
						break;

					case 0x0A:
						int key = keyboard.getLastKeyPressed();

						if (key == -1) {
							cpu.pc -= 2;
						} else {
							cpu.v[x] = (short) key;
						}

						break;

					case 0x15:
						cpu.dt = cpu.v[x];
						break;

					case 0x18:
						cpu.st = cpu.v[x];
						break;

					case 0x1E:
						cpu.i = (short) (cpu.i + cpu.v[x]);
						break;

					case 0x29: // Draw Sprites
						cpu.i = (short) (0x50 + cpu.v[x] * 5);
						break;

					case 0x33:
						cpu.memory[cpu.i] = (short) (cpu.v[x] / 100);
						cpu.memory[cpu.i + 1] = (short) ((cpu.v[x] % 100) / 10);
						cpu.memory[cpu.i + 2] = (short) ((cpu.v[x] % 100) % 10);
						break;

					case 0x55:
						for (int reg = 0; reg <= x; reg++) {
							cpu.memory[cpu.i + reg] = cpu.v[reg];
						}
						break;

					case 0x65:
						for (int reg = 0; reg <= x; reg++) {
							cpu.v[reg] = cpu.memory[cpu.i + reg];
						}
						break;
				}
				break; //No sé si es correcto
		}
	}

	private void updateTimers() {
		if (cpu.dt > 0) {
			cpu.dt--;
		}
			
		if (cpu.st > 0) {
			if (cpu.st == 1) {
				//Toolkit.getDefaultToolkit().beep();
			}
			cpu.st--;
		}
	}

}
