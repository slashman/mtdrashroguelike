package crl.action.spells;

import crl.player.Player;

public class MindMissile extends MissileSpell{
	public String getID(){
		return "MindMissile";
	}
	
	public String getSFX(){
		return "wav/fireball.wav";
	}

	public String getPromptPosition(){
		return "AIM";
	}
	
	public int getManaCost() {
		return 5;
	}
	
	public int getRange() {
		return 20;
	}
	
	public String getSelfTargettedMessage() {
		return "Nothing happens";
	}
	
	public String getSFXID() {
		return "SFX_MIND_MISSILE";
	}
	
	public String getShootMessage() {
		return "MENTAR!";
	}
	
	public String getSpellAttackDesc() {
		return "mental wave";
	}
	
	public int getSpellDamage() {
		Player aPlayer = (Player) performer;
		return 4 + 2 * aPlayer.getSoulPower();
	}
	
	public double getTimeCostModifier() {
		return 2.5d;
	}
	
	public int getHit() {
		return 80;
	}
}