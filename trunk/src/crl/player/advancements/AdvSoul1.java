package crl.player.advancements;

import crl.player.Player;

public class AdvSoul1 implements Advancement{
	public String getName(){
		return "Soul";
	}
	public void advance(Player p) {
		p.setCastCost(p.getBaseCastCost()-5);
		p.setManaMax(p.getBaseManaMax()+5);
		p.setSoulPower(p.getBaseSoulPower()+1);
		p.recoverMana(10);
		p.setFlag(getID(), true);
	}

	public String getID() {
		return "ADV_SOUL1";
	}

	private String[] requirements = new String [] {
		
	};
	
	public String[] getRequirements() {
		return requirements;
	}

	public String getDescription(){
		return "Soul+1, Mana+5, Spellcasting+5";
	}
}