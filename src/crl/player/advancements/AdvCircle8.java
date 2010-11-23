package crl.player.advancements;

import crl.player.Player;

public class AdvCircle8 implements Advancement{
	public String getName(){
		return "Eighth Circle of Magic";
	}
	public void advance(Player p) {
		p.setFlag("MAGIC_CIRCLE8", true);
		p.setManaMax(p.getManaMax()+20);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_CIRCLE_8";
	}

	private String[] requirements = new String [] {
		"ADV_CIRCLE_7"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Gains access to the eighth circle of magic";
	}
}