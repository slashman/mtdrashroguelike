package crl.action.spells;

import crl.action.Action;
import crl.actor.Actor;
import crl.player.Player;

public class GreatLight extends Spell{
	public int getManaCost() {
		return 15;
	}

	public String getID(){
		return "Great Light";
	}
	
	public double getTimeCostModifier() {
		return 1;
	}
	
	public void execute(){
		super.execute();
		Player player = (Player)performer;
		player.getLevel().addMessage("LORUM!");
		player.setCounter("INCREASE_SIGHT2", 200);
	}
	
	
}