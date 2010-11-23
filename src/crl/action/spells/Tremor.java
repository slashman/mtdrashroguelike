package crl.action.spells;

import sz.util.Position;
import sz.util.Util;
import crl.action.Action;
import crl.actor.Actor;
import crl.feature.Feature;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;
import crl.ui.effects.EffectFactory;

public class Tremor extends Spell{
	
	public String getID(){
		return "Tremor";
	}
	
	public String getSFX(){
		return "wav/explosion.wav";
	}

	public void execute(){
		super.execute();
		
        Level aLevel = performer.getLevel();
        Player aPlayer = aLevel.getPlayer();
        
	    
		aLevel.addMessage("XAM MATERIA EX MAXIMA!!");
		
		int blasts = Util.rand(10,25);
		
		for (int i = 0; i < blasts; i++){
			targetPosition = new Position(Util.rand(6, aLevel.getWidth()-6), Util.rand(6, aLevel.getHeight()-6));
			for (int x = targetPosition.x -5; x <= targetPosition.x+5; x++)
				for (int y = targetPosition.y -5; y <= targetPosition.y+5; y++){
					Position destinationPoint = new Position(x,y);
					Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
		        	if (destinationFeature != null && destinationFeature.isDestroyable()){
						destinationFeature.damage(aPlayer, 6);
			        }
					Monster targetMonster = performer.getLevel().getMonsterAt(destinationPoint);
					if (targetMonster != null){
						targetMonster.damageIntegrity(aPlayer, 10);
					}
					Cell targetCell = aLevel.getMapCell(destinationPoint);
					if (targetCell.isSolid()){
						if (Util.chance(60)){
							performer.getLevel().transformCell(destinationPoint, "CAVE_FLOOR");
						}
					} else {
						if (!targetCell.getID().startsWith("DDOOR") && Util.chance(10)){
							performer.getLevel().addFeature("ROCK", destinationPoint);
						}
					}
					
				}
		}
	}

	public int getManaCost() {
		return 30;
	}
	
	public double getTimeCostModifier() {
		return 5;
	}
}