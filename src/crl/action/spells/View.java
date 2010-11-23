package crl.action.spells;

import sz.util.Position;
import sz.util.Util;
import crl.feature.Feature;
import crl.level.Level;
import crl.monster.Monster;
import crl.monster.MonsterFactory;
import crl.player.Player;
import crl.ui.UserInterface;

public class View extends Spell{
	public String getID(){
		return "View";
	}
	
	public int getManaCost() {
		return 40;
	}
	
	public void execute(){
		super.execute();
		Level aLevel = performer.getLevel();
		aLevel.addMessage("VIEDA!");
		Position runner = new Position(0,0);
		for (int i = 0; i < 200; i++){
			runner.x = Util.rand(1,aLevel.getWidth()-1);
			runner.y = Util.rand(1,aLevel.getHeight()-1);
			aLevel.setSeen(runner.x, runner.y);
		}
		aLevel.addMessage("You feel you know more of your surroundings!");
	}

	public String getSFX(){
		return "wav/clockbel.wav";
	}
	public double getTimeCostModifier() {
		return 4;
	}
}