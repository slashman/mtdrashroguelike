package crl.action.spells;

import sz.util.Position;
import crl.action.Action;
import crl.actor.Actor;
import crl.feature.Feature;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;
import crl.ui.effects.EffectFactory;

public class Explotion extends Spell{
	public String getID(){
		return "Explotion";
	}
	
	public int getManaCost() {
		return 10;
	}
	
	public double getTimeCostModifier() {
		return 1.5d;
	}
	
	public boolean needsPosition(){
		return true;
	}

	public void execute(){
		super.execute();
		Player aPlayer = (Player) performer;
        Level aLevel = performer.getLevel();
		aLevel.addMessage("FUEGO GRAVITA!");
		
		if (Position.distance(aPlayer.getPosition(), targetPosition) > 4){
			aLevel.addMessage("Too far.");
			return;
		}
		int damage = 1 + 3 * aPlayer.getSoulPower();

		aLevel.addEffect(EffectFactory.getSingleton().createLocatedEffect(targetPosition, "SFX_EXPLOTION"));
		/*aLevel.addSmartFeature("BURNING_FLAME", targetPosition);
		aLevel.addSmartFeature("BURNING_FLAME", Position.add(targetPosition, Action.directionToVariation(Action.UPLEFT)));
		aLevel.addSmartFeature("BURNING_FLAME", Position.add(targetPosition, Action.directionToVariation(Action.UPRIGHT)));
		aLevel.addSmartFeature("BURNING_FLAME", Position.add(targetPosition, Action.directionToVariation(Action.DOWNLEFT)));
		aLevel.addSmartFeature("BURNING_FLAME", Position.add(targetPosition, Action.directionToVariation(Action.DOWNRIGHT)));*/

		String message = "";
		for (int x = targetPosition.x -1; x <= targetPosition.x+1; x++)
			for (int y = targetPosition.y -1; y <= targetPosition.y+1; y++){
				Position destinationPoint = new Position(x,y);
				Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
	        	if (destinationFeature != null && destinationFeature.isDestroyable()){
					destinationFeature.damage(aPlayer, damage);
		        }
				Monster targetMonster = performer.getLevel().getMonsterAt(destinationPoint);
				if (targetMonster != null){
					targetMonster.tryMagicHit(aPlayer, damage, damage, 120, targetMonster.wasSeen(), "fireball");
				}
			}
		aLevel.addMessage(message);

	}

	public String getPromptPosition(){
		return "WHERE?";
	}
	
	public String getSFX(){
		return "wav/explosion.wav";
	}

	
}