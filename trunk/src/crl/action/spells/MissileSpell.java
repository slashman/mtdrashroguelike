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

public abstract class MissileSpell extends Spell{
	/** "The missile dives to the floor"*/ 
	public abstract String getSelfTargettedMessage();
	
	/** "You shoot a magic missile"*/
	public abstract String getShootMessage();
	
	public abstract int getRange();
	/**"SFX_MAGIC_MISSILE"*/
	public abstract String getSFXID(); 
	/** 2+aPlayer.getSoulPower() */
	public abstract int getSpellDamage(); 
	/** 2+aPlayer.getSoulPower() */
	public int getSpellBreak(){
		return getSpellDamage();
	}
	/** "magic missile" */
	public abstract String getSpellAttackDesc(); 
	public abstract String getPromptPosition();
	
	public abstract int getHit();
	
	public final boolean needsPosition(){
		return true;
	}
	
	public final void execute(){
		super.execute();
        Level aLevel = performer.getLevel();
        Player aPlayer = aLevel.getPlayer();
        if (targetPosition.equals(performer.getPosition())){
        	aLevel.addMessage(getSelfTargettedMessage());
        	return;
        }
	    
		aLevel.addMessage(getShootMessage());
		boolean hit = false;
		Line fireLine = new Line(performer.getPosition(), targetPosition);
		for (int i=0; i<getRange(); i++){
			Position destinationPoint = fireLine.next();
			String message = "";

			Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
        	if (destinationFeature != null && destinationFeature.isDestroyable()){
	        	message = "The " + getSpellAttackDesc() + " hits the "+destinationFeature.getDescription();
	        	drawEffect(EffectFactory.getSingleton().createDirectedEffect(aPlayer.getPosition(), targetPosition, getSFXID(), i));
				Feature prize = destinationFeature.damage(aPlayer, getSpellDamage());
	        	if (prize != null){
		        	message += " and destroys it.";
				}
				aLevel.addMessage(message);
				return;
			}

			Monster targetMonster = performer.getLevel().getMonsterAt(destinationPoint);
			//Cell destinationCell = performer.getLevel().getMapCell(destinationPoint);
			
			if (aLevel.isSolid(destinationPoint)){
				drawEffect(EffectFactory.getSingleton().createDirectedEffect(aPlayer.getPosition(), targetPosition, getSFXID(), i));
				return;
			}

			if (
				targetMonster != null /*&&
				(destinationCell.getHeight() == aLevel.getMapCell(aPlayer.getPosition()).getHeight() ||
				destinationCell.getHeight() -1  == aLevel.getMapCell(aPlayer.getPosition()).getHeight() ||
				destinationCell.getHeight() == aLevel.getMapCell(aPlayer.getPosition()).getHeight()-1)*/
			){
				if (targetMonster.tryMagicHit(aPlayer,getSpellBreak(),getSpellDamage(), getHit(), targetMonster.wasSeen(), getSpellAttackDesc())){
					drawEffect(EffectFactory.getSingleton().createDirectedEffect(aPlayer.getPosition(), targetPosition, getSFXID(), i));
					hit = true;
					return;
				};
			}
		}
		if (!hit)
			drawEffect(EffectFactory.getSingleton().createDirectedEffect(aPlayer.getPosition(), targetPosition, getSFXID(), getRange()));

	}

	
}