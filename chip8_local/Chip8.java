/*
 * Another Chip8 emulator - For educational purposes
 * 2018 - Javier Crespo
 * 
 * https://en.wikipedia.org/wiki/CHIP-8 // Description
 * http://devernay.free.fr/hacks/chip8/C8TECH10.HTM // Technical Reference
 * http://www.multigesture.net/articles/how-to-write-an-emulator-chip-8-interpreter/
 * 
 */
//package es.javiercrespo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JFrame;

//import org.apache.commons.io.IOUtils;



public class Chip8 {
	
	private static Cpu cpu;
	private static Display display;
	private static Keyboard keyboard;
	private static ProcesingUnit pu;
	private static Sprite sprites;

	public static void main(String[] args) throws InterruptedException {

		cpu 		= new Cpu();
		display 	= new Display();
		display.setScreen(cpu.screen);
		keyboard 	= new Keyboard();
		pu			= new ProcesingUnit(cpu, keyboard, display);
		sprites		= new Sprite();
		
		
		init();
		loadRom();
		//cpu.dumpMemory();
		prepareGUI();
		
		boolean end = false;

		
		while (!end) {
			pu.run();
			//end = true;
		}
		
		

	}
	
	private static void init() {

		cpu.sp = cpu.i = cpu.dt = cpu.st = 0x00;
		cpu.pc = 0x200;
		
		for (int i=0; i<cpu.memory.length;i++) cpu.memory[i] = 0x00;
		for (int i=0; i<cpu.screen.length;i++) cpu.screen[i] = 0x00;
		for (int i=0; i<cpu.stack.length;i++) cpu.stack[i] = 0x00;
		for (int i=0; i<cpu.v.length;i++) cpu.v[i] = 0x00;

		System.arraycopy(sprites.sprites, 0, cpu.memory, 0x50, sprites.sprites.length);//Sprites
		display.addKeyListener(keyboard);

	}
	

	private static void loadRom() {
		
		File file = new File("roms/INVADERS");
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(file);
            //byte[] data = IOUtils.toByteArray(fis);
			byte[] data = fis.readAllBytes();
            int currentOffset = 0x200;
            for (byte theByte : data) {
            	cpu.memory[currentOffset] = (short) (theByte & 0xFF);
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
	
	
	private static void prepareGUI(){
        JFrame f = new JFrame("CHIP-8 emulator");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(display);
        f.pack();
        f.setResizable(false);
        f.setVisible(true);
        f.addKeyListener(keyboard);

	}

}
