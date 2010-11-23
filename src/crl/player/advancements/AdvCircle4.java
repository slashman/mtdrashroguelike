package crl.player.advancements;

import crl.player.Player;

public class AdvCircle4 implements Advancement{

	public String getName(){
		return "Fourth Circle of Magic";
	}
	
	public void advance(Player p) {
		p.setFlag("MAGIC_CIRCLE4", true);
		p.setManaMax(p.getManaMax()+10);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_CIRCLE_4";
	}

	private String[] requirements = new String [] {
		"ADV_CIRCLE_3",
		"ADV_SOUL1"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Gains access to the fourth circle of magic";
	}
}