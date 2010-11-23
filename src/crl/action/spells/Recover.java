package crl.action.spells;

import crl.action.Action;
import crl.actor.Actor;
import crl.item.Item;
import crl.item.ItemDefinition;
import crl.player.Player;

public class Recover extends Spell{
	public int getManaCost() {
		return 15;
	}
	
	public String getID(){
		return "Recover";
	}
	
	public void execute(){
		super.execute();
		Player player = (Player)performer;
		player.getLevel().addMessage("You feel better");
		player.recoverIntegrityP(10+player.getSoulPower()*3);
		player.recoverEvade(10+player.getSoulPower()*3);
	}
	
	public double getTimeCostModifier() {
		return 2;
	}
	
	
}