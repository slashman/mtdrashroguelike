package crl.player.advancements;

import crl.player.Player;

public class AdvCircle7 implements Advancement{
	public String getName(){
		return "Seventh Circle of Magic";
	}
	public void advance(Player p) {
		p.setFlag("MAGIC_CIRCLE7", true);
		p.setManaMax(p.getManaMax()+20);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_CIRCLE_7";
	}

	private String[] requirements = new String [] {
		"ADV_CIRCLE_6",
		"ADV_SOUL2"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Gains access to the seventh circle of magic";
	}
}