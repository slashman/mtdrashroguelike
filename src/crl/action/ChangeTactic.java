package crl.action;

import crl.player.Player;

public class ChangeTactic extends Action{
	public String getID(){
		return "ChangeTactic";
	}
	
	public void execute(){
		Player aPlayer = (Player) performer;
		int nextTactic = 0;
		switch (aPlayer.getCurrentTactic()){
		case Player.TACTIC_BRAVE:
			nextTactic = Player.TACTIC_NORMAL;
			aPlayer.getLevel().addMessage("You assume a defensive stance");
			break;
		case Player.TACTIC_NORMAL:
			nextTactic = Player.TACTIC_BRAVE;
			aPlayer.getLevel().addMessage("You assume an offensive stance!");
			break;
		}
		aPlayer.setCurrentTactic(nextTactic);
	}
	
	public int getCost(){
		return 1;
	}
}