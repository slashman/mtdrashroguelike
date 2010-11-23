package crl.player.advancements;

import crl.player.Player;

public class AdvDrain implements Advancement{
	public String getName(){
		return "LifeDrain";
	}
	
	public void advance(Player p) {
		p.setFlag("MAGIC_LIFEDRAIN", true);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_LIFEDRAIN";
	}

	private String[] requirements = new String [] {
		"ADV_SOUL2",
		"ADV_DISCIPLINE2"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Steal enemies health and transform it into mana";
	}
}