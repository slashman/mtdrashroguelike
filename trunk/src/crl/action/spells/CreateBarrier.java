package crl.action.spells;



import sz.util.Position;
import crl.action.Action;
import crl.actor.Actor;
import crl.player.Player;

public class CreateBarrier extends Spell{
	
	public int getManaCost() {
		return 5;
	}

	public double getTimeCostModifier() {
		return 2;
	}

	public String getPromptPosition() {
		return "WHERE?";
	}

	public boolean needsPosition() {
		return true;
	}

	public String getID(){
		return "Create Barrier";
	}
	
	
	public void execute(){
		super.execute();
		Player player = (Player)performer;
		player.getLevel().addMessage("IN KALAM!");
		if (Position.distance(player.getPosition(), targetPosition)>8)
			player.getLevel().addMessage("Too Far.");
		else {
			if (player.getLevel().isSolid(targetPosition) || player.getLevel().getMonsterAt(targetPosition) != null)
				return;
			player.getLevel().addFeature("MAGIC_BARRIER", targetPosition);
			player.getLevel().addFeature("MAGIC_BARRIER", Position.add(targetPosition, Action.directionToVariation(Action.UP)));
			player.getLevel().addFeature("MAGIC_BARRIER", Position.add(targetPosition, Action.directionToVariation(Action.DOWN)));
			player.getLevel().addFeature("MAGIC_BARRIER", Position.add(targetPosition, Action.directionToVariation(Action.LEFT)));
			player.getLevel().addFeature("MAGIC_BARRIER", Position.add(targetPosition, Action.directionToVariation(Action.RIGHT)));
		}
	}
}