package crl.action.spells;

import crl.player.Player;

public class FireMissile extends MissileSpell{
	public String getID(){
		return "FireMissile";
	}
	
	public String getSFX(){
		return "wav/fireball.wav";
	}

	public String getPromptPosition(){
		return "AIM";
	}
	
	public int getManaCost() {
		return 4;
	}
	
	public int getRange() {
		return 20;
	}
	
	public String getSelfTargettedMessage() {
		return "Nothing happens";
	}
	
	public String getSFXID() {
		return "SFX_FIRE_MISSILE";
	}
	
	public String getShootMessage() {
		return "FULGAR!";
	}
	
	public String getSpellAttackDesc() {
		return "fire missile";
	}
	
	public int getSpellDamage() {
		Player aPlayer = (Player) performer;
		return 3 + 2 * aPlayer.getSoulPower();
	}
	
	public double getTimeCostModifier() {
		return 1;
	}
	
	public int getHit() {
		return 80;
	}
}