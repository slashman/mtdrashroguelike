package crl.action.spells;

import sz.util.Util;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;

public class Charm extends Spell{
	public String getID(){
		return "Charm";
	}
	
	public String getPromptPosition() {
		return "WHO?";
	}

	public boolean needsPosition() {
		return true;
	}
	
	public int getManaCost() {
		return 20;
	}
	
	public void execute(){
		super.execute();
		Player aPlayer = (Player) performer;
		Level x = performer.getLevel();
		x.addMessage("MENI MANIPULA!");
		Monster m = x.getMonsterAt(targetPosition);
		if (m == null){
			x.addMessage("Nothing happens.");
		} else {
			if (Util.chance(75)){
				if (m.isMagus()){
					x.addMessage("The "+m.getDescription()+" resists the spell!");
				} else {
					x.addMessage("The "+m.getDescription()+" turns to help!");
					m.setCounter("CHARM", 5+aPlayer.getSoulPower()*5);
				}
			} else {
				x.addMessage("The "+m.getDescription()+" wasn't charmed.");
			}
			
		}
		
	}

	public String getSFX(){
		return "wav/clockbel.wav";
	}
	
	public double getTimeCostModifier() {
		return 3;
	}
}