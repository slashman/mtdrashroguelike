package crl.ui.consoleUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Vector;


import crl.player.GameSessionInfo;
import crl.player.HiScore;
import crl.player.Player;
import crl.player.advancements.Advancement;
import crl.ui.CommandListener;
import crl.ui.Display;
import crl.ui.UserAction;
import crl.ui.UserCommand;
import crl.ui.UserInterface;
import crl.ui.consoleUI.cuts.CharChat;
import crl.conf.console.data.CharCuts;
import crl.game.Game;
import crl.game.STMusicManagerNew;
import sz.csi.CharKey;
import sz.csi.ConsoleSystemInterface;
import sz.csi.textcomponents.TextBox;
import sz.util.FileUtil;
import sz.util.ScriptUtil;

public class CharDisplay extends Display{
	private ConsoleSystemInterface si;
	
	public CharDisplay(ConsoleSystemInterface si){
		this.si = si;
	}
	
	public int showTitleScreen(){
		si.cls();
		String tempString = "Mount Drash, The Roguelike"; 
		si.print((80-tempString.length())/2,3, tempString, ConsoleSystemInterface.GREEN);
		tempString = "a. New Game";
		si.print((80-tempString.length())/2,9, tempString, ConsoleSystemInterface.BLUE);
		tempString = "b. Load Character";
		si.print((80-tempString.length())/2,10, tempString, ConsoleSystemInterface.BLUE);
		tempString = "c. Hall of Fame";
		si.print((80-tempString.length())/2,11, tempString, ConsoleSystemInterface.BLUE);
		tempString = "d. Quit";
		si.print((80-tempString.length())/2,12, tempString, ConsoleSystemInterface.BLUE);
		tempString = "DrashRL v"+Game.getVersion()+", Slash ~ 2006-2007";
		si.print((80-tempString.length())/2,19, tempString, ConsoleSystemInterface.WHITE);
		si.refresh();
    	STMusicManagerNew.thus.playKey("TITLE");
    	CharKey x = new CharKey(CharKey.NONE);
		while (x.code != CharKey.A && x.code != CharKey.a &&
				x.code != CharKey.B && x.code != CharKey.b &&
				x.code != CharKey.C && x.code != CharKey.c &&
				x.code != CharKey.D && x.code != CharKey.d)
			x = si.inkey();
		si.cls();
		switch (x.code){
		case CharKey.A: case CharKey.a:
			return 0;
		case CharKey.B: case CharKey.b:
			return 1;
		case CharKey.C: case CharKey.c:
			return 2;
		case CharKey.D: case CharKey.d:
			return 3;
		}
		return 0;
		
	}
	
	public void showIntro(Player player){
		si.cls();
		si.print(32,1, "Prologue", ConsoleSystemInterface.DARK_BLUE);
		
		TextBox tb1 = new TextBox(si);
		TextBox tb2 = new TextBox(si);
		tb2.setForeColor(ConsoleSystemInterface.BLUE);
		tb1.setForeColor(ConsoleSystemInterface.BLUE);

		tb1.setPosition(1,3);
		tb1.setHeight(4);
		tb1.setWidth(79);

		tb2.setPosition(1,8);
		tb2.setHeight(8);
		tb2.setWidth(79);

		tb1.setText(
"The battle with the evil wizard Mondain was long and hard; the final showdown in "+
"the bounds of time and space barely left you energies to keep on foot. Still, "+
"the gem was shattered, and the peace returned to Sorsaria");

		tb2.setText(
"With your vanishing breath, you headed to the Time Machine... the time to rest "+
"was close, you were going back home, perhaps watch some TV and go to vacations to "+
"a caribbean island... you boarded the machine and activated the four crystals; "+
"the ancient device turned on, the door closed, and the feeling of being stretched "+
"and swallowed inside the current of time and space got a hold on yourself...");

		tb1.draw();
		tb2.draw();
		si.print(2,18, "[Press Space]", ConsoleSystemInterface.BLUE);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.cls();

		tb1.setPosition(1,3);
		tb1.setHeight(5);
		tb1.setWidth(79);

		tb2.setPosition(1,9);
		tb2.setHeight(2);
		tb2.setWidth(79);

tb1.setText(
"Then, all of a sudden, a green flash lit all the cockpit... a wicked smile "+
"boomed around and the crystals shattered; the device seemed to crack appart. "+
"The last thing you saw was a relieving, it was Sorsaria, high above the "+
"clouds, not your place but better than drifting away forever in the void.");

tb2.setText(
"You passed out...");

		tb1.draw();
		tb2.draw();
		si.print(2,18, "[Press Space]", ConsoleSystemInterface.BLUE);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.cls();

		tb1.setPosition(1,3);
		tb1.setHeight(6);
		tb1.setWidth(79);

		tb2.setPosition(1,10);
		tb2.setHeight(3);
		tb2.setWidth(79);


tb1.setText(
"It has been months since that happened. Now you are imprisoned inside a "+
"cell. Nobody told you why you are alive, but they are keeping you alive. "+
"Finally, today, you started to realize what this was all about. You were taken out "+
"of your cell and given a gauntlet with room for two gems. You saw yourself for a "+
"last time in a giant screen, there were cameras all around the place.");

tb2.setText(
"Then, the voice... coming from all places and no place... 'Stranger from another "+
"world! Fight, Entertain us and Win your freedom in this, the Garrintrots arena!'");

		tb1.draw();
		tb2.draw();
		si.print(2,18, "[Press Space]", ConsoleSystemInterface.BLUE);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.cls();

		tb1.setPosition(1,3);
		tb1.setHeight(6);
		tb1.setWidth(79);


tb1.setText("You hear a choir of spectators, you are in The Game, you fight for them to smile "+
"for a while and then see you die. And there is no way back.");

		tb1.draw();
		si.print(2,18, "[Press Space]", ConsoleSystemInterface.BLUE);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.cls();
	}
	
