package crl.action.spells;

import crl.action.Action;
import crl.actor.Actor;
import crl.player.Player;

public class Light extends Spell{
	public int getManaCost() {
		return 10;
	}
	
	public double getTimeCostModifier() {
		return 1;
	}

	public String getID(){
		return "Light";
	}
	
	public void execute(){
		super.execute();
		Player player = (Player)performer;
		player.getLevel().addMessage("LORUM!");
		player.setCounter("INCREASE_SIGHT", 100);
	}
	
	
}