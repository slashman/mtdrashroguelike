package crl.ui.graphicsUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JTextArea;

import sz.util.FileUtil;
import sz.util.ImageUtils;
import sz.util.Position;
import sz.util.ScriptUtil;

import crl.player.GameSessionInfo;
import crl.player.HiScore;
import crl.player.Player;
import crl.player.advancements.Advancement;
import crl.ui.CommandListener;
import crl.ui.Display;
import crl.ui.UserAction;
import crl.ui.UserCommand;
import crl.ui.UserInterface;
import crl.ui.graphicsUI.components.GFXChatBox;
import crl.conf.gfx.data.GFXCuts;
import crl.game.Game;
import crl.item.ItemDefinition;
import crl.item.ItemFactory;

public class GFXDisplay extends Display{
	private SwingSystemInterface si;
	
	private String IMG_TITLE;  
	private String IMG_PROLOGUE;
	private String IMG_RESUME;
	private String IMG_ENDGAME;
	private String IMG_HISCORES;
	private String IMG_HELP;
	private String IMG_SAVED;
	private String IMG_LEVEL_UP;
	public static String IMG_FRAME;
	public static Font FNT_TEXT;
	public static Font FNT_TITLE;
	public static Font FNT_DIALOGUEIN;
	public static Font FNT_MONO;
	public static Color COLOR_BOLD;
	private Color getColor(String rgba){
		String[] components = rgba.split(",");
		if (components.length == 4)
			return new Color (inte(components[0]), inte(components[1]), inte(components[2]),inte(components[3]));
		else
			return new Color (inte(components[0]), inte(components[1]), inte(components[2]));
	}
	private int inte(String n){
		return Integer.parseInt(n);
	}
	
