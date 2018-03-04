package es.javiercrespo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends JPanel {
	
	private static final long serialVersionUID = -5606672764531745977L;
	private Graphics g;
	boolean drawFlag;
	private int SCALE = 10;
    private int FIL = 32;
    private int COL = 64;
    
    public Display() {
    	setBackground(Color.BLACK);
    	
	}
    
    

    
    public Dimension getPreferredSize() {
        return new Dimension(COL * SCALE, FIL * SCALE);
    }
    
    public void paintPixel(boolean white, int x, int y) {
        if (white) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.BLACK);
        }

        g.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);

    }
    
    public void paintPixel(int x, int y) {

    	g.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);

    }    
    
    
    public void paintFullScreen(short[] screen) {

    	super.paint(g);
    	int i=0;
    	
        for (int y = 0; y < FIL; y++) {
            for (int x = 0; x < COL; x++) {
            	if ((screen[i] & 0xFF) == 1) {
            		g.setColor(Color.WHITE);
	 		   	} else {
	 			  g.setColor(Color.BLACK);
	 		   	}
                
                paintPixel(x, y);
                i++;
            }
        }
        
    }    
    
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.g = g;
}
    
    
    
    public void paint(Graphics g, short[] screen) {
    	
	    	super.paint(g);
	    	
	    	
	    	int i=0;
	    	for (int y=0; y<FIL; y++) {   
	 		   for (int x=0; x<COL; x++) {
		 		   if ((screen[i] & 0xFF) == 1) {
		 			  g.setColor(Color.WHITE);
		 		   } else {
		 			  g.setColor(Color.BLACK);
		 		   }
		 		  g.fillRect((x*SCALE), (y*SCALE), SCALE, SCALE);
		 		  i++;
	 		   }
	    	}    	
    }
    

}