	public boolean showResumeScreen(Player player){
		GameSessionInfo gsi = player.getGameSessionInfo();
		si.cls();
		String heshe = "She";
		String hisher = "her";
		
		
		si.print(2,3, "* * * AS SEEN IN THE ARENA, THE ADVENTURE OF "+player.getName()+" FINISHED.", ConsoleSystemInterface.BLUE);
		
		TextBox tb = new TextBox(si);
		tb.setPosition(2,5);
		tb.setHeight(3);
		tb.setWidth(70);
		tb.setForeColor(ConsoleSystemInterface.BLUE);
		tb.setText("Brave "+player.getName() + ", challenger to Drash, "+gsi.getDeathString()+" on the "+player.getLevel().getDescription()+"... ");
		tb.draw();
		
		si.print(2,14, "Do you want to archive this record? [Y/N]");
		
		return UserInterface.getUI().prompt();
	}

	public void showEndgame(Player player){
		si.cls();
		si.print(32,1, "Ending", ConsoleSystemInterface.DARK_BLUE);
		
		TextBox tb1 = new TextBox(si);
		TextBox tb2 = new TextBox(si);
		tb2.setForeColor(ConsoleSystemInterface.BLUE);
		tb1.setForeColor(ConsoleSystemInterface.BLUE);

		tb1.setPosition(1,3);
		tb1.setHeight(6);
		tb1.setWidth(79);

		tb2.setPosition(1,10);
		tb2.setHeight(5);
		tb2.setWidth(79);

		tb1.setText(
"After steping on the teleporter, you appear in a Sorsarian forest. The peaceful "+
"noise of the birds and the streams of water replaces the cries of the choir "+
"and the growling of monsters on the arena. In the distance, you see Mt. Drash, the "+
"Arena in which you were reduced to a simple player inside a game of Death. You "+
"head to Britain, seeking to find a way to return home.");

		tb2.setText(
"Two months later, you return to the mountain with your group of fellow friends. "+
"It is empty, it is just another dark dungeon in the lands of Sorsaria. You turn "+
"your back and decide to forget everything that happened in this doomed place. "+
"The future holds things of more relevance.");

		tb1.draw();
		tb2.draw();
		si.print(2,18, "[Press Space]", ConsoleSystemInterface.BLUE);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.cls();
	}
	
	public void showHiscores(HiScore[] scores){
		si.cls();


		si.print(2,1, "DrashRL: ARENA OF DEATH "+Game.getVersion(), ConsoleSystemInterface.BLUE);
		si.print(2,2, "Legendary Challengers", ConsoleSystemInterface.BLUE);

		si.print(13,4, "Score");
		si.print(20,4, "Date");	
		si.print(31,4, "Turns");
		si.print(38,4, "Death");

		for (int i = 0; i < scores.length; i++){
			si.print(2,5+i, scores[i].getName(), ConsoleSystemInterface.BLUE);
			si.print(13,5+i, ""+scores[i].getScore());
			si.print(20,5+i, ""+scores[i].getDate());
			si.print(31,5+i, ""+scores[i].getTurns());
			si.print(38,5+i, ""+scores[i].getDeathString()+" on level "+scores[i].getDeathLevel());

		}
		si.print(2,23, "[Press Space]");
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.cls();
	}
	
