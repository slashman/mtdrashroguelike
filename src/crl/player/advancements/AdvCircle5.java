package crl.player.advancements;

import crl.player.Player;

public class AdvCircle5 implements Advancement{

	public String getName(){
		return "Fifth Circle of Magic";
	}
	
	public void advance(Player p) {
		p.setFlag("MAGIC_CIRCLE5", true);
		p.setManaMax(p.getManaMax()+10);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_CIRCLE_5";
	}

	private String[] requirements = new String [] {
		"ADV_CIRCLE_4"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Gains access to the fifth circle of magic";
	}
}