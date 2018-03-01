package es.javiercrespo;

import java.util.Random;

public class Cpu {
	
	public short [] memory = new short [4096]; // Main memory
	public short [] v = new short[16]; //Registers v
	public short i; //I register
	public short dt, st; //Delay Timer & Sound Timer registers
	public short pc; //Program Counter
	public short [] stack = new short[16]; //Stack
	public short sp; //Stack pointer (value to save in stack array)
	public Random rnd = new Random();
	public short[] screen = new short[2048]; // Screen memory



	public void dumpMemory() {
		for (int i=0; i<memory.length;i++) {
			System.out.println(String.format("%03X",i) + " : " + String.format("%08X",memory[i]));
		}
	}
}
