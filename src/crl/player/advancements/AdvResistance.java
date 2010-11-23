package crl.player.advancements;

import crl.player.Player;

public class AdvResistance implements Advancement{
	public String getName(){
		return "Resistance";
	}
	
	public void advance(Player p) {
		p.setFlag("COMBAT_RESISTANCE", true);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_RESISTANCE";
	}

	private String[] requirements = new String [] {
		"ADV_TEMPER2",
		"ADV_DISCIPLINE2"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Always evade blows partially when in brave mode";
	}
}