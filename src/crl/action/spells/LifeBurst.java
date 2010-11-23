package crl.action.spells;

import sz.util.Util;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;

public class LifeBurst extends Spell{
	public String getID(){
		return "Lifeburst";
	}
	
	public String getPromptPosition() {
		return "WHERE?";
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
		x.addMessage("HA BER!");
		Monster m = x.getMonsterAt(targetPosition);
		if (m == null){
			x.addMessage("Nothing happens.");
		} else {
			int damageMonster = (int)((double)aPlayer.getIntegrityPoints() / 3.0D);
			int damagePlayer = (int)((double)aPlayer.getIntegrityPoints() / 3.0D);
			x.addMessage("You fire a bolt of life energy!");
			m.damageIntegrity(damageMonster);
			aPlayer.selfDamage(Player.DAMAGE_LIFEBURST, damagePlayer);
		}
		
	}

	public String getSFX(){
		return "wav/clockbel.wav";
	}
	public double getTimeCostModifier() {
		return 1;
	}
}