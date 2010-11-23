package crl.player.advancements;

import crl.player.Player;

public class AdvSoul4 implements Advancement{
	public String getName(){
		return "Soul 4";
	}
	public void advance(Player p) {
		p.setCastCost(p.getBaseCastCost()-5);
		p.setManaMax(p.getBaseManaMax()+20);
		p.recoverMana(20);
		p.setSoulPower(p.getBaseSoulPower()+1);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_SOUL4";
	}

	private String[] requirements = new String [] {
		"ADV_SOUL3"
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Soul+1, Mana+20, Spellcasting+5";
	}
}