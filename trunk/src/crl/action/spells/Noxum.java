package crl.action.spells;

import sz.util.Position;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;
import crl.ui.effects.EffectFactory;

public class Noxum extends Spell{
	
	public String getID(){
		return "Noxum";
	}
	
	public void execute(){
		super.execute();
        Level aLevel = performer.getLevel();
        Player aPlayer = aLevel.getPlayer();
        
		aLevel.addMessage("NOXUM!!");
		
		for (int i = 0; i < 6; i++){
			Position targetPosition = aPlayer.getNearestMonsterPosition();
			if (targetPosition == null)
				break;
			Monster m = aLevel.getMonsterAt(targetPosition);
			if (m.wasSeen()){
				drawEffect(EffectFactory.getSingleton().createDirectedEffect(aPlayer.getPosition(), targetPosition, "SFX_MINDBLAST", 20));
			}
			m.tryMagicHit(aPlayer, 3+aPlayer.getSoulPower()*4, 3+aPlayer.getSoulPower()*4, 50+aPlayer.getSoulPower()*7,m.wasSeen(), "mind blast");
		}
	}

	public int getManaCost() {
		return 20;
	}
	
	public double getTimeCostModifier() {
		return 3;
	}

}