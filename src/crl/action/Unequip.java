package crl.action;

import crl.player.Player;

public class Unequip extends Action{
	public String getID(){
		return "Unequip";
	}
	
	public boolean needsEquipedItem(){
		return true;
	}

	public String getPromptEquipedItem(){
		return "What do you want to unequip?";
	}

	public void execute(){
		Player player = (Player)performer;
		
		if (player.getArmor() == targetEquipedItem){
			
			player.addItem(player.getArmor());
			player.setArmor(null);
		}
		if (player.getWeapon() == targetEquipedItem){
			player.addItem(player.getWeapon());
			player.setWeapon(null);
		}
		if (player.getAccesory() == targetEquipedItem){
			player.addItem(player.getAccesory());
			player.setAccesory(null);
		}
		if (player.getSecondaryWeapon() == targetEquipedItem){
			player.addItem(player.getSecondaryWeapon());
			player.setSecondaryWeapon(null);
		}
		if(player.getMana()>player.getManaMax())
			player.setMana(player.getManaMax());
		if(player.getIntegrityPoints()>player.getIntegrityMax())
			player.setIntegrityPoints(player.getIntegrityMax());
	}
}