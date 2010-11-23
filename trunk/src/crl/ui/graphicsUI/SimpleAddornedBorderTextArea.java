package crl.ui.graphicsUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class SimpleAddornedBorderTextArea extends SimpleAddornedBorderPanel{
	private JTextArea textArea; 
	
	public SimpleAddornedBorderTextArea(
			Color OUT_COLOR, Color IN_COLOR) {
		super(OUT_COLOR, IN_COLOR);
		textArea = new JTextArea();
		setLayout(new GridLayout(1,1));
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setFocusable(false);
		textArea.setEditable(false);
		textArea.setOpaque(false);
		//textArea.setVisible(false);
		setBorder(new EmptyBorder(32, 32,32,32));
		setOpaque(false);
		add(textArea);
	}
	public void setText(String text){
		textArea.setText(text);
	}
	
	public void setFont(Font font){
		if (textArea != null)
			textArea.setFont(font);
	}
	
	public void setForeground(Color fore){
		if (textArea != null) textArea.setForeground(fore);
	}
	public void setBackground(Color fore){
		if (textArea != null) textArea.setBackground(fore);
	}
	
}