	public void showHelp(){
		si.cls();
	    
		si.print(2,0, "Help", ConsoleSystemInterface.BLUE);
		si.print(2,1, "Actions", ConsoleSystemInterface.BLUE);
		si.print(2,2,  "a Attack: Uses a weapon in a given direction");
		si.print(2,3,  "d Drop: Discards an item");
		si.print(2,4,  "e Equip: Wears a weapon or armor");
		si.print(2,5,  "f Fire: Targets a weapon on a given position");
		si.print(2,6,  "g Get: Picks up an item");
		si.print(2,7,  "r Reload: Reloads the equiped weapon");
		si.print(2,8,  "R Ready: Readies a secondary weapon");
		si.print(2,9, "s Skills: Shows the skill list");
		si.print(2,10, "t Tactics: Switches normal and brave tactics");
		si.print(2,11,  "u Use: Uses an Item");
		si.print(2,12,  "U Unequip: Take off an item");
		si.print(2,13,  "x Switch: Switched primary and secondary weapon");
		

		si.print(2,14, "Commands", ConsoleSystemInterface.BLUE);

		si.print(2,15, "? Help: Shows Help (this screen)");
		si.print(2,16, "c Character: Shows the character stats");
		si.print(2,17, "D Dump: Dumps you character info to a file");
		si.print(2,18, "i Inventory: Shows the inventory");
		si.print(2,19, "l Look: Identifies map symbols and monsters");
		si.print(2,20, "m Messages: Shows the message history");
		si.print(2,21, "M Map: Shows the level map in multiple screens");
		si.print(2,22, "T Music: Turns music on/off");
		si.print(2,23, "Q Quit: Exits game");
		si.print(2,24, "S Save: Saves game");
		
		
		//si.print(2,24,"[Press Space to continue]");

		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.cls();
	}
	
	public void init(ConsoleSystemInterface syst){
		si = syst;
	}
	
	public int showSavedGames(File[] saveFiles){
		si.cls();
		if (saveFiles.length == 0){
			si.print(3,6, "Thy journey has not yet begun");
			si.print(4,8, "[Space to Cancel]");
			si.refresh();
			si.waitKey(CharKey.SPACE);
			return -1;
		}
			
		si.print(3,6, "Pick an adventurer");
		for (int i = 0; i < saveFiles.length; i++){
			si.print(5,7+i, (char)(CharKey.a+i+1)+ " - "+ saveFiles[i].getName());
		}
		si.print(3,9+saveFiles.length, "[Space to Cancel]");
		si.refresh();
		CharKey x = si.inkey();
		while ((x.code < CharKey.a || x.code > CharKey.a+saveFiles.length) && x.code != CharKey.SPACE){
			x = si.inkey();
		}
		si.cls();
		if (x.code == CharKey.SPACE)
			return -1;
		else
			return x.code - CharKey.a;
	}
	
	private int readAlphaToNumber(int numbers){
		while (true){
			CharKey key = si.inkey();
			if (key.code >= CharKey.A && key.code <= CharKey.A + numbers -1){
				return key.code - CharKey.A;
			}
			if (key.code >= CharKey.a && key.code <= CharKey.a + numbers -1){
				return key.code - CharKey.a;
			}
		}
	}
	
	public void showChat(String chatID, Game game){
		si.saveBuffer();
		CharChat chat = CharCuts.thus.get(chatID);
		TextBox tb = new TextBox(si);
		tb.setBounds(3,4,40,10);
		tb.setBorder(true);
		String[] marks = new String[] {"%NAME"};
		String[] replacements = new String[] {game.getPlayer().getName()};
		for (int i = 0; i < chat.getConversations(); i++){
			tb.clear();
			tb.setText(ScriptUtil.replace(marks, replacements, chat.getConversation(i)));
			tb.setTitle(ScriptUtil.replace(marks, replacements, chat.getName(i)));
			tb.draw();
			si.refresh();
			si.waitKey(CharKey.SPACE);
		}
		si.restore();
	}
	
	public void showScreen(Object pScreen){
		si.saveBuffer();
		si.cls();
		try {
			BufferedReader red = FileUtil.getReader((String)pScreen);
			String line = red.readLine();
			int i = 0;
			while (line != null){
				si.print(1, i+2, line);
				i++;
				line = red.readLine();
			}
		} catch (IOException ioe){
			Game.crash("Error loading screen "+pScreen, ioe);
		}
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.restore();
	}
	

	public Advancement levelUp(Player p) {
		Vector advancements = p.getAvailableAdvancements();
		si.saveBuffer();
		si.cls();
		si.print(1,1, "You have gained a change to pick an advancement!", ConsoleSystemInterface.BLUE);
		
		for (int i = 0; i < advancements.size(); i++){
			si.print(1,3+i*2, ((char)('a'+i))+". "+((Advancement)advancements.elementAt(i)).getName());
			si.print(1,4+i*2, ((Advancement)advancements.elementAt(i)).getDescription());
		}
		si.refresh();
		int choice = readAlphaToNumber(advancements.size());
		si.restore();			
		Advancement ret = (Advancement)advancements.elementAt(choice);
		if (ret.getID().equals("ADV_COMBAT_CHARGE"))
			showScreen("screens/charge.txt");
		else if (ret.getID().equals("ADV_COMBAT_CINETIC"))
			showScreen("screens/cinetic.txt");
		else if (ret.getID().equals("ADV_COMBAT_CORNER"))
			showScreen("screens/corner.txt");
		else if (ret.getID().equals("ADV_COMBAT_MIRAGE"))
			showScreen("screens/mirage.txt");
		else if (ret.getID().equals("ADV_COMBAT_SLASH"))
			showScreen("screens/slash.txt");
		else if (ret.getID().equals("ADV_COMBAT_HALFSLASH"))
			showScreen("screens/halfslash.txt");
		return ret;
	}
	

}
