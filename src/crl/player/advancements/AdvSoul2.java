package crl.player.advancements;

import crl.player.Player;

public class AdvSoul2 implements Advancement{
	public String getName(){
		return "Soul 2";
	}
	public void advance(Player p) {
		p.setCastCost(p.getBaseCastCost()-5);
		p.setManaMax(p.getBaseManaMax()+10);
		p.recoverMana(10);
		p.setSoulPower(p.getBaseSoulPower()+1);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_SOUL2";
	}

	private String[] requirements = new String [] {
		"ADV_SOUL1"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Soul+1, Mana+10, Spellcasting+5";
	}
}