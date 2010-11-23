package crl.action.spells;

import crl.action.Action;
import crl.actor.Actor;
import crl.item.Item;
import crl.item.ItemDefinition;
import crl.player.Player;

public class Cure extends Spell{
	public int getManaCost() {
		return 10;
	}
	
	public String getID(){
		return "Cure";
	}
	
	public void execute(){
		super.execute();
		Player player = (Player)performer;
		player.getLevel().addMessage("ALCORT!");
		if (player.getCounter("POISON") > 0){
			player.getLevel().addMessage("You feel better");
			player.setCounter("POISON", 0);
		} else {
			player.getLevel().addMessage("Nothing happens");
		}
	}
	
	public double getTimeCostModifier() {
		return 3;
	}
	
	
}