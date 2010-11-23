package crl.action.spells;

import sz.util.Line;
import sz.util.Position;
import crl.action.Action;
import crl.actor.Actor;
import crl.feature.Feature;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;
import crl.ui.effects.EffectFactory;

public class LightingStorm extends Spell{
	
	public String getID(){
		return "Lighting";
	}
	
	public String getSFX(){
		return "wav/thunder.wav";
	}

	public void execute(){
		super.execute();
        Level aLevel = performer.getLevel();
        Player aPlayer = aLevel.getPlayer();
        
		aLevel.addMessage("LORUM XAM FUEGO!!");
		
		for (int i = 0; i < 4; i++){
			Position targetPosition = aPlayer.getNearestMonsterPosition();
			if (targetPosition == null)
				break;
			Monster m = aLevel.getMonsterAt(targetPosition);
			if (m.wasSeen()){
				drawEffect(EffectFactory.getSingleton().createDirectedEffect(aPlayer.getPosition(), targetPosition, "SFX_LIGHTING", 20));
			}
			m.tryMagicHit(aPlayer, 3+aPlayer.getSoulPower()*3, 3+aPlayer.getSoulPower()*3, 60+aPlayer.getSoulPower()*7,m.wasSeen(), "lighting bolt");
		}
	}

	public int getManaCost() {
		return 15;
	}
	
	public double getTimeCostModifier() {
		return 2;
	}

}