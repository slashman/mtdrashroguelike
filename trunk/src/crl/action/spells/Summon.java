package crl.action.spells;

import sz.util.Position;
import sz.util.Util;
import crl.feature.Feature;
import crl.level.Level;
import crl.monster.Monster;
import crl.monster.MonsterFactory;
import crl.player.Player;

public class Summon extends Spell{
	public String getID(){
		return "Summon";
	}
	
	public int getManaCost() {
		return 20;
	}
	
	private final static String[] SUMMONS = new String[] {
		"ETTIN",
		"BALRON",
		"LICH",
		"PHANTOM"
	};
	
	public void execute(){
		super.execute();
		Level aLevel = performer.getLevel();
		aLevel.addMessage("KAM KALAM!");
		Position randPos = null;
		if (Util.chance(75)){
			int count = 50;
			out: while (count > 0){
				int xran = 1-Util.rand(0,2);
				int yran = 1-Util.rand(0,2);
				
				randPos = Position.add(performer.getPosition(), new Position(xran, yran));
				if (aLevel.isWalkable(randPos) && aLevel.getActorAt(randPos) == null){
					break out;
				}
				count--;
			}
			if (count > 0){
				Monster m = MonsterFactory.getFactory().buildMonster(Util.randomElementOf(SUMMONS));
				aLevel.addMessage("A "+m.getDescription()+" rises from nowhere!");
				m.setCounter("CHARM", 9999999);
				m.setPosition(randPos);
				aLevel.addMonster(m);
			} else {
				aLevel.addMessage("You feel an energy shock!");
			}
		} else {
			aLevel.addMessage("Nothing happens.");
		}
			
		
		
	}

	public String getSFX(){
		return "wav/clockbel.wav";
	}
	public double getTimeCostModifier() {
		return 4;
	}
}