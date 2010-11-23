package crl.player.advancements;

import java.io.Serializable;

import crl.player.Player;

public interface Advancement extends Serializable{
	public String[] getRequirements();
	public String getID();
	public void advance(Player p);
	public String getDescription();
	public String getName();
}
