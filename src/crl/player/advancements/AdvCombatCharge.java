package crl.player.advancements;

import crl.player.Player;

public class AdvCombatCharge implements Advancement{
	public String getName(){
		return "Charge";
	}
	
	public void advance(Player p) {
		p.setFlag("COMBAT_CHARGE", true);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_COMBAT_CHARGE";
	}

	private String[] requirements = new String [] {
		
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Bashes enemies running to them at full speed";
	}
}