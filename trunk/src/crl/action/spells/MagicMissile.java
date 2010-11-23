package crl.action.spells;

import crl.player.Player;

public class MagicMissile extends MissileSpell{
	public String getID(){
		return "MagicMissile";
	}
	
	public String getSFX(){
		return "wav/fireball.wav";
	}

	public String getPromptPosition(){
		return "Aim your magic missile";
	}
	
	public int getManaCost() {
		return 2;
	}
	
	public int getRange() {
		return 20;
	}
	
	public String getSelfTargettedMessage() {
		return "The missile dives to the floor";
	}
	
	public String getSFXID() {
		return "SFX_MAGIC_MISSILE";
	}
	
	public String getShootMessage() {
		return "HURTIS FUEGO!";
	}
	
	public String getSpellAttackDesc() {
		return "magic missile";
	}
	
	public int getSpellBreak() {
		Player aPlayer = (Player) performer;
		return 2 + aPlayer.getSoulPower();
	}
	
	public int getSpellDamage() {
		Player aPlayer = (Player) performer;
		return 2 + aPlayer.getSoulPower();
	}
	
	public double getTimeCostModifier() {
		return 1;
	}
	
	public int getHit() {
		return 80;
	}
}