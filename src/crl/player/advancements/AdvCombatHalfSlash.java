package crl.player.advancements;

import crl.player.Player;

public class AdvCombatHalfSlash implements Advancement{
	public String getName(){
		return "Half-Slash";
	}
	
	public void advance(Player p) {
		p.setFlag("COMBAT_HALFSLASH", true);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_COMBAT_HALFSLASH";
	}

	private String[] requirements = new String [] {
			"ADV_COMBAT_CINETIC",
			"ADV_COMBAT_DRASHBACK",
			"ADV_COMBAT_STUN"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Slices an enemy by crossing him.";
	}
}