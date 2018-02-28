package es.javiercrespo;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;

public class Display extends JFrame {
	
	int SCALE = 10;
    int FIL = 32;
    int COL = 64;
    
    public Display() {
	
	setSize(650,360);
    //setResizable(false);
    setTitle("Chip8 Display");
	getContentPane().setBackground(Color.BLACK);
	//getContentPane().setSize(640, 320);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setVisible(true);
	
	//Píxeles límite
	//screen[0][0] = true;
	//screen[0][63] = true;
	//screen[31][0] = true;
	//screen[31][63] = true;

	}
    
    public void paint(Graphics g, short[] screen) {
    	
	    	super.paint(g);
	    	g.setColor(Color.WHITE);
	    	
	    	int i=0;
	    	for (int y=0; y<COL; y++) {   
	 		   for (int x=0; x<FIL; x++) {
	 		   //y+4  x+32
	 		   if ((screen[i] & 0xFF)  == 1) g.fillRect((y*SCALE)+4, (x*SCALE)+32, SCALE, SCALE);
	 		   i++;
	 	   }
	 		   
	    }    	
    	
    }
    
    

}
