package crl.player.advancements;

import crl.player.Player;

public class AdvCombatCounter implements Advancement{
	public String getName(){
		return "Counter-attack";
	}
	public void advance(Player p) {
		p.setFlag("COMBAT_COUNTER", true);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_COMBAT_COUNTER";
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
		return "Counterattacks";
	}
}