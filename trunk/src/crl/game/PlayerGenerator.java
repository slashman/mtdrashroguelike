
package crl.game;

import java.util.Hashtable;

import sz.util.Util;
import crl.item.Item;
import crl.item.ItemFactory;
import crl.player.Player;
import crl.ui.AppearanceFactory;

public abstract class PlayerGenerator {
	public static PlayerGenerator thus;
	public abstract Player generatePlayer();
	
	private Hashtable SPECIAL_PLAYERS = new Hashtable();
	private void initSpecialPlayers(){
		SPECIAL_PLAYERS.clear();
}
	public Player createSpecialPlayer(String playerID){
		initSpecialPlayers();
		return (Player) SPECIAL_PLAYERS.get(playerID);
	}
	
	public Player getPlayer(String name, int sex){
		Player player = new Player();
		player.setSex(sex);
		if (name.trim().equals("")){
			if (sex == Player.MALE)
				player.setName(Util.randomElementOf(MALE_NAMES));
			else
				player.setName(Util.randomElementOf(FEMALE_NAMES));
		} else {
			player.setName(name);
		}
		
		
		String[] initWeapons = null;
		String[] initArmors = null;
		String[] initItems = null;
		player.setBaseSightRange(4);
		
		initWeapons = VKILLER_WEAPONS;
		initArmors = VKILLER_ARMORS;
		initItems = VKILLER_ITEMS;
		player.setWalkCost(50);
		player.setAttackCost(50);
		player.setCastCost(50);
		player.setCarryMax(20);
		
		player.setEvadePointsMax(30);
		player.setEvadePoints(30);
		player.setIntegrityMax(60);
		player.setIntegrityPoints(60);
		//player.setIntegrityPoints(20000);
		player.setEvasion(40);
		player.setAttack(0);
		player.setEvadeBreak(0);
		
		
		player.setManaMax(40);
		player.setMana(40);
		//player.setMana(40000);
		
		ItemFactory itf = ItemFactory.getItemFactory();
		AppearanceFactory apf = AppearanceFactory.getAppearanceFactory();
		if (player.getSex()==Player.MALE)
			player.setAppearance(apf.getAppearance("AVATAR"));
		else
			player.setAppearance(apf.getAppearance("AVATAR_F"));
		if (initWeapons != null)
			player.setWeapon(itf.createItem(Util.randomElementOf(initWeapons)));
		player.setArmor(itf.createItem(Util.randomElementOf(initArmors)));
		int items = 10;
		
		for (int i = 0; i < items; i++){
			Item it = itf.createItem(Util.randomElementOf(initItems));
			if (it == null){
				i--;
				continue;
					
			}
			player.addItem(it);
		}
		
		/*player.setFlag("COMBAT_STUN", true);
		player.setFlag("COMBAT_HALFSLASH", true);
		//Test
		/*player.setFlag("COMBAT_CORNER", true);
		player.setFlag("COMBAT_CHARGE", true);
		player.setFlag("COMBAT_DRASHBACK", true);
		player.setFlag("COMBAT_COUNTER", true);
		player.setFlag("COMBAT_CINETIC", true);
		player.setFlag("COMBAT_PIERCE", true);
		player.setFlag("COMBAT_POWER", true);
		player.setFlag("COMBAT_MIRAGE", true);
		player.setFlag("COMBAT_SLASH", true);*/
		/*
		player.setFlag("MAGIC_CIRCLE2", true);
		player.setFlag("COMBAT_CORNER", true);*/
		return player;

	}
	
	private String[] VKILLER_WEAPONS = new String[] {"DAGGER", "CUDGEL"};
	private String[] VKILLER_ARMORS = new String[] {"LEATHER"};
	private String[] VKILLER_ITEMS = new String[] {"RED_POTION", "YELLOW_POTION", "TORCH"};
	//private String[] VKILLER_ITEMS = new String[] {"KEG"};
	
	protected String [] MALE_NAMES = new String [] {"Rodney", "Avatar", "Adam", "Bjorn", "Anubis", "Iolo", "Dupre", "Shamino", "Hadral"};
	protected String [] FEMALE_NAMES = new String [] {"Maiden", "Valentina", "Carrie", "Sypha", "Mina", "Mariah", "Julia", "Katrina", "Jaana"};


}
