package crl.player.advancements;

import crl.player.Player;

public class AdvTemper4 implements Advancement{
	public String getName(){
		return "Temper 4";
	}
	public void advance(Player p) {
		p.setAttack(p.getBaseAttack()+2);
		p.setEvadeBreak(p.getBaseEvadeBreak()+2);
		p.setIntegrityMax(p.getBaseIntegrityMax()+20);
		p.setEvadePointsMax(p.getBaseEvadePointsMax()+20);
		p.setAttackCost(p.getBaseAttackCost()-5);
		p.recoverIntegrity(20);
		p.recoverEvade(20);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_TEMPER4";
	}

	private String[] requirements = new String [] {
		"ADV_TEMPER3"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Attack+2, Break+2, Integrity+20, Evade Points+20, Combat+5";
	}
}