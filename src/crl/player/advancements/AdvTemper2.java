package crl.player.advancements;

import crl.player.Player;

public class AdvTemper2 implements Advancement{
	public String getName(){
		return "Temper 2";
	}
	public void advance(Player p) {
		p.setAttack(p.getBaseAttack()+1);
		p.setEvadeBreak(p.getBaseEvadeBreak()+1);
		p.setAttackCost(p.getBaseAttackCost()-5);
		p.setIntegrityMax(p.getBaseIntegrityMax()+10);
		p.setEvadePointsMax(p.getBaseEvadePointsMax()+10);
		p.recoverIntegrity(10);
		p.recoverEvade(10);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_TEMPER2";
	}

	private String[] requirements = new String [] {
		"ADV_TEMPER1"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Attack+1, Break+1, Integrity+10, Evade Points+10, Combat+5";
	}
}