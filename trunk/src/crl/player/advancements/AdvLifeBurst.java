package crl.player.advancements;

import crl.player.Player;

public class AdvLifeBurst implements Advancement{
	public String getName(){
		return "LifeBurst";
	}
	
	public void advance(Player p) {
		p.setFlag("MAGIC_LIFEBURST", true);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_LIFEBURST";
	}

	private String[] requirements = new String [] {
		"ADV_SOUL2",
		"ADV_TEMPER2"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Sacrifice your health into a powerful bolt of energy";
	}
}