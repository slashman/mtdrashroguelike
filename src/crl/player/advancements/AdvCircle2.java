package crl.player.advancements;

import crl.player.Player;

public class AdvCircle2 implements Advancement{
	public String getName(){
		return "Second Circle of Magic";
	}

	public void advance(Player p) {
		p.setFlag("MAGIC_CIRCLE2", true);
		p.setManaMax(p.getManaMax()+5);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_CIRCLE_2";
	}

	private String[] requirements = new String [] {
		"ADV_CIRCLE_1"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Gains access to the second circle of magic";
	}
}
