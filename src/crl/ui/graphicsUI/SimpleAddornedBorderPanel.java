package crl.ui.graphicsUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class SimpleAddornedBorderPanel extends JPanel {
	private Color OUT_COLOR, IN_COLOR;
	//private static Color TRANSPARENT_BLUE = new Color(100,100,100,200);
	private static Color TRANSPARENT_BLUE = new Color(20,20,20,200);
	
	public SimpleAddornedBorderPanel(
			Color OUT_COLOR, Color IN_COLOR) {
		this.OUT_COLOR = OUT_COLOR; 
		this.IN_COLOR = IN_COLOR;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(TRANSPARENT_BLUE);
		g.fillRect(6,6,getWidth()-14,getHeight()-14);
		g.setColor(OUT_COLOR);
		g.drawRect(6,6,getWidth()-14,getHeight()-14);
		g.setColor(IN_COLOR);
		g.drawRect(8,8,getWidth()-18,getHeight()-18);
	}

}
