package crl.action.spells;

import sz.util.Line;
import sz.util.Position;
import crl.action.Action;
import crl.actor.Actor;
import crl.level.Cell;
import crl.level.Level;
import crl.player.Player;
import crl.ui.UserInterface;
import crl.ui.effects.EffectFactory;

public class Blink extends Spell{
	public int getManaCost() {
		return 10;
	}

	public double getTimeCostModifier() {
		return 1.4d;
	}

	public String getID(){
		return "Blink";
	}
	
	public boolean needsPosition(){
		return true;
	}

	public String getPromptPosition(){
		return "Where do you want to blink?";
	}

	public void execute(){
		super.execute();
		Player player = (Player) performer;
		Level level = player.getLevel();
		level.addMessage("HURTIS MATERIA!");
		if (targetPosition.equals(performer.getPosition())){
        	return;
        }

		drawEffect(EffectFactory.getSingleton().createLocatedEffect(player.getPosition(), "SFX_FLASH_TELEPORT"));
		
		Line line = new Line(player.getPosition(), targetPosition);
		Position start = new Position(player.getPosition());
		Position runner = line.next();
		int i = 0;
		for (; i < 10; i++){
			runner = line.next();
        	Cell destinationCell = performer.getLevel().getMapCell(runner);
			if (
				(
				!level.isSolid(runner) &&
				(destinationCell.getHeight() == level.getMapCell(player.getPosition()).getHeight() ||
				destinationCell.getHeight() -1  == level.getMapCell(player.getPosition()).getHeight() ||
				destinationCell.getHeight() == level.getMapCell(player.getPosition()).getHeight()-1))
			)
				player.setPosition(new Position(runner));
			else
				break;
		}
		//drawEffect(new LineMissileEffect(start, "\\|/--/|\\", Appearance.WHITE, start, targetPosition, i, 30));
		drawEffect(EffectFactory.getSingleton().createDirectedEffect(performer.getPosition(), targetPosition, "SFX_TELEPORT", i));
		player.see();
		UserInterface.getUI().refresh();
	}

	public String getSFX(){
		return "wav/scrch.wav";
	}
	
}