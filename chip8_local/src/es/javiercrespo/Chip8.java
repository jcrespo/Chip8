/*
 * Another Chip8 emulator - For educational purposes
 * 2018 - Javier Crespo
 * 
 * https://en.wikipedia.org/wiki/CHIP-8 // Description
 * http://devernay.free.fr/hacks/chip8/C8TECH10.HTM // Technical Reference
 * 
 * Inicializar, 	Obtener Opcode, Procesar, Tareas de Control
 * 
 */
package es.javiercrespo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;



public class Chip8 {
	
	private static Cpu cpu;
	private static Display display;
	private static Sprite sprites;
	

	public static void main(String[] args) throws InterruptedException {

		cpu 	= new Cpu();
		display = new Display();
		sprites = new Sprite();
		
		init();
		loadRom();
		boolean end = false;

		while (!end) {
			int opcode = (int) (((cpu.memory[cpu.pc] & 0xFF) << 8) + (cpu.memory[cpu.pc+1] & 0xFF));
			short nnn 	= (short) (opcode & 0xFFF);
			short kk 	= (short) (opcode & 0xFF);
			short n 	= (short) (opcode & 0xF);
			short x 	= (short) (opcode >> 8 & 0xF);
			short y 	= (short) (opcode >> 4 & 0xF);
			short msb	= (short) (opcode >> 12 & 0xF);
			
			System.out.println("opcode: " + String.format("0x%04X",opcode));
			System.out.println("nnn: " + String.format("%03X",nnn));
			System.out.println("kk: " + String.format("%02X",kk));
			System.out.println("n: " + String.format("%01X",n));
			System.out.println("x: " + String.format("%01X",x));
			System.out.println("y: " + String.format("%01X",y));
			System.out.println("msb: " + String.format("%01X",msb));


			
			switch (msb) {
				case 0:
					if (n==0x0)
						if (opcode==0x00E0) {
							for (int i = 0; i< cpu.screen.length; i++){
								cpu.screen[i] = 0x0; //CLS
							}
							
						} else if (opcode==0X00EE) {
								cpu.pc = cpu.sp;
								cpu.sp = (byte) (cpu.sp - 0x01);
						}
					break;
					
				case 1: cpu.pc = (short) (nnn & 0x0FFF);
				break;
				
				case 2:
					if (cpu.sp < 15) {
						cpu.stack[cpu.sp++] = cpu.pc;
					}
					break;

				case 3: 
					if (cpu.v[x]  == kk) {
						cpu.pc = (short) (cpu.pc + (short)0x0002);
					}
					break;

				case 4: 
					if (cpu.v[x] == kk) {
						cpu.pc = (short) (cpu.pc + 2 & 0xFFF);
					}
					break;

				case 5: 
					if (cpu.v[x] == cpu.v[y]) {
						cpu.pc = (short) (cpu.pc + 2 & 0xFFF);
					}
					break;

				case 6: 
					
					cpu.v[x] = (short) (kk & 0xFF);
					break;

				case 7: cpu.v[x] = (short) ((cpu.v[x] + kk) & 0xFF); break;
				
				case 8: 
					switch (n) {
						case 0: cpu.v[x] = cpu.v[y]; break;
						
						case 1: cpu.v[x] = (short) (cpu.v[x] | cpu.v[y]); cpu.pc = nnn; break;
						
						case 2: cpu.v[x] = (short) (cpu.v[x] & cpu.v[y]); break;
						
						case 3: cpu.v[x] = (short) (cpu.v[x] ^ cpu.v[y]); break;
						
						case 4: 
								cpu.v[0xF] = (short) ((cpu.v[x] > cpu.v[x] + cpu.v[y]) ? 1 : 0); // Carry
								cpu.v[x] += cpu.v[y];
							break;
							
						case 5:
								cpu.v[0xF] = (short) ((cpu.v[x] > cpu.v[y]) ? 1 : 0); // Carry
								cpu.v[x] -= cpu.v[y];
							break;
							
						case 6: 
								cpu.v[0xF] = (short) (cpu.v[x] & 0x01); // Carry
								cpu.v[x] >>= 1;
								break;
								
						case 7: 
								cpu.v[0xF] = (short) ((cpu.v[y] > cpu.v[x]) ? 1 : 0); // Carry
								cpu.v[x] = (short) (cpu.v[y] - cpu.v[x]);
								break;
								
						case 0xE:
								cpu.v[0xF] = (short) (((cpu.v[x] & 0x80) != 0) ? 1 : 0);
								cpu.v[x] <<= 1;
								break;
					}
					break;
				
				case 9: 
					if (cpu.v[x] != cpu.v[y]) {
						cpu.pc = (short) (cpu.pc + 2 & 0xFFF);
					}
					break;
				
				case 0xA: cpu.i = nnn; break;
				
				case 0xB: cpu.pc = (short) ((cpu.v[0] + nnn & 0XFFF)); break;
				
				case 0xC:
					cpu.v[x] = (short) ((short) cpu.rnd.nextInt() & kk); break; //Random value
				
				case 0xD: 
					//DRW
					cpu.v[15] = 0;
					for (int j=0; j<n; j++) {
						short sprite = cpu.memory[cpu.i + j];
						for (int i=0; i<8; i++) {
							int px = (cpu.v[x] + i) & 63;
							int py = (cpu.v[y] + j) & 31;
							int pos = ((64 * py + px) & 0xFF);

							boolean isActive = (sprite & (1 << (7-i))) != 0;
							if (isActive & (cpu.screen[pos] == 1)) cpu.v[15] = 1;
							cpu.screen[pos] ^= (short) (isActive ? 1 : 0);
						}
					}
					
					break;
				
				case 0xE:
						
					switch (kk) {
						case 0x9E: break;
						case 0xA1: break;
					}
					break;

				case 0xF:
					switch(kk) {
					case 0x07: cpu.v[x] = (short) (cpu.dt & 0xFF); break;
					
					case 0x0A: break;
					
					case 0x15: cpu.dt = cpu.v[x]; break;
					
					case 0x18: cpu.st = cpu.v[x]; break;
					
					case 0x1E: cpu.i += cpu.v[x]; break;
					
					case 0x29: cpu.i = (short) (0x50 + (cpu.v[x] & 0xF) * 5); break; //Draw Sprites
					
					case 0x33:
						cpu.memory[cpu.i + 2] = (short) (cpu.v[x] % 10); 
						cpu.memory[cpu.i + 1] = (short) (cpu.v[x] / 10 % 10);
						cpu.memory[cpu.i] = (short) (cpu.v[x] / 100);
						break;
					
					case 0x55:
						for (int reg=0; reg <=x; reg++) {
							cpu.memory[cpu.i + reg] = cpu.v[reg]; 
						}
						break;
						
					case 0x65:
						for (int reg=0; reg <=x; reg++) {
							cpu.v[reg] = cpu.memory[cpu.i + reg];
						}
						break;
					}
					break;
			}
		
			
			//Display
			display.paint(display.getGraphics(), cpu.screen);

			//Increment program counter
			//end = true;
			//cpu.pc = (short) ((cpu.pc + 2) & 0xFFF);
			cpu.pc+=0x2;
			//if (cpu.pc >= 4096) cpu.pc = 0x200;
			
            Thread.sleep(1000 / 60);
            if (cpu.dt > 0) cpu.dt--;
            if (cpu.st > 0) cpu.st--;
		}

	}
	
	private static void init() {

		cpu.sp = cpu.i = cpu.dt = cpu.st = 0x00;
		cpu.pc = 0x200;
		
		for (int i=0; i<cpu.memory.length;i++) cpu.memory[i] = 0x00;
		for (int i=0; i<cpu.screen.length;i++) cpu.screen[i] = 0x00;
		for (int i=0; i<cpu.stack.length;i++) cpu.stack[i] = 0x00;
		for (int i=0; i<cpu.v.length;i++) cpu.v[i] = 0x00;
		
		//System.arraycopy(sprites.sprites, 0, cpu.memory, 0x50, 80);//Sprites

	}
	
	//Hardcoded load pong.rom in memory
	private static void loadRom() {
		
		File file = new File("roms/PONG");
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(file);
            byte[] data = IOUtils.toByteArray(fis);
            int currentOffset = 0x200;
            for (byte theByte : data) {
            	cpu.memory[currentOffset] = (byte) (theByte & 0xFF);
                currentOffset++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
        	try {
				fis.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
        }
		
	}
}
