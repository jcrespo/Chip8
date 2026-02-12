import java.util.Random;

/**
 * Represents the CPU and memory state of the CHIP-8 emulator.
 */
public class Cpu {

	/** Main memory (4KB) */
	private final short[] memory = new short[4096];

	/** Registers V0 through VF */
	private final short[] v = new short[16];

	/** Index register I */
	private short i;

	/** Delay Timer and Sound Timer registers */
	private short dt, st;

	/** Program Counter */
	private short pc;

	/** Stack for subroutine addresses */
	private final short[] stack = new short[16];

	/** Stack pointer */
	private short sp;

	/** Random number generator for opcode CXKK */
	private final Random rnd = new Random();

	/** Screen memory (64x32 pixels) */
	private final short[] screen = new short[2048];

	// Getters and Setters for fields accessed by ProcessingUnit

	public short[] getMemory() {
		return memory;
	}

	public short[] getV() {
		return v;
	}

	public short getI() {
		return i;
	}

	public void setI(short i) {
		this.i = i;
	}

	public short getDt() {
		return dt;
	}

	public void setDt(short dt) {
		this.dt = dt;
	}

	public short getSt() {
		return st;
	}

	public void setSt(short st) {
		this.st = st;
	}

	public short getPc() {
		return pc;
	}

	public void setPc(short pc) {
		this.pc = pc;
	}

	public short[] getStack() {
		return stack;
	}

	public short getSp() {
		return sp;
	}

	public void setSp(short sp) {
		this.sp = sp;
	}

	public Random getRnd() {
		return rnd;
	}

	public short[] getScreen() {
		return screen;
	}

	/**
	 * Dumps the current memory state to the log (using JCL or similar).
	 * Note: Per user rules, avoiding System.out.println.
	 */
	public void dumpMemory() {
		// Implementation for logging would go here if a logger was defined.
		// For now, keeping it commented or using a future-proof placeholder.
	}
}
