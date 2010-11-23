package crl.action.spells;

import sz.util.Util;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;

public class Sleep extends Spell{
	public String getID(){
		return "Sleep";
	}
	
	public String getPromptPosition() {
		return "WHERE?";
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
		x.addMessage("HYPNUS LORUM!");
		Monster m = x.getMonsterAt(targetPosition);
		if (m == null){
			x.addMessage("Nothing happens.");
		} else {
			if (Util.chance(80)){
				if (!m.isMagus()){
					x.addMessage("The "+m.getDescription()+" falls asleep!");
					m.setCounter("SLEEP", 3+aPlayer.getSoulPower()*4);
				} else {
					x.addMessage("The "+m.getDescription()+" resists the spell!");
				}
			} else {
				x.addMessage("The "+m.getDescription()+" didn't get asleep.");
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