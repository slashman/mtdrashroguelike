package crl.player.advancements;

import crl.player.Player;

public class AdvCombatCinetic implements Advancement{
	public String getName(){
		return "Cinetic Bash";
	}
	
	public void advance(Player p) {
		p.setFlag("COMBAT_CINETIC", true);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_COMBAT_CINETIC";
	}

	private String[] requirements = new String [] {
			"ADV_COMBAT_CHARGE",
			"ADV_COMBAT_CORNER",
			"ADV_COMBAT_MIRAGE"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Doubles blow strength when attacking opposite side";
	}
}
