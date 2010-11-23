package crl.player.advancements;

import crl.player.Player;

public class AdvDiscipline1 implements Advancement{
	public String getName(){
		return "Discipline";
	}
	
	public void advance(Player p) {
		p.setWalkCost(p.getBaseWalkCost()-5);
		p.setEvasion(p.getBaseEvasion()+5);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_DISCIPLINE1";
	}

	private String[] requirements = new String [] {
		
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Speed+5, Evasion+5";
	}
}