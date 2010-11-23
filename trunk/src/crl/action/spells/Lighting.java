package crl.action.spells;

import crl.player.Player;

public class Lighting extends MissileSpell{
	public String getID(){
		return "Lighting";
	}
	
	public String getSFX(){
		return "wav/thunder.wav";
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
		return "The thunderbolt dissapears into your hands";
	}
	
	public String getSFXID() {
		return "SFX_LIGHTING";
	}
	
	public String getShootMessage() {
		return "LORUM FLAMI!";
	}
	
	public String getSpellAttackDesc() {
		return "lighting bolt";
	}
	
	public int getSpellDamage() {
		Player aPlayer = (Player) performer;
		return 3+aPlayer.getSoulPower()*3;
	}
	
	public double getTimeCostModifier() {
		return 1;
	}
	
	public int getHit() {
		return 100;
	}
	
}