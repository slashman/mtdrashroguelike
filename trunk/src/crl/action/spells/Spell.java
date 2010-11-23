package crl.action.spells;

import crl.action.Action;
import crl.actor.Actor;
import crl.item.Item;
import crl.item.ItemDefinition;
import crl.player.Player;

public abstract class Spell extends Action{
	public abstract int getManaCost();
	public abstract double getTimeCostModifier();
	
	public void execute(){
		Player aPlayer = (Player) performer;
		aPlayer.reduceMana(getManaCost());
	}

	public final int getCost(){
		Player p = (Player) performer;
		return (int)(p.getCastCost() * getTimeCostModifier());
	}

	public final boolean canPerform(Actor a) {
		Player p = (Player) a;
		if (p.getMana() >= getManaCost()){
			return true;
		}
		invalidationMessage = "Your mana is too low";
		return false;
	}
}