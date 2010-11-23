package crl.action.spells;

import sz.util.Position;
import crl.action.Action;
import crl.actor.Actor;
import crl.feature.Feature;
import crl.player.Player;

public class DestroyBarrier extends Spell{
	
	public int getManaCost() {
		return 5;
	}

	public double getTimeCostModifier() {
		return 3;
	}

	public String getPromptPosition() {
		return "WHERE?";
	}

	public boolean needsPosition() {
		return true;
	}

	public String getID(){
		return "Destroy Barrier";
	}
	
	
	public void execute(){
		super.execute();
		Player player = (Player)performer;
		player.getLevel().addMessage("XAM KALAM!");
		if (Position.distance(player.getPosition(), targetPosition)>8)
			player.getLevel().addMessage("Too Far.");
		else {
			Feature feat = player.getLevel().getFeatureAt(targetPosition);
			if (feat == null){
				player.getLevel().addMessage("Nothing there.");
			} else {
				if (feat.getID().equals("MAGIC_BARRIER")){
					player.getLevel().destroyFeature(feat);
				} else {
					player.getLevel().addMessage("Nothing happens.");
				}
			}
		}
	}
}