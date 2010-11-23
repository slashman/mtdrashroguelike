package crl.player.advancements;

import crl.player.Player;

public class AdvCircle3 implements Advancement{

	public String getName(){
		return "Third Circle of Magic";
	}
	
	public void advance(Player p) {
		p.setFlag("MAGIC_CIRCLE3", true);
		p.setManaMax(p.getManaMax()+5);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_CIRCLE_3";
	}

	private String[] requirements = new String [] {
		"ADV_CIRCLE_2"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Gains access to the third circle of magic";
	}
}