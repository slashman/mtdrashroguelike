package crl.action.spells;

import sz.util.Position;
import sz.util.Util;
import crl.action.Action;
import crl.actor.Actor;
import crl.feature.Feature;
import crl.game.SFXManager;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;
import crl.ui.effects.EffectFactory;

public class FlameWind extends Spell{
	
	public String getID(){
		return "FLAME_WIND";
	}
	
	public String getSFX(){
		return "wav/fireball.wav";
	}

	public int getManaCost() {
		return 40;
	}
	public void execute(){
		super.execute();
        Level aLevel = performer.getLevel();
        Player aPlayer = aLevel.getPlayer();
        
		aLevel.addMessage("FUEGO HURACANADO EX LORUM!!!");
		
		int blasts = Util.rand(4,8);
		
		for (int i = 0; i < blasts; i++){
			targetPosition = Position.add(aPlayer.getPosition(), new Position(Util.rand(-5, 5), Util.rand(-5, 5)));
			if (!aLevel.isValidCoordinate(targetPosition))
				continue;
			aLevel.addEffect(EffectFactory.getSingleton().createLocatedEffect(targetPosition, "SFX_EXPLOTION"));
			for (int x = targetPosition.x -5; x <= targetPosition.x+5; x++)
				for (int y = targetPosition.y -5; y <= targetPosition.y+5; y++){
					Position destinationPoint = new Position(x,y);
					Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
		        	if (destinationFeature != null && destinationFeature.isDestroyable()){
						destinationFeature.damage(aPlayer, 6);
			        }
					Monster targetMonster = performer.getLevel().getMonsterAt(destinationPoint);
					if (targetMonster != null){
						targetMonster.tryMagicHit(aPlayer, (aPlayer.getSoulPower()+1)*4, (aPlayer.getSoulPower()+1)*5, 120, targetMonster.wasSeen(), "flame wind");
					}
				}
		}
	}

	public double getTimeCostModifier() {
		return 5;
	}
}