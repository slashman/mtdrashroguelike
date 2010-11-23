package crl.player.advancements;

import crl.player.Player;

public class AdvCircle6 implements Advancement{

	public String getName(){
		return "Sixth Circle of Magic";
	}
	
	public void advance(Player p) {
		p.setFlag("MAGIC_CIRCLE6", true);
		p.setManaMax(p.getManaMax()+10);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_CIRCLE_6";
	}

	private String[] requirements = new String [] {
		"ADV_CIRCLE_5"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Gains access to the sixth circle of magic";
	}
}