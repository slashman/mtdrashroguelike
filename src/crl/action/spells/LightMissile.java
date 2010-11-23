package crl.action.spells;

import crl.player.Player;

public class LightMissile extends MissileSpell{
	public String getID(){
		return "LightMissile";
	}
	
	public String getSFX(){
		return "wav/fireball.wav";
	}

	public String getPromptPosition(){
		return "AIM";
	}
	
	public int getManaCost() {
		return 3;
	}
	
	public int getRange() {
		return 20;
	}
	
	public String getSelfTargettedMessage() {
		return "The light dissapears in your hands";
	}
	
	public String getSFXID() {
		return "SFX_LIGHT_MISSILE";
	}
	
	public String getShootMessage() {
		return "MITTAR!";
	}
	
	public String getSpellAttackDesc() {
		return "light missile";
	}
	
	public int getSpellDamage() {
		Player aPlayer = (Player) performer;
		return 5 + aPlayer.getSoulPower();
	}
	
	public double getTimeCostModifier() {
		return 1;
	}
	
	public int getHit() {
		return 90;
	}
}