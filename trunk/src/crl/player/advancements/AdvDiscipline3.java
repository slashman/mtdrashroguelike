package crl.player.advancements;

import crl.player.Player;

public class AdvDiscipline3 implements Advancement{
	public String getName(){
		return "Discipline 3";
	}
	public void advance(Player p) {
		p.setWalkCost(p.getBaseWalkCost()-5);
		p.setEvasion(p.getBaseEvasion()+5);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_DISCIPLINE3";
	}

	private String[] requirements = new String [] {
		"ADV_DISCIPLINE2"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Speed+5, Evasion+5";
	}
}