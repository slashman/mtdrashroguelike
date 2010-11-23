package crl.player.advancements;

import crl.player.Player;

public class AdvCombatPierce implements Advancement{
	public String getName(){
		return "Pierce";
	}
	
	public void advance(Player p) {
		p.setFlag("COMBAT_PIERCE", true);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_COMBAT_PIERCE";
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
		return "Stabs through enemies";
	}
}