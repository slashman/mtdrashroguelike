package crl.ui.graphicsUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class AddornedBorderPanel extends JPanel {
	private Image UPRIGHT, UPLEFT, DOWNRIGHT, DOWNLEFT;
	private Color OUT_COLOR, IN_COLOR;
	private int borderWidth, borderHeight;
	//private static Color TRANSPARENT_BLUE = new Color(100,100,100,200);
	private static Color TRANSPARENT_BLUE = new Color(20,20,20,200);
	
	public AddornedBorderPanel(Image UPRIGHT, 
			Image UPLEFT, Image DOWNRIGHT, Image DOWNLEFT,
			Color OUT_COLOR, Color IN_COLOR,
			int borderWidth, int borderHeight) {
		this.UPRIGHT = UPRIGHT; 
		this.UPLEFT = UPLEFT;  
		this.DOWNRIGHT = DOWNRIGHT;
		this.DOWNLEFT = DOWNLEFT;
		this.OUT_COLOR = OUT_COLOR; 
		this.IN_COLOR = IN_COLOR;
		this.borderHeight = borderHeight;
		this.borderWidth = borderWidth;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(TRANSPARENT_BLUE);
		g.fillRect(6,6,getWidth()-14,getHeight()-14);
		g.setColor(OUT_COLOR);
		g.drawRect(6,6,getWidth()-14,getHeight()-14);
		g.setColor(IN_COLOR);
		g.drawRect(8,8,getWidth()-18,getHeight()-18);
		g.drawImage(UPLEFT, 0,0, this);
		g.drawImage(UPRIGHT, getWidth()-borderWidth,0, this);
		g.drawImage(DOWNLEFT, 0, getHeight()-borderHeight,this);
		g.drawImage(DOWNRIGHT, getWidth()-borderWidth, getHeight()-borderHeight,this);
		
		
		
	}

}
