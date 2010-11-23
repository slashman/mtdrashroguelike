package crl.action.spells;

import crl.action.Action;
import crl.actor.Actor;
import crl.player.Player;

public class Invisibility extends Spell{
	
	public int getManaCost() {
		return 10;
	}

	public String getID(){
		return "Invisibility";
	}
	
	public void execute(){
		super.execute();
		Player player = (Player)performer;
		player.getLevel().addMessage("CONCILI LOR!");
		player.setInvisible(25);
	}
	
	public double getTimeCostModifier() {
		return 2;
	}
	
	
}