package crl.player.advancements;

import crl.player.Player;

public class AdvCombatStun implements Advancement{
	public String getName(){
		return "Stun";
	}
	
	public void advance(Player p) {
		p.setFlag("COMBAT_STUN", true);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_COMBAT_STUN";
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
		return "Randomly inmobilizes your enemies";
	}
}