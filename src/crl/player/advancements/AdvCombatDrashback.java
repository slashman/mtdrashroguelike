package crl.player.advancements;

import crl.player.Player;

public class AdvCombatDrashback implements Advancement{
	public String getName(){
		return "Drashback";
	}
	public void advance(Player p) {
		p.setFlag("COMBAT_DRASHBACK", true);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_COMBAT_DRASHBACK";
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
		return "Returns a blow after three consecutive hits";
	}
}