package crl.action.spells;

import sz.util.Util;
import crl.action.Action;
import crl.actor.Actor;
import crl.player.Player;

public class Prayer extends Spell{
	public int getManaCost() {
		return 1;
	}
	
	
	public String getID(){
		return "Recover";
	}
	
	public void execute(){
		super.execute();
		Player player = (Player)performer;
		int piety = player.getPiety();
		if (piety > 8){
			player.recoverIntegrity(30);
			player.recoverEvade(60);
			player.setCounter("POISON", 0);
			player.decreasePiety(Util.rand(1,3));
			player.getLevel().addMessage("A shimmering light covers you!");
		} else
		if (piety > 6){
			player.recoverIntegrity(20);
			player.recoverEvade(40);
			player.setCounter("POISON", 0);
			player.decreasePiety(Util.rand(1,3));
			player.getLevel().addMessage("You feel much better!");
		} else 
		if (piety > 4){
			if (Util.chance(70)){
				player.recoverIntegrity(30);
				player.decreasePiety(Util.rand(1,3));
				player.getLevel().addMessage("You feel better!");
			} else {
				player.recoverIntegrity(20);
				player.decreasePiety(Util.rand(1,3));
				player.getLevel().addMessage("You feel better.");
			}
		} else 
		if (piety > 2){
			if (Util.chance(70)){
				player.recoverIntegrity(20);
				player.decreasePiety(Util.rand(1,3));
				player.getLevel().addMessage("You feel better.");
			} else {
				player.getLevel().addMessage("You feel shocked!");
				player.selfDamage(Player.DAMAGE_SHOCKED, 5);
			}
		} else {
			if (Util.chance(70)){
				player.getLevel().addMessage("You feel shocked!");
				player.selfDamage(Player.DAMAGE_SHOCKED, 5);
				player.decreasePiety(Util.rand(1,3));
			} else {
				player.getLevel().addMessage("Your call was unheard.");
			}
		}
		piety = player.getPiety();
		if (piety > 8){
			player.getLevel().addMessage("You feel the heavens are really on your side!");
		} else
		if (piety > 6){
			player.getLevel().addMessage("You feel the heavens are on your side!");
		} else 
		if (piety > 4){
			player.getLevel().addMessage("You feel righteous.");
		} else 
		if (piety > 2){
			player.getLevel().addMessage("You feel unworthy.");
		} else {
			player.getLevel().addMessage("You feel really unworthy.");
		}
		
	}
	
	public double getTimeCostModifier() {
		return 2;
	}
	
	
}