package crl.player.advancements;

import crl.player.Player;

public class AdvTemper1 implements Advancement{
	public String getName(){
		return "Temper";
	}
	public void advance(Player p) {
		p.setAttack(p.getBaseAttack()+1);
		p.setEvadeBreak(p.getBaseEvadeBreak()+1);
		p.setIntegrityMax(p.getBaseIntegrityMax()+5);
		p.setEvadePointsMax(p.getBaseEvadePointsMax()+5);
		p.recoverIntegrity(5);
		p.recoverEvade(5);
		p.setAttackCost(p.getBaseAttackCost()-5);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_TEMPER1";
	}

	private String[] requirements = new String [] {
			
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Attack+1, Break+1, Integrity+5, Evade Points+5, Combat+5";
	}
}