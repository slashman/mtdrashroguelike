package crl.action.spells;

import sz.util.Position;
import crl.action.Action;
import crl.actor.Actor;
import crl.feature.Feature;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;
import crl.ui.UserInterface;
import crl.ui.effects.EffectFactory;

public class FireBarrage extends Spell{
	public String getID(){
		return "Fire Barrage";
	}
	
	public boolean needsDirection(){
		return true;
	}

	public String getPromptDirection(){
		return "WHERE?";
	}

	public String getSFX(){
		return "wav/fireball.wav";
	}
	
	public double getTimeCostModifier() {
		return 1.5d;
	}
	
	public int getManaCost() {
		return 5;
	}
	
	public void execute(){
		super.execute();
		Player aPlayer = (Player)performer;
		Level aLevel = aPlayer.getLevel();
		aLevel.addMessage("FUEGO RELI!");
   		int otherDir1 = 0;
		int otherDir2 = 0;
		switch (targetDirection){
			case Action.UP:
				otherDir1 = Action.UPLEFT;
				otherDir2 = Action.UPRIGHT;
				break;
			case Action.DOWN:
				otherDir1 = Action.DOWNLEFT;
				otherDir2 = Action.DOWNRIGHT;
				break;
			case Action.LEFT:
				otherDir1 = Action.UPLEFT;
				otherDir2 = Action.DOWNLEFT;
				break;
			case Action.RIGHT:
				otherDir1 = Action.UPRIGHT;
				otherDir2 = Action.DOWNRIGHT;
				break;
			case Action.UPRIGHT:
				otherDir1 = Action.UP;
				otherDir2 = Action.RIGHT;
				break;
			case Action.UPLEFT:
				otherDir1 = Action.UP;
				otherDir2 = Action.LEFT;
				break;
			case Action.DOWNLEFT:
				otherDir1 = Action.LEFT;
				otherDir2 = Action.DOWN;
				break;
			case Action.DOWNRIGHT:
				otherDir1 = Action.RIGHT;
				otherDir2 = Action.DOWN;
				break;
			case Action.SELF:
				aLevel.addMessage("The dragonfire rises as a flaming column!");
				hit (Position.add(performer.getPosition(), Action.directionToVariation(Action.UP)));
				hit (Position.add(performer.getPosition(), Action.directionToVariation(Action.UPLEFT)));
				hit (Position.add(performer.getPosition(), Action.directionToVariation(Action.LEFT)));
				hit (Position.add(performer.getPosition(), Action.directionToVariation(Action.DOWNLEFT)));
				hit (Position.add(performer.getPosition(), Action.directionToVariation(Action.DOWN)));
				hit (Position.add(performer.getPosition(), Action.directionToVariation(Action.DOWNRIGHT)));
				hit (Position.add(performer.getPosition(), Action.directionToVariation(Action.RIGHT)));
				hit (Position.add(performer.getPosition(), Action.directionToVariation(Action.UPRIGHT)));
	        	return;
		}
		Position directionVar = Action.directionToVariation(targetDirection);
		Position runner1 = Position.add(performer.getPosition(), Action.directionToVariation(otherDir1));
		Position runner2 = Position.add(performer.getPosition(), Action.directionToVariation(targetDirection));
		Position runner3 = Position.add(performer.getPosition(), Action.directionToVariation(otherDir2));
		for (int i = 0; i < 10; i++){
			hit (runner1);
			hit (runner2);
			hit (runner3);
			runner1.add(directionVar);
			runner2.add(directionVar);
			runner3.add(directionVar);
		}
	}

	private boolean hit (Position destinationPoint){
		Level aLevel = performer.getLevel();
        Player aPlayer = aLevel.getPlayer();
        UserInterface.getUI().drawEffect(EffectFactory.getSingleton().createLocatedEffect(destinationPoint, "SFX_PLACEBLAST"));
		//aLevel.addBlood(destinationPoint, 8);
		Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
        if (destinationFeature != null && destinationFeature.isDestroyable()){
			destinationFeature.damage(aPlayer, 4);
        	return true;
		}
        Monster targetMonster = performer.getLevel().getMonsterAt(destinationPoint);
        if (
			targetMonster != null &&
			!(targetMonster.isInWater() && targetMonster.canSwim())
			){
				targetMonster.tryMagicHit(aPlayer, 1+aPlayer.getSoulPower()*2, 1+aPlayer.getSoulPower()*2, 70+aPlayer.getSoulPower()*7, targetMonster.wasSeen(), "fire");
				return true;
			}
		return false;
	}
}