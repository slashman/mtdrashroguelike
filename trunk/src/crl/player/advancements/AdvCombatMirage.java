package crl.player.advancements;

import crl.player.Player;

public class AdvCombatMirage implements Advancement{
	public String getName(){
		return "Mirage";
	}
	public void advance(Player p) {
		p.setFlag("COMBAT_MIRAGE", true);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_COMBAT_MIRAGE";
	}

	private String[] requirements = new String [] {
			
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Increases evasion by moving side to side";
	}
}