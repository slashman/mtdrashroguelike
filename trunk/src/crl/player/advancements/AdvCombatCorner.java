package crl.player.advancements;

import crl.player.Player;

public class AdvCombatCorner implements Advancement{
	public String getName(){
		return "Corner";
	}
	public void advance(Player p) {
		p.setFlag("COMBAT_CORNER", true);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_COMBAT_CORNER";
	}

	private String[] requirements = new String [] {
		
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Corners enemies against walls";
	}
}