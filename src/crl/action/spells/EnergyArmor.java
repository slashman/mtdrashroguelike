package crl.action.spells;

import sz.util.Position;
import crl.action.Action;
import crl.actor.Actor;
import crl.item.ItemFactory;
import crl.player.Player;

public class EnergyArmor extends Spell{
	
	public int getManaCost() {
		return 25;
	}

	public double getTimeCostModifier() {
		return 1;
	}

	public String getPromptPosition() {
		return "WHERE?";
	}

	public boolean needsPosition() {
		return true;
	}

	
	public String getID(){
		return "Energy Armor";
	}
	
	
	public void execute(){
		super.execute();
		Player player = (Player)performer;
		player.getLevel().addMessage("MATERIA SANCTO!");
		if (Position.distance(player.getPosition(), targetPosition)>8)
			player.getLevel().addMessage("Too Far.");
		else {
			player.getLevel().addItem(targetPosition, ItemFactory.getItemFactory().createItem("MAGIC_ARMOR"));
		}
	}
}