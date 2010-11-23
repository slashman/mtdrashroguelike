package crl.action.spells;

import sz.util.Line;
import sz.util.Position;
import crl.action.Action;
import crl.actor.Actor;
import crl.feature.Feature;
import crl.level.Cell;
import crl.level.Level;
import crl.levelgen.MonsterSpawnInfo;
import crl.monster.Monster;
import crl.monster.VMonster;
import crl.player.Player;
import crl.ui.effects.EffectFactory;

public class Destruction extends Spell{
	
	public String getID(){
		return "Destruction";
	}
	
	public void execute(){
		super.execute();
        Level aLevel = performer.getLevel();
        Player aPlayer = aLevel.getPlayer();
        
		aLevel.addMessage("You mutter some indescriptible words!!");
		
		VMonster monsters = aLevel.getMonsters();
		for (int i = 0; i < monsters.size(); i++){
			Monster m = monsters.elementAt(i);
			if (Position.flatDistance(performer.getPosition(), m.getPosition()) < 10) 
				m.tryMagicHit(aPlayer, 2+aPlayer.getSoulPower()*6, 5+aPlayer.getSoulPower()*6, 50+aPlayer.getSoulPower()*8,m.wasSeen(), "darkness");
		}	
	}

	public int getManaCost() {
		return 40;
	}
	
	public double getTimeCostModifier() {
		return 8;
	}

}