package crl.player.advancements;

import crl.player.Player;

public class AdvDiscipline4 implements Advancement{
	public String getName(){
		return "Discipline 4";
	}
	
	public void advance(Player p) {
		p.setWalkCost(p.getBaseWalkCost()-5);
		p.setEvasion(p.getBaseEvasion()+5);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_DISCIPLINE4";
	}

	private String[] requirements = new String [] {
		"ADV_DISCIPLINE3"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Speed+5, Evasion+5";
	}
}