	private void initProperties(Properties p){
		IMG_TITLE = p.getProperty("IMG_TITLE");
		IMG_PROLOGUE = p.getProperty("IMG_PROLOGUE");
		IMG_RESUME = p.getProperty("IMG_RESUME");
		IMG_ENDGAME = p.getProperty("IMG_ENDGAME");
		IMG_HISCORES = p.getProperty("IMG_HISCORES");
		IMG_HELP = p.getProperty("IMG_HELP");
		IMG_SAVED = p.getProperty("IMG_SAVED");
		IMG_LEVEL_UP = p.getProperty("IMG_LEVEL_UP");
		IMG_FRAME = p.getProperty("IMG_FRAME");
		COLOR_BOLD = getColor(p.getProperty("COLOR_BOLD"));
		try {
			FNT_TITLE = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File(p.getProperty("FNT_TITLE")))).deriveFont(Font.PLAIN, inte(p.getProperty("FNT_TITLE_SIZE")));
			FNT_TEXT = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File(p.getProperty("FNT_TEXT")))).deriveFont(Font.PLAIN, inte(p.getProperty("FNT_TEXT_SIZE")));
			FNT_DIALOGUEIN  = FNT_TEXT;
			FNT_MONO = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File(p.getProperty("FNT_MONO")))).deriveFont(Font.PLAIN, inte(p.getProperty("FNT_MONO_SIZE")));
		} catch (FontFormatException ffe){
			Game.crash("Error loading the font", ffe);
		} catch (IOException ioe){
			Game.crash("Error loading the font", ioe);
		} catch (Exception e){
			Game.crash("Error loading images", e);
		}
	}
	
	private SimpleAddornedBorderTextArea addornedTextArea;

	
	public GFXDisplay(SwingSystemInterface si, Properties p){
		initProperties(p);
		this.si = si;
		try {
			addornedTextArea = new SimpleAddornedBorderTextArea(
					new Color(187,161,80),
					new Color(92,78,36)
					);
			addornedTextArea.setVisible(false);
			addornedTextArea.setEnabled(false);
			addornedTextArea.setForeground(Color.WHITE);
			addornedTextArea.setBackground(Color.BLACK);
			addornedTextArea.setFont(FNT_TEXT);
			addornedTextArea.setOpaque(false);
		}
		 catch (Exception e){
			 Game.crash("Error loading UI data", e);
		 }
		 
		 si.add(addornedTextArea);

	}
	
	public int showTitleScreen(){
		((GFXUserInterface)UserInterface.getUI()).messageBox.setVisible(false);
		si.setFont(FNT_TEXT);
		si.drawImage(IMG_TITLE);
		si.printAtPixel(140, 535, "DrashRL v"+Game.getVersion()+", Slash ~ 2006-2007", Color.WHITE);
		si.refresh();
		CharKey x = new CharKey(CharKey.NONE);
		while (x.code != CharKey.A && x.code != CharKey.a &&
				x.code != CharKey.B && x.code != CharKey.b &&
				x.code != CharKey.C && x.code != CharKey.c &&
				x.code != CharKey.D && x.code != CharKey.d
				)
			x = si.inkey();
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
		si.drawImage(IMG_PROLOGUE);
		si.setFont(FNT_TITLE);
		si.printAtPixel(126,76, "The History So Far...", Color.WHITE);
		si.setFont(FNT_TEXT);
		si.setColor(Color.GRAY);
		JTextArea t1 = createTempArea(45,170,720,360);
		t1.setForeground(Color.LIGHT_GRAY);
		t1.setText(
				"The battle with the evil wizard Mondain was long and hard; the final showdown in "+
				"the bounds of time and space barely left you energies to keep on foot. Still, "+
				"the gem was shattered, and the peace returned to Sorsaria. \n\n"+
				"With your vanishing breath, you headed to the Time Machine... the time to rest "+
				"was close, you were going back home, perhaps watch some TV and go to vacations to "+
				"a caribbean island... you boarded the machine and activated the four crystals; "+
				"the ancient device turned on, the door closed, and the feeling of being stretched "+
				"and swallowed inside the current of time and space got a hold on yourself...");
						
		si.add(t1);
		si.printAtPixel(156,436, "[Space] to continue", COLOR_BOLD);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		t1.setText(
		"Then, all of a sudden, a green flash lit all the cockpit... a wicked smile "+
		"boomed around and the crystals shattered; the device seemed to crack appart. "+
		"The last thing you saw was a relieving, it was Sorsaria, high above the "+
		"clouds, not your place but better than drifting away forever in the void. \n\n"+
		"You passed out...");
		si.refresh();
		si.waitKey(CharKey.SPACE);
		t1.setText(
		"It has been months since that happened. Now you are imprisoned inside a "+
		"cell. Nobody told you why you are alive, but they are keeping you alive. "+
		"Finally, today, you started to realize what this was all about. You were taken out "+
		"of your cell and given a gauntlet with room for two gems. You saw yourself for a "+
		"last time in a giant screen, there were cameras all around the place.\n\n"+
		"Then, the voice... coming from all places and no place... 'Stranger from another "+
		"world! Fight, Entertain us and Win your freedom in this, the Garrintrots arena!'");
		si.refresh();
		si.waitKey(CharKey.SPACE);
		t1.setText(
		"You hear a choir of spectators, you are in The Game, you fight for them to laugh "+
		"for a while and then see you die. And there is no way back.");
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.remove(t1);
	}
	
	public boolean showResumeScreen(Player player){
		((GFXUserInterface)UserInterface.getUI()).messageBox.setVisible(false);
		si.drawImage(IMG_RESUME);
		
		GameSessionInfo gsi = player.getGameSessionInfo();
		si.setFont(FNT_TEXT);
		si.setColor(Color.ORANGE);
		si.print(3,3, "AS SEEN IN THE ARENA, THE ADVENTURE OF "+player.getName()+" FINISHED.", Color.BLUE);
		JTextArea t1 = createTempArea(30,125,690,75);
		t1.setForeground(Color.WHITE);
		t1.setText("Brave "+player.getName() + ", challenger to Drash, "+gsi.getDeathString()+" on the "+player.getLevel().getDescription()+"...");
		si.add(t1);
		si.print(3,14, "Do you want to archive this record? [Y/N]", Color.WHITE);
		si.refresh();
		boolean ret = UserInterface.getUI().prompt();
		si.remove(t1);
		return ret;
	}

	public void showEndgame(Player player){
		((GFXUserInterface)UserInterface.getUI()).messageBox.setVisible(false);
		si.drawImage(IMG_ENDGAME);
		si.setFont(FNT_TITLE);
		si.printAtPixel(126,76, "Epilogue", Color.WHITE);
		si.setFont(FNT_TEXT);
		si.setColor(Color.GRAY);
		JTextArea t1 = createTempArea(45,170,720,360);
		t1.setForeground(Color.LIGHT_GRAY);
		t1.setText(
				"After steping on the teleporter, you appear in a Sorsarian forest. The peaceful "+
				"noise of the birds and the streams of water replaces the cries of the choir "+
				"and the growling of monsters on the arena. In the distance, you see Mt. Drash, the "+
				"Arena in which you were reduced to a simple player inside a game of Death. You "+
				"head to Britain, seeking to find a way to return home.");
						
		si.add(t1);
		si.printAtPixel(156,436, "[Space] to continue", COLOR_BOLD);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		t1.setText(
				"Two months later, you return to the mountain with your group of fellow friends. "+
				"It is empty, it is just another dark dungeon in the lands of Sorsaria. You turn "+
				"your back and decide to forget everything that happened in this doomed place. "+
				"The future holds things of more relevance.");
		si.refresh();
		si.waitKey(CharKey.SPACE);
		
		si.remove(t1);

	}
	
	public void showHiscores(HiScore[] scores){
		si.drawImage(IMG_HISCORES);
		si.setFont(FNT_TITLE);
		si.print(3,3, "DrashRL: ARENA OF DEATH "+Game.getVersion(), Color.BLUE);
		si.print(3,4, "Legendary Challengers", Color.BLUE);
		si.setFont(FNT_TEXT);
		si.print(15,6, "Score", Color.BLUE);
		si.print(24,6, "Turns", Color.BLUE);	
		si.print(31,6, "Death", Color.BLUE);
		

		for (int i = 0; i < scores.length; i++){
			si.print(4,(7+i), scores[i].getName(), Color.BLUE);
			si.print(15,(7+i), ""+scores[i].getScore(), Color.WHITE);
			si.print(24,(7+i), ""+scores[i].getTurns(), Color.WHITE);
			si.print(31,(7+i), ""+scores[i].getDeathString()+" on level "+scores[i].getDeathLevel(), Color.WHITE);
		}
		si.print(4,19, "[Press Space]", Color.WHITE);
		si.refresh();
		si.waitKey(CharKey.SPACE);
	}
	
	

	public void showHelp(){
		si.saveBuffer();
		si.drawImage(IMG_HELP);
		//si.setFont(FNT_DIALOGUE);
		//si.print(3,1, "Help", GOLD);
		si.setFont(FNT_TEXT);
		
		si.print(3,2,  "(a)", COLOR_BOLD);
		si.print(3,3,  "(d)", COLOR_BOLD);
		si.print(3,4,  "(e)", COLOR_BOLD);
		si.print(3,5, "(f)", COLOR_BOLD);
		si.print(3,6, "(g)", COLOR_BOLD);
		si.print(3,7, "(r)", COLOR_BOLD);
		si.print(3,8, "(R)", COLOR_BOLD);
		si.print(3,9, "(s)", COLOR_BOLD);
		si.print(3,10, "(u)", COLOR_BOLD);
		si.print(3,11, "(U)", COLOR_BOLD);
		si.print(3,12, "(T)", COLOR_BOLD);
		si.print(3,13, "(x)", COLOR_BOLD);
		
		si.print(7,2,  "Attack: Uses a weapon in a given direction", Color.WHITE);
		si.print(7,3,  "Drop: Drops an item", Color.WHITE);
		si.print(7,4,  "Equip: Wears a weapon, armor or accesory", Color.WHITE);
		si.print(7,5, "Fire: Aims a weapon at a position", Color.WHITE);
		si.print(7,6, "Get: Picks up an item", Color.WHITE);
		si.print(7,7, "Reload: Reloads a given weapon", Color.WHITE);
		si.print(7,8, "Ready: Readies a secondary weapon", Color.WHITE);
		si.print(7,9, "Skills: Allows to use your character skills", Color.WHITE);
		si.print(7,10, "Use: Uses an Item", Color.WHITE);
		si.print(7,11, "Unequip: Take off an item", Color.WHITE);
		si.print(7,12, "Tactics: Switches normal and brave tactics", Color.WHITE);
		si.print(7,13, "Switch weapons: Exchange primary for secondary weapon", Color.WHITE);

		si.print(3,14, "(?)", COLOR_BOLD);
		si.print(3,15, "(c)", COLOR_BOLD);
		si.print(3,16, "(D)", COLOR_BOLD);
		si.print(3,17, "(i)", COLOR_BOLD);
		si.print(3,18, "(l)", COLOR_BOLD);
		si.print(3,19, "(m)", COLOR_BOLD);
		si.print(3,20, "(M)", COLOR_BOLD);
		si.print(3,21, "(T)", COLOR_BOLD);
		si.print(3,22, "(Q)", COLOR_BOLD);
		si.print(3,23, "(S)", COLOR_BOLD);
		
		si.print(7,14, "Help: Shows help (this screen)", Color.WHITE);
		si.print(7,15, "Character info: Shows your skills and attributes", Color.WHITE);
		si.print(7,16, "Character Dump: Dumps your character info to a file", Color.WHITE);
		si.print(7,17, "Inventory: Shows your current items", Color.WHITE);
		si.print(7,18, "Look: Identifies map symbols and monsters", Color.WHITE);
		si.print(7,19, "Messages: Shows the latest messages", Color.WHITE);
		si.print(7,20, "Map: Shows the level minimap", Color.WHITE);
		si.print(7,21, "Switch Music: Turns music on/off", Color.WHITE);
		si.print(7,22, "Quit: Exits game", Color.WHITE);
		si.print(7,23, "Save: Saves game", Color.WHITE);
		


		
		si.print(20,23,"[Space to continue]", COLOR_BOLD);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.restore();
		si.refresh();
	}
	
	public void init(SwingSystemInterface syst){
		si = syst;
	}
	
	
	
	public int showSavedGames(File[] saveFiles){
		si.drawImage(IMG_SAVED);
		if (saveFiles.length == 0){
			si.print(3,6, "No adventurers available",Color.WHITE);
			si.print(4,8, "[Space to Cancel]",Color.WHITE);
			si.refresh();
			si.waitKey(CharKey.SPACE);
			return -1;
		}
			
		si.print(3,6, "Pick an adventurer",Color.WHITE);
		for (int i = 0; i < saveFiles.length; i++){
			si.print(5,7+i, (char)(CharKey.a+i+1)+ " - "+ saveFiles[i].getName(), COLOR_BOLD);
		}
		si.print(3,9+saveFiles.length, "[Space to Cancel]", Color.WHITE);
		si.refresh();
		crl.ui.graphicsUI.CharKey x = si.inkey();
		while ((x.code < CharKey.a || x.code > CharKey.a+saveFiles.length) && x.code != CharKey.SPACE){
			x = si.inkey();
		}
		if (x.code == CharKey.SPACE)
			return -1;
		else
			return x.code - CharKey.a;
	}
	
	
	public void showTextBox(String text, int consoleX, int consoleY, int consoleW, int consoleH){
		addornedTextArea.setBounds(consoleX, consoleY, consoleW, consoleH);
		addornedTextArea.setText(text);
		addornedTextArea.setVisible(true);
		si.waitKey(CharKey.SPACE);
		addornedTextArea.setVisible(false);
	}
	
	public void showTextBox(String text, int consoleX, int consoleY, int consoleW, int consoleH, Font f){
		addornedTextArea.setBounds(consoleX, consoleY, consoleW, consoleH);
		addornedTextArea.setText(text);
		addornedTextArea.setFont(f);
		addornedTextArea.setVisible(true);
		si.waitKey(CharKey.SPACE);
		addornedTextArea.setVisible(false);
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
	
	//private Color TRANSPARENT_BLUE = new Color(100,100,100,200);
	
	
	public void showScreen(Object pScreen){
		/*si.saveBuffer();
		String screenText = (String) pScreen;
		showTextBox(screenText, 430, 70,340,375);
		si.restore();*/
		((GFXUserInterface)UserInterface.getUI()).messageBox.setVisible(false);
		si.saveBuffer();
		si.drawImage(IMG_FRAME);
		Font ole = si.getFont();
		si.setFont(FNT_MONO);
		try {
			BufferedReader red = FileUtil.getReader((String)pScreen);
			String line = red.readLine();
			int i = 0;
			while (line != null){
				if (i == 0)
					si.print(3, i+2, line, Color.BLUE);
				else
					si.print(3, i+2, line, Color.WHITE);
				i++;
				line = red.readLine();
			}
		} catch (IOException ioe){
			Game.crash("Error loading screen "+pScreen, ioe);
		}
		si.setFont(ole);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.restore();
		si.refresh();
		((GFXUserInterface)UserInterface.getUI()).messageBox.setVisible(true);
	}

	public static JTextArea createTempArea(int xpos, int ypos, int w, int h){
		JTextArea ret = new JTextArea();
		ret.setOpaque(false);
		ret.setForeground(Color.WHITE);
		ret.setVisible(true);
		ret.setEditable(false);
		ret.setFocusable(false);
		ret.setBounds(xpos, ypos, w, h);
		ret.setLineWrap(true);
		ret.setWrapStyleWord(true);
		ret.setFont(GFXDisplay.FNT_TEXT);
		return ret;
	}
	
	public Advancement levelUp(Player p) {
		((GFXUserInterface)UserInterface.getUI()).messageBox.setVisible(false);
		
		Vector advancements = p.getAvailableAdvancements();
		si.saveBuffer();
		si.drawImage(IMG_LEVEL_UP);
		si.print(4,3, "You have gained a change to pick an advancement!", Color.BLUE);
		for (int i = 0; i < advancements.size(); i++){
			si.print(3,4+i*2, ((char)('a'+i))+". "+((Advancement)advancements.elementAt(i)).getName(), Color.BLUE);
			si.print(3,5+i*2, "   "+((Advancement)advancements.elementAt(i)).getDescription(), Color.WHITE);
		}
		si.refresh();
		int choice = readAlphaToNumber(advancements.size());
		si.restore();
		si.refresh();
		((GFXUserInterface)UserInterface.getUI()).messageBox.setVisible(true);
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

