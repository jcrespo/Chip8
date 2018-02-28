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
	
	setSize(660,365);
    setResizable(false);
    setTitle("Chip8 Display");
	getContentPane().setBackground(Color.BLACK);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setVisible(true);
	
	}
    
    public void paint(Graphics g, short[] screen) {
    	
	    	super.paint(g);
	    	g.setColor(Color.WHITE);
	    	
	    	int i=0;
	    	for (int y=0; y<FIL; y++) {   
	 		   for (int x=0; x<COL; x++) {
	 		   if ((screen[i] & 0xFF)  == 1) g.fillRect((x*SCALE)+10, (y*SCALE)+34, SCALE, SCALE);
	 		   i++;
	 	   }
	    }    	
    }

}
