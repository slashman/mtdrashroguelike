package crl.action.spells;

import sz.util.Position;
import crl.action.Action;
import crl.actor.Actor;
import crl.item.ItemFactory;
import crl.monster.Monster;
import crl.player.Player;

public class Kill extends Spell{
	
	public String getPromptPosition() {
		return "WHERE?";
	}

	public boolean needsPosition() {
		return true;
	}

	public int getManaCost() {
		return 30;
	}
	
	public double getTimeCostModifier() {
		return 5;
	}
	

	public String getID(){
		return "Kill";
	}
	
	
	public void execute(){
		super.execute();
		Player player = (Player)performer;
		player.getLevel().addMessage("XAM CORPUS LORUM!!");
		if (Position.distance(player.getPosition(), targetPosition)>8)
			player.getLevel().addMessage("Too Far.");
		else {
			Monster m = player.getLevel().getMonsterAt(targetPosition);
			if (m != null)
				if (m.canBeVanished() && ! m.isMagus()){
					player.getLevel().addMessage("You take life away from the "+m.getDescription()+"!");
					m.die();
				} else {
					player.getLevel().addMessage("The "+m.getDescription()+" resists the spell!");
				}
		}
	}
}