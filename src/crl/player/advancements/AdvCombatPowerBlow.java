package crl.player.advancements;

import crl.player.Player;

public class AdvCombatPowerBlow implements Advancement{
	public String getName(){
		return "Power Blow";
	}
	
	public void advance(Player p) {
		p.setFlag("COMBAT_POWER", true);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_COMBAT_POWER";
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
		return "Stores energy to deliver a strong blow";
	}
}
