# CHIP-8 Emulator

Another Chip8 emulator - For educational purposes
2018 - Javier Crespo

## Description

CHIP-8 is an interpreted programming language, developed by Joseph Weisbecker on his COSMAC VIP and Telmac 1800 8-bit microcomputers in the mid-1970s. It was designed to allow video games to be more easily programmed for these computers, which had very limited memory and processing power. The language runs on a virtual machine, providing a simple yet powerful abstraction for graphics, sound, and input.

The architecture of a CHIP-8 system typically includes 4KB of memory, 16 8-bit registers (V0 to VF), a 16-bit address register (I), and two timers for delay and sound. The display is a monochrome grid of 64x32 pixels, and input is handled through a 16-key hexadecimal keypad. This minimalistic set of specifications makes it an ideal "first project" for developers interested in emulator development.

Programs for CHIP-8 are composed of two-byte opcodes that perform operations such as clearing the screen, jumping to addresses, performing arithmetic, and drawing sprites onto the display. Because it is interpreted, CHIP-8 code is platform-independent, meaning a game written for the COSMAC VIP can run on any system that has a CHIP-8 interpreter.

Over the decades, CHIP-8 has maintained a dedicated following in the hobbyist and retro-computing communities. Its simplicity allows developers to focus on the core concepts of emulation—such as fetch-decode-execute cycles, memory management, and hardware abstraction—without the overwhelming complexity of modern architectures.

## References

- [Wikipedia CHIP-8](https://en.wikipedia.org/wiki/CHIP-8) // Description
- [Cowgod's CHIP-8 Technical Reference](http://devernay.free.fr/hacks/chip8/C8TECH10.HTM) // Technical Reference
- [How to write an emulator CHIP-8 interpreter](http://www.multigesture.net/articles/how-to-write-an-emulator-chip-8-interpreter/)

## Usage

To run the emulator with a specific ROM:
```bash
javac *.java
java Chip8 path/to/rom
```
