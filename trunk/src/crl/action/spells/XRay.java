package crl.action.spells;

import sz.util.Position;
import sz.util.Util;
import crl.feature.Feature;
import crl.level.Level;
import crl.monster.Monster;
import crl.monster.MonsterFactory;
import crl.player.Player;
import crl.ui.UserInterface;

public class XRay extends Spell{
	public String getID(){
		return "XRay";
	}
	
	public int getManaCost() {
		return 25;
	}
	
	public void execute(){
		super.execute();
		Player player = (Player)performer;
		player.getLevel().addMessage("VISIO ETER!");
		player.setCounter("XRAY",25);
	}

	public String getSFX(){
		return "wav/clockbel.wav";
	}
	public double getTimeCostModifier() {
		return 4;
	}
}