package crl.player.advancements;

import crl.player.Player;

public class AdvTemper3 implements Advancement{
	public String getName(){
		return "Temper 3";
	}
	public void advance(Player p) {
		p.setAttack(p.getBaseAttack()+2);
		p.setEvadeBreak(p.getBaseEvadeBreak()+2);
		p.setIntegrityMax(p.getBaseIntegrityMax()+10);
		p.setEvadePointsMax(p.getBaseEvadePointsMax()+10);
		p.setAttackCost(p.getBaseAttackCost()-5);
		p.recoverIntegrity(10);
		p.recoverEvade(10);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_TEMPER3";
	}

	private String[] requirements = new String [] {
		"ADV_TEMPER2"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Attack+2, Break+2, Integrity+10, Evade Points+10, Combat+5";
	}
}