package crl.action.spells;

import crl.action.Action;
import crl.actor.Actor;
import crl.level.Level;
import crl.player.Player;

public class TimeStop extends Spell{
	public String getID(){
		return "TimeStop";
	}
	
	public void execute(){
		super.execute();
		Player aPlayer = (Player) performer;
		Level x = performer.getLevel();
		
		x.addMessage("TYM NOX!");
		x.stopTime(5 + aPlayer.getSoulPower()*4);
	}

	public String getSFX(){
		return "wav/clockbel.wav";
	}
	
	public int getManaCost() {
		return 30;
	}
	
	public double getTimeCostModifier() {
		return 1;
	}
}