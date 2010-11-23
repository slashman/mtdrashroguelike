package crl.player.advancements;

import crl.player.Player;

public class AdvCircle1 implements Advancement{

	public String getName(){
		return "First Circle of Magic";
	}
	
	public void advance(Player p) {
		p.setFlag("MAGIC_CIRCLE1", true);
		p.setManaMax(p.getManaMax()+5);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_CIRCLE_1";
	}

	public String[] requirements = new String[]{};
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Gains access to the first circle of magic";
	}
}
