package crl.action.spells;

import crl.action.Action;
import crl.actor.Actor;
import crl.item.Item;
import crl.item.ItemDefinition;
import crl.player.Player;

public class Quickness extends Spell{
	public int getManaCost() {
		return 10;
	}
	
	public String getID(){
		return "Quickness";
	}
	
	public void execute(){
		super.execute();
		Player player = (Player)performer;
		player.getLevel().addMessage("HURTIS HURTIS!");
		player.setCounter("QUICK", 25);
	}
	
	public double getTimeCostModifier() {
		return 4;
	}
	
	
}