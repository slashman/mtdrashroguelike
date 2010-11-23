package crl.player.advancements;

import crl.player.Player;

public class AdvCombatSlash implements Advancement{
	public String getName(){
		return "Slash";
	}
	
	public void advance(Player p) {
		p.setFlag("COMBAT_SLASH", true);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_COMBAT_SLASH";
	}

	private String[] requirements = new String [] {
			"ADV_COMBAT_KNOCKBACK",
			"ADV_COMBAT_PIERCE",
			"ADV_COMBAT_HALFSLASH"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Slices two adjacent enemies by crossing them.";
	}
}