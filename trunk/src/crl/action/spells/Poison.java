package crl.action.spells;

import sz.util.Util;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;

public class Poison extends Spell{
	public String getID(){
		return "Poison";
	}
	
	public String getPromptPosition() {
		return "WHO?";
	}

	public boolean needsPosition() {
		return true;
	}
	
	public int getManaCost() {
		return 5;
	}
	
	public void execute(){
		super.execute();
		Player aPlayer = (Player) performer;
		Level x = performer.getLevel();
		x.addMessage("BIO KALAM!");
		Monster m = x.getMonsterAt(targetPosition);
		if (m == null){
			x.addMessage("Nothing happens.");
		} else {
			if (Util.chance(80)){
				if (m.isMagus()){
					x.addMessage("The "+m.getDescription()+" resists the spell!");
				} else {
					x.addMessage("The "+m.getDescription()+" is poisoned!");
					m.setCounter("POISON", 3+aPlayer.getSoulPower()*7);
				}
			} else {
				x.addMessage("The "+m.getDescription()+" wasn't poisoned.");
			}
			
		}
		
	}

	public String getSFX(){
		return "wav/clockbel.wav";
	}
	public double getTimeCostModifier() {
		return 2;
	}
}