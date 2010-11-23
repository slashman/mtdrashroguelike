package crl.ui.graphicsUI;

import java.awt.Color;
import java.util.Properties;

import javax.swing.JTextArea;

import sz.util.ScriptUtil;
import sz.util.Util;
import crl.game.PlayerGenerator;
import crl.player.Player;
import crl.ui.AppearanceFactory;
import crl.ui.Display;

public class GFXPlayerGenerator extends PlayerGenerator{
	public GFXPlayerGenerator(SwingSystemInterface si, Properties UIProperties){
		IMG_GENERATOR = UIProperties.getProperty("IMG_FRAME");
		this.si = si;
		txtClassDescription = new JTextArea();
		txtClassDescription.setOpaque(false);
		txtClassDescription.setForeground(Color.WHITE);
		txtClassDescription.setVisible(false);
		txtClassDescription.setBounds(345, 162, 302, 84);
		txtClassDescription.setLineWrap(true);
		txtClassDescription.setWrapStyleWord(true);
		txtClassDescription.setFocusable(false);
		txtClassDescription.setEditable(false);
		txtClassDescription.setFont(GFXDisplay.FNT_TEXT);
		
		si.add(txtClassDescription);
	}
	private SwingSystemInterface si;
	private JTextArea txtClassDescription;
	
	private String IMG_GENERATOR;
	
	public Player generatePlayer(){
		si.drawImage(IMG_GENERATOR);
		si.refresh();
		si.printAtPixel(69,118,"NAME:", Color.WHITE); 
		String name = si.input(143,118,GFXDisplay.COLOR_BOLD, 10);
		si.printAtPixel(69,133, "SEX: [M/F]", Color.WHITE);
		si.refresh();
		CharKey x = new CharKey(CharKey.NONE);
		while ( x.code != CharKey.M &&
				x.code != CharKey.m &&
				x.code != CharKey.F &&
				x.code != CharKey.f)
			x = si.inkey();
		int sex = 0;
		if (x.code == CharKey.M || x.code == CharKey.m)
			sex = Player.MALE;
		else
			sex = Player.FEMALE;
		si.printAtPixel(138,133, x.toString(), GFXDisplay.COLOR_BOLD);
		return getPlayer(name, sex);
	}
}