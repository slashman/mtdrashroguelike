package crl.action.spells;

import crl.action.Action;
import crl.actor.Actor;
import crl.item.Item;
import crl.item.ItemDefinition;
import crl.player.Player;

public class Protection extends Spell{
	public int getManaCost() {
		return 15;
	}
	
	public String getID(){
		return "Protection";
	}
	
	public void execute(){
		super.execute();
		Player player = (Player)performer;
		player.getLevel().addMessage("SANCTO!");
		player.setCounter("PROTECTION", 25);
	}
	
	public double getTimeCostModifier() {
		return 2;
	}
	
	
}