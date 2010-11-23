package crl.action.spells;

import crl.player.Player;

public class IceBall extends MissileSpell{
	public String getID(){
		return "IceBall";
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
		return "SFX_ICE_BALL";
	}
	
	public String getShootMessage() {
		return "FRIGIDA HURTIS!";
	}
	
	public String getSpellAttackDesc() {
		return "ice ball";
	}
	
	public int getSpellDamage() {
		Player aPlayer = (Player) performer;
		return 4 * aPlayer.getSoulPower();
	}
	
	public double getTimeCostModifier() {
		return 1;
	}
	
	public int getHit() {
		return 80;
	}
}