package crl.player.advancements;

import crl.player.Player;

public class AdvCombatKnockback implements Advancement{
	public String getName(){
		return "Knockback";
	}
	
	public void advance(Player p) {
		p.setFlag("COMBAT_KNOCKBACK", true);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_COMBAT_KNOCKBACK";
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
		return "Randomly pushes small enemies";
	}
}