package crl.action.spells;

import sz.util.Util;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;

public class LifeDrain extends Spell{
	public String getID(){
		return "Lifedrain";
	}
	
	public String getPromptPosition() {
		return "WHO?";
	}

	public boolean needsPosition() {
		return true;
	}
	
	public int getManaCost() {
		return 0;
	}
	
	public void execute(){
		super.execute();
		Player aPlayer = (Player) performer;
		Level x = performer.getLevel();
		x.addMessage("SIMO POBRI!");
		Monster m = x.getMonsterAt(targetPosition);
		if (m == null){
			x.addMessage("Nothing happens.");
		} else {
			if (m.hasCounter("LIFEDRAINED")){
				x.addMessage("The "+m.getDescription()+" has no life energy");
			} else {
				if (Util.chance(75)){
					x.addMessage("You steal life energy from the "+m.getDescription()+"!");
					m.damageIntegrity(2);
					aPlayer.recoverMana(5);
					m.setCounter("LIFEDRAINED", 50);
				} else {
					x.addMessage("You couldnt steal the "+m.getDescription()+" energy!");
				}
			}
			
		}
		
	}

	public String getSFX(){
		return "wav/clockbel.wav";
	}
	public double getTimeCostModifier() {
		return 1;
	}
}