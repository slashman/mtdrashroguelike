package crl.ui.consoleUI;

import java.util.*;

import sz.csi.CharKey;
import sz.csi.ConsoleSystemInterface;
import sz.csi.textcomponents.BasicListItem;
import sz.csi.textcomponents.ListBox;
import sz.csi.textcomponents.MenuBox;
import sz.csi.textcomponents.TextInformBox;
import sz.util.*;

import crl.action.*;
import crl.ui.consoleUI.effects.CharEffect;
import crl.ui.effects.*;
import crl.player.*;
import crl.item.*;
import crl.level.*;
import crl.monster.*;
import crl.feature.*;
import crl.game.GameFiles;
import crl.game.STMusicManagerNew;
import crl.ai.*;
import crl.actor.*;
import crl.ui.*;

/** 
 *  Shows the level using characters.
 *  Informs the Actions and Commands of the player.
 * 	Must be listening to a System Interface
 */

public class ConsoleUserInterface extends UserInterface implements CommandListener, Runnable{
	private static UISelector selector;
	
	//Attributes
	private int xrange = 28;
	private int yrange = 9;
	
	//Components
	private TextInformBox messageBox;
	private ListBox idList;
	
	private boolean eraseOnArrival; // Erase the buffer upon the arrival of a new msg
	private String lastMessage; 
    private StringBuffer messageBuffer = new StringBuffer();
	private int msgCounter;
	private Monster lockedMonster;
	
	private Hashtable /*BasicListItem*/ sightListItems = new Hashtable();
	// Relations

 	private transient ConsoleSystemInterface si;

	// Setters
	/** Sets the object which will be informed of the player commands.
     * this corresponds to the Game object */
	
	//Getters

    // Smart Getters
    public Position getAbsolutePosition(Position insideLevel){
    	Position relative = Position.subs(insideLevel, player.getPosition());
		return Position.add(PC_POS, relative);
	}

	public final Position
		VP_START = new Position(1,1),
		VP_END = new Position (58,19),
		PC_POS = new Position (29,9);

    private final int WEAPONCODE = CharKey.SPACE;

    private boolean [][] FOVMask;
    //Interactive Methods
    public void doLook(){
		Position offset = new Position (0,0);
		messageBox.setForeColor(ConsoleSystemInterface.BLUE);
		si.saveBuffer();
		while (true){
			Position browser = Position.add(player.getPosition(), offset);
			String looked = "";
			si.restore();
			if (FOVMask[PC_POS.x + offset.x][PC_POS.y + offset.y]){
				Cell choosen = level.getMapCell(browser);
				Feature feat = level.getFeatureAt(browser);
				Vector items = level.getItemsAt(browser);
				Item item = null;
				if (items != null) {
					item = (Item) items.elementAt(0);
				}
				Actor actor = level.getActorAt(browser);
				if (choosen != null)
					looked += choosen.getDescription();
				if (level.getBloodAt(browser) != null)
				    looked += "{bloody}";
				if (feat != null)
					looked += ", "+ feat.getDescription();
				if (actor != null)
					looked += ", "+ actor.getDescription();
				if (item != null)
					if (items.size() == 1)
						looked += ", "+ item.getDescription();
					else
						looked += ", "+ item.getDescription()+" and some items";
			}
			messageBox.setText(looked);
			messageBox.draw();
			si.print(PC_POS.x + offset.x, PC_POS.y + offset.y, '_', ConsoleSystemInterface.BLUE);
			si.refresh();
			CharKey x = new CharKey(CharKey.NONE);
			while (x.code != CharKey.SPACE && x.code != CharKey.ESC &&
				   ! x.isArrow())
				x = si.inkey();
			if (x.code == CharKey.SPACE || x.code == CharKey.ESC){
				si.restore();
				break;
			}

			offset.add(Action.directionToVariation(Action.toIntDirection(x)));

			if (offset.x >= xrange) offset.x = xrange;
			if (offset.x <= -xrange) offset.x = -xrange;
			if (offset.y >= yrange) offset.y = yrange;
			if (offset.y <= -yrange) offset.y = -yrange;
     	}
		messageBox.setText("Look mode off");
		refresh();
	}

    

    // Drawing Methods
	public void drawEffect(Effect what){
		//Debug.enterMethod(this, "drawEffect", what);
		if (what == null)
			return;
		//drawLevel();
		if (insideViewPort(getAbsolutePosition(what.getPosition()))){
			si.refresh();
			si.setAutoRefresh(true);
			((CharEffect)what).drawEffect(this, si);
			si.setAutoRefresh(false);
		}
		//Debug.exitMethod();
	}
	
	public boolean isOnFOVMask(int x, int y){
		return FOVMask[x][y];
	}

	private void drawLevel(){
		Debug.enterMethod(this, "drawLevel");
		//Cell[] [] cells = level.getCellsAround(player.getPosition().x,player.getPosition().y, player.getPosition().z, range);
		Cell[] [] rcells = level.getMemoryCellsAround(player.getPosition().x,player.getPosition().y, player.getPosition().z, xrange,yrange);
		Cell[] [] vcells = level.getVisibleCellsAround(player.getPosition().x,player.getPosition().y, player.getPosition().z, xrange,yrange);
		
		Position runner = new Position(player.getPosition().x - xrange, player.getPosition().y-yrange, player.getPosition().z);
		
		for (int x = 0; x < rcells.length; x++){
			for (int y=0; y<rcells[0].length; y++){
				if (rcells[x][y] != null){
					CharAppearance app = (CharAppearance)rcells[x][y].getAppearance(); 
					char cellChar = app.getChar();
					if (level.getFrostAt(runner) != 0){
						cellChar = '#';
					}
					//if (!level.isVisible(runner.x, runner.y))
					if (vcells[x][y] == null)
						si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, cellChar, ConsoleSystemInterface.GRAY);
				} else
					si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, CharAppearance.getVoidAppearance().getChar(), CharAppearance.BLACK);
				runner.y++;
			}
			runner.y = player.getPosition().y-yrange;
			runner.x ++;
		}
		
		
		runner.x = player.getPosition().x - xrange;
		runner.y = player.getPosition().y-yrange;
		
		monstersOnSight.removeAllElements();
		featuresOnSight.removeAllElements();
		itemsOnSight.removeAllElements();
		
		for (int x = 0; x < vcells.length; x++){
			for (int y=0; y<vcells[0].length; y++){
				FOVMask[PC_POS.x-xrange+x][PC_POS.y-yrange+y] = false;
				if (vcells[x][y] != null){
					FOVMask[PC_POS.x-xrange+x][PC_POS.y-yrange+y] = true;
					String bloodLevel = level.getBloodAt(runner);
					CharAppearance cellApp = (CharAppearance)vcells[x][y].getAppearance();
					int cellColor = cellApp.getColor();
					if (bloodLevel != null){
						switch (Integer.parseInt(bloodLevel)){
							case 0:
								cellColor = ConsoleSystemInterface.RED;
								break;
							case 1:
								cellColor = ConsoleSystemInterface.DARK_RED;
								break;
							case 8:
								cellColor = ConsoleSystemInterface.LEMON;
								break;
						}
					}
					char cellChar = cellApp.getChar();
					if (level.getFrostAt(runner) != 0){
						cellChar = '#';
						cellColor = ConsoleSystemInterface.CYAN;
					}
					if (level.getDepthFromPlayer(player.getPosition().x - xrange + x, player.getPosition().y - yrange + y) != 0 ){
						cellColor = ConsoleSystemInterface.TEAL;
					}

					if (player.isInvisible() || x!=xrange || y != yrange)
						si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, cellChar, cellColor);
					Feature feat = level.getFeatureAt(runner);
					if (feat != null){
						if (feat.isVisible()) {
							BasicListItem li = (BasicListItem)sightListItems.get(feat.getID());
							if (li == null){
								Debug.say("Adding "+feat.getID()+" to the hashtable");
								sightListItems.put(feat.getID(), new BasicListItem(((CharAppearance)feat.getAppearance()).getChar(), ((CharAppearance)feat.getAppearance()).getColor(), feat.getDescription()));
								li = (BasicListItem)sightListItems.get(feat.getID());
							}
							if (feat.isRelevant() && !featuresOnSight.contains(li)) 
									featuresOnSight.add(li);
							CharAppearance featApp = (CharAppearance)feat.getAppearance();
							si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, featApp.getChar(), featApp.getColor());
						}
					}
					
					SmartFeature sfeat = level.getSmartFeature(runner);
					if (sfeat != null){
						if (sfeat.isVisible()){
							CharAppearance featApp = 
								(CharAppearance)sfeat.getAppearance();
							si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, featApp.getChar(), featApp.getColor());
						}
					}
					
					Vector items = level.getItemsAt(runner);
					Item item = null;
					if (items != null){
						item = (Item) items.elementAt(0);
					}
					if (item != null){
						if (item.isVisible()){
							CharAppearance itemApp = (CharAppearance)item.getAppearance();
							si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, itemApp.getChar(), itemApp.getColor());
							BasicListItem li = (BasicListItem)sightListItems.get(item.getDefinition().getID());
							if (li == null){
								//Debug.say("Adding "+item.getDefinition().getID()+" to the hashtable");
								sightListItems.put(item.getDefinition().getID(), new BasicListItem(((CharAppearance)item.getAppearance()).getChar(), ((CharAppearance)item.getAppearance()).getColor(), item.getDefinition().getDescription()));
								li = (BasicListItem)sightListItems.get(item.getDefinition().getID());
							}
							if (!itemsOnSight.contains(li))
								itemsOnSight.add(li);
						}
					}
					
					Monster monster = level.getMonsterAt(runner);
					if (monster != null && monster.isVisible()){
						BasicListItem li = null;
						
							li = (BasicListItem)sightListItems.get(monster.getID());
							if (li == null){
								CharAppearance monsterApp = (CharAppearance)monster.getAppearance();
								Debug.say("Adding "+monster.getID()+" to the hashtable");
								sightListItems.put(monster.getID(), new BasicListItem(monsterApp.getChar(), monsterApp.getColor(), monster.getDescription()));
								li = (BasicListItem)sightListItems.get(monster.getID());
							}
						
						if (!monstersOnSight.contains(li))
							monstersOnSight.add(li);
						
						CharAppearance monsterApp = (CharAppearance) monster.getAppearance();
						if (monster.isFrozen())
							si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, monsterApp.getChar(), ConsoleSystemInterface.CYAN);
						else
							si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, monsterApp.getChar(), monsterApp.getColor());
					}
					
					if (!player.isInvisible()){
						si.print(PC_POS.x,PC_POS.y, ((CharAppearance)player.getAppearance()).getChar(), ((CharAppearance)player.getAppearance()).getColor());
					}

					
				}
				runner.y++;
			}
			runner.y = player.getPosition().y-yrange;
			runner.x ++;
		}
		
		/*monstersList.clear();
		itemsList.clear();*/
		idList.clear(); 
		idList.addElements(monstersOnSight);
		idList.addElements(itemsOnSight);
		idList.addElements(featuresOnSight);

		Debug.exitMethod();
	}
	
	private Vector messageHistory = new Vector(20,10);
	public void addMessage(Message message){
		Debug.enterMethod(this, "addMessage", message);
		if (eraseOnArrival){
	 		messageBox.clear();
	 		messageBox.setForeColor(ConsoleSystemInterface.BLUE);
	 		eraseOnArrival = false;
		}
		if ((player != null && player.getPosition() != null && message.getLocation().z != player.getPosition().z) || (message.getLocation() != null && !insideViewPort(getAbsolutePosition(message.getLocation())))){
			Debug.exitMethod();
			return;
		}
		messageHistory.add(message.getText());
		if (messageHistory.size()>100)
			messageHistory.removeElementAt(0);
		messageBox.addText(message.getText());
		
		messageBox.draw();
		Debug.exitMethod();
		
	}

	String[] statusBuffer = new String[11];
	static String[] SPACES = new String[]{
		"",
		" ",
		"  ",
		"   ",
		"    ",
		"     ",
		"      ",
		"       ",
		"        ",
		"         ",
		"          ",
		"           ",
		"            ",
		"             ",
		"              ",
		"               ",
		"                " ,
		"                 ",
		"                  ",
		"                   ",
		"                    " ,
		"                     ",
	};
    private void drawPlayerStatus(){
    	statusBuffer[0] = "HEALTH: "+player.getIntegrityPoints()+"/"+player.getIntegrityMax();
    	statusBuffer[1] = "EVADE:  "+player.getEvadePoints()+"/"+player.getEvadePointsMax()+" ("+player.getEvasion()+"%"+")";
    	statusBuffer[2] = "MANA:   "+player.getMana()+"/"+player.getManaMax();
    	statusBuffer[3] = "SCORE:  "+player.getScore();
    	if (player.getArmor()!= null){
    		statusBuffer[4] = player.getArmor().getDescription();
    		statusBuffer[5] = "INT:"+player.getArmor().getIntegrity()+" COV:"+player.getArmor().getCoverage()+"%";
    	}
    	if (player.getWeapon()!= null){
    		statusBuffer[6] = player.getWeapon().getDescription();
    		statusBuffer[7] = "ATK:"+(player.getWeapon().getAttack()+player.getAttack())+" BRK:"+(player.getWeapon().getEvadeBreak()+player.getEvadeBreak());
    	} else {
    		statusBuffer[6] = "BARE HANDS";
    		statusBuffer[7] = "ATK:"+player.getAttack()+" BRK:"+player.getEvadeBreak();
    	}
    	statusBuffer[8] = "GOLD: "+player.getGold();
    	statusBuffer[9] = player.getName();
    	String tactic = player.getCurrentTactic() == Player.TACTIC_NORMAL ? "": "(Brave)";
    	statusBuffer[10] = player.getStatusString()+tactic;
    	
    	for (int i = 0; i < statusBuffer.length; i++){
    		if (statusBuffer[i].length() < 20){
    			int spaces = 20 - statusBuffer[i].length();
    			statusBuffer[i] = statusBuffer[i] + SPACES[spaces];
    		}
        	else
        		statusBuffer[i] = statusBuffer[i].substring(0,20);
    		si.print(59,2+i, statusBuffer[i]);
    	}
    	
    	si.print(20,20, "                  ");
    	String description = player.getLevel().getDescription();
    	if (description.length() < 18)
    		si.print(20,20, description);
    	else
    		si.print(20,20, description.substring(0,17));
 		Debug.exitMethod();
    }

	public void init(ConsoleSystemInterface psi, UserAction[] gameActions, UserCommand[] gameCommands,
		Action advance, Action target, Action attack){
		Debug.enterMethod(this, "init");
		super.init(gameActions, gameCommands, advance, target, attack);
		selector = new UISelector();
		messageBox = new TextInformBox(psi);
		idList = new ListBox(psi);
		
		messageBox.setPosition(0,21);
		messageBox.setWidth(80);
		messageBox.setHeight(3);
		messageBox.setForeColor(ConsoleSystemInterface.LEMON);
		
		/*monstersList.setPosition(2, 4);
		monstersList.setWidth(27);
		monstersList.setHeight(10);*/
		
		idList.setPosition(59,13);
		idList.setWidth(20);
		idList.setHeight(11);
		si = psi;
		FOVMask = new boolean[80][25];
		for (int x = 0; x < 80; x++)
			for (int y = 0; y < 25; y++)
				FOVMask[x][y]=false;
		Debug.exitMethod();
	}

	/** 
	 * Checks if the point, relative to the console coordinates, is inside the
	 * ViewPort 
	 */
	public boolean insideViewPort(int x, int y){
    	//return (x>=VP_START.x && x <= VP_END.x && y >= VP_START.y && y <= VP_END.y);
		//return (x>=0 && x < FOVMask.length && y >= 0 && y < FOVMask[0].length) && FOVMask[x][y];
		return (x>=VP_START.x && x <= VP_END.x && y >= VP_START.y && y <= VP_END.y) && FOVMask[x][y];
    }

	public boolean insideViewPort(Position what){
    	return insideViewPort(what.x, what.y);
    }

	public boolean isDisplaying(Actor who){
    	return insideViewPort(getAbsolutePosition(who.getPosition()));
    }

    public Position pickPosition(String prompt) throws ActionCancelException{
    	Debug.enterMethod(this, "pickPosition");
    	messageBox.setForeColor(ConsoleSystemInterface.BLUE);
    	messageBox.setText(prompt);
		messageBox.draw();
		si.refresh();
		si.saveBuffer();
		
		Position defaultTarget = null; 
   		Position nearest = getNearestMonsterPosition();
   		if (nearest != null){
   			defaultTarget = nearest;
   		} else {
   			defaultTarget = null;
   		}
    	
    	Position browser = null;
    	Position offset = new Position (0,0);
    	if (lockedMonster != null){
			if (!player.sees(lockedMonster)  || lockedMonster.isDead())
				lockedMonster = null;
			else
				defaultTarget = new Position(lockedMonster.getPosition());
		}
    	if (!insideViewPort(PC_POS.x + offset.x,PC_POS.y + offset.y)){
    		offset = new Position (0,0);
    	}
    	
    	if (defaultTarget == null) {
    		offset = new Position (0,0);
    	} else{
			offset = new Position(defaultTarget.x - player.getPosition().x, defaultTarget.y - player.getPosition().y);
		}
		
		while (true){
			si.restore();
			String looked = "";
			browser = Position.add(player.getPosition(), offset);
			
			/*if (PC_POS.x + offset.x < 0 || PC_POS.x + offset.x >= FOVMask.length || PC_POS.y + offset.y < 0 || PC_POS.y + offset.y >=FOVMask[0].length){
				offset = new Position (0,0);
				browser = Position.add(player.getPosition(), offset);
			}*/
				
			if (FOVMask[PC_POS.x + offset.x][PC_POS.y + offset.y]){
				Cell choosen = level.getMapCell(browser);
				Feature feat = level.getFeatureAt(browser);
				Vector items = level.getItemsAt(browser);
				Item item = null;
				if (items != null) {
					item = (Item) items.elementAt(0);
				}
				Actor actor = level.getActorAt(browser);
				si.restore();
				
				if (choosen != null)
					looked += choosen.getDescription();
				if (level.getBloodAt(browser) != null)
				    looked += "{bloody}";
				if (feat != null)
					looked += ", "+ feat.getDescription();
				if (actor != null)
					looked += ", "+ actor.getDescription();
				if (item != null)
					looked += ", "+ item.getDescription();
			}
			messageBox.setText(prompt+" "+looked);
			messageBox.draw();
			//si.print(PC_POS.x + offset.x, PC_POS.y + offset.y, '_', ConsoleSystemInterface.BLUE);
			drawLineTo(PC_POS.x + offset.x, PC_POS.y + offset.y, '*', ConsoleSystemInterface.DARK_BLUE);
			si.print(PC_POS.x + offset.x, PC_POS.y + offset.y, 'X', ConsoleSystemInterface.BLUE);
			si.refresh();
			CharKey x = new CharKey(CharKey.NONE);
			while (x.code != CharKey.SPACE && x.code != CharKey.ESC &&
				   ! x.isArrow())
				x = si.inkey();
			if (x.code == CharKey.ESC){
				si.restore();
				throw new ActionCancelException();
			} 
			if (x.code == CharKey.SPACE){
				si.restore();
				if (level.getMonsterAt(browser) != null)
					lockedMonster = level.getMonsterAt(browser);
				return browser;
			}
			offset.add(Action.directionToVariation(Action.toIntDirection(x)));

			if (offset.x >= xrange) offset.x = xrange;
			if (offset.x <= -xrange) offset.x = -xrange;
			if (offset.y >= yrange) offset.y = yrange;
			if (offset.y <= -yrange) offset.y = -yrange;
     	}
		
		
    }

	public int pickDirection(String prompt) throws ActionCancelException{
		Debug.enterMethod(this, "pickDirection");
		//refresh();
		messageBox.setText(prompt);
		messageBox.draw();
		si.refresh();
		//refresh();

		CharKey x = new CharKey(CharKey.NONE);
		while (x.code == CharKey.NONE)
			x = si.inkey();
		if (x.isArrow() || x.code == CharKey.N5){
			int ret = Action.toIntDirection(x);
        	Debug.exitMethod(ret);
        	return ret;
		} else {
			ActionCancelException ret = new ActionCancelException(); 
			Debug.exitExceptionally(ret);
			si.refresh();
        	throw ret; 
		}
	}

	public Item pickEquipedItem(String prompt) throws ActionCancelException{
		Debug.enterMethod(this, "pickEquipedItem");
  		Vector equipped = new Vector();
  		if (player.getArmor() != null)
  			equipped.add(player.getArmor());
  		if (player.getWeapon() != null)
  			equipped.add(player.getWeapon());
  		if (player.getAccesory() != null)
  			equipped.add(player.getAccesory());
  		if (player.getSecondaryWeapon() != null)
  			equipped.add(player.getSecondaryWeapon());
  		MenuBox menuBox = new MenuBox(si);
  		//menuBox.setBounds(26,6,30,11);
  		menuBox.setBounds(10,3,60,18);
  		menuBox.setPromptSize(2);
  		menuBox.setMenuItems(equipped);
  		menuBox.setPrompt(prompt);
  		menuBox.setForeColor(ConsoleSystemInterface.BLUE);
  		menuBox.setBorder(true);
  		si.saveBuffer();
  		menuBox.draw();
		Item equiped = (Item)menuBox.getSelection();
		si.restore();
		if (equiped == null){
			ActionCancelException ret = new ActionCancelException();
			Debug.exitExceptionally(ret);
			throw ret;
		}
		return equiped;
	}
	
	public Item pickItem(String prompt) throws ActionCancelException{
		Debug.enterMethod(this, "pickItem");
  		Vector inventory = player.getInventory();
  		MenuBox menuBox = new MenuBox(si);
  		menuBox.setBounds(10,3,60,18);
  		menuBox.setPromptSize(2);
  		menuBox.setMenuItems(inventory);
  		menuBox.setPrompt(prompt);
  		menuBox.setForeColor(ConsoleSystemInterface.BLUE);
  		menuBox.setBorder(true);
  		si.saveBuffer();
  		menuBox.draw();
		Equipment equipment = (Equipment)menuBox.getSelection();
		si.restore();
		if (equipment == null){
			ActionCancelException ret = new ActionCancelException();
			Debug.exitExceptionally(ret);
			throw ret;
		}
		return equipment.getItem();
	}
	
	public Item pickUnderlyingItem(String prompt) throws ActionCancelException{
		Debug.enterMethod(this, "pickUnderlyingItem");
  		Vector items = level.getItemsAt(player.getPosition());
  		if (items == null)
  			return null;
  		if (items.size() == 1)
  			return (Item) items.elementAt(0);
  		MenuBox menuBox = new MenuBox(si);
  		menuBox.setBounds(10,3,60,18);
  		menuBox.setPromptSize(2);
  		menuBox.setMenuItems(items);
  		menuBox.setPrompt(prompt);
  		menuBox.setForeColor(ConsoleSystemInterface.BLUE);
  		menuBox.setBorder(true);
  		si.saveBuffer();
  		menuBox.draw();
		Item item = (Item)menuBox.getSelection();
		si.restore();
		if (item == null){
			ActionCancelException ret = new ActionCancelException();
			Debug.exitExceptionally(ret);
			throw ret;
		}
		return item;
	}
	
	private Vector pickMultiItems(String prompt) {
		Equipment.eqMode = true;
		Vector inventory = player.getInventory();
  		MenuBox menuBox = new MenuBox(si);
  		menuBox.setBounds(25,3,40,18);
  		menuBox.setPromptSize(2);
  		menuBox.setMenuItems(inventory);
  		menuBox.setPrompt(prompt);
  		menuBox.setForeColor(ConsoleSystemInterface.BLUE);
  		menuBox.setBorder(true);
  		Vector ret = new Vector();
  		MenuBox selectedBox = new MenuBox(si);
  		selectedBox.setBounds(5,3,20,18);
  		selectedBox.setPromptSize(2);
  		selectedBox.setPrompt("Selected Items");
  		selectedBox.setMenuItems(ret);
  		selectedBox.setForeColor(ConsoleSystemInterface.BLUE);
  		selectedBox.setBorder(true);
  		
  		si.saveBuffer();
  		
		while (true){
			selectedBox.draw();
			menuBox.draw();
			
	  		
			Equipment equipment = (Equipment)menuBox.getSelection();
			if (equipment == null)
				break;
			if (!ret.contains(equipment.getItem()))
				ret.add(equipment.getItem());
		}
		si.restore();
		Equipment.eqMode = false;
		return ret;
	}
	
	private Vector pickSpirits() {
		Vector originalInventory = player.getInventory();
		Vector inventory = new Vector();
		for (int i = 0; i < originalInventory.size(); i++){
			Equipment testEq = (Equipment) originalInventory.elementAt(i);
			if (testEq.getItem().getDefinition().getID().endsWith("_SPIRIT")){
				inventory.add(testEq);
			}
		}
		
  		MenuBox menuBox = new MenuBox(si);
  		menuBox.setBounds(25,3,40,18);
  		menuBox.setPromptSize(2);
  		menuBox.setMenuItems(inventory);
  		menuBox.setPrompt("Select the spirits to fusion");
  		menuBox.setForeColor(ConsoleSystemInterface.BLUE);
  		menuBox.setBorder(true);
  		
  		Vector ret = new Vector();
  		MenuBox selectedBox = new MenuBox(si);
  		selectedBox.setBounds(5,3,20,18);
  		selectedBox.setPromptSize(2);
  		selectedBox.setPrompt("Selected Spirits");
  		selectedBox.setMenuItems(ret);
  		selectedBox.setForeColor(ConsoleSystemInterface.BLUE);
  		selectedBox.setBorder(true);
  		
  		si.saveBuffer();
  		
		while (true){
			selectedBox.draw();
			menuBox.draw();
	  		
			Equipment equipment = (Equipment)menuBox.getSelection();
			if (equipment == null)
				break;
			if (!ret.contains(equipment.getItem()))
				ret.add(equipment.getItem());
		}
		si.restore();
		return ret;
	}

	public void processQuit(){
		messageBox.setForeColor(ConsoleSystemInterface.BLUE);
		messageBox.setText(quitMessages[Util.rand(0, quitMessages.length-1)]+" (y/n)");
		messageBox.draw();
		si.refresh();
		if (prompt()){
			messageBox.setText("Go away, and let the world flood in darkness... [Press Space to continue]");
			messageBox.draw();
			si.refresh();
			si.waitKey(CharKey.SPACE);
			player.getGameSessionInfo().setDeathCause(GameSessionInfo.QUIT);
			player.getGameSessionInfo().setDeathLevel(level.getLevelNumber());
			informPlayerCommand(CommandListener.QUIT);
		}
		messageBox.draw();
		messageBox.clear();
		si.refresh();
	}
	
	public void processSave(){
		if (!player.getGame().canSave()){
			level.addMessage("You cannot save your game here!");
			return;
		}
		messageBox.setForeColor(ConsoleSystemInterface.BLUE);
		messageBox.setText("Save your game? (y/n)");
		messageBox.draw();
		si.refresh();
		if (prompt()){
			messageBox.setText("Saving... I will await your return.. [Press Space to continue]");
			messageBox.draw();
			si.refresh();
			si.waitKey(CharKey.SPACE);
			informPlayerCommand(CommandListener.SAVE);
		}
		messageBox.draw();
		messageBox.clear();
		si.refresh();
	}

	public boolean prompt (){
		CharKey x = new CharKey(CharKey.NONE);
		while (x.code != CharKey.Y && x.code != CharKey.y && x.code != CharKey.N && x.code != CharKey.n)
			x = si.inkey();
		return (x.code == CharKey.Y || x.code == CharKey.y);
	}

	public void refresh(){
		//cleanViewPort();
		drawPlayerStatus();
	 	drawLevel();
		idList.draw();
		si.refresh();
		messageBox.draw();
	  	messageBox.setForeColor(ConsoleSystemInterface.DARK_BLUE);
	 	eraseOnArrival = true;
	  	
    }

	private void setTargets(Action a) throws ActionCancelException{
		if (a.needsItem())
			a.setItem(pickItem(a.getPromptItem()));
		if (a.needsDirection()){
			a.setDirection(pickDirection(a.getPromptDirection()));
		}
		if (a.needsPosition()){
			a.setPosition(pickPosition(a.getPromptPosition()));
		}
		if (a.needsEquipedItem())
			a.setEquipedItem(pickEquipedItem(a.getPromptEquipedItem()));
		if (a.needsMultiItems()){
			a.setMultiItems(pickMultiItems(a.getPromptMultiItems()));
		}
		if (a.needsSpirits()){
			a.setMultiItems(pickSpirits());
		}
		if (a.needsUnderlyingItem()){
			a.setItem(pickUnderlyingItem(a.getPrompUnderlyingItem()));
		}
	}
	
	public Action showInventory() throws ActionCancelException {
		Equipment.eqMode = true;
  		Vector inventory = player.getInventory();
		int xpos = 1, ypos = 0;
  		MenuBox menuBox = new MenuBox(si);
  		menuBox.setHeight(11);
  		menuBox.setWidth(70);
  		menuBox.setPosition(1,8);
  		menuBox.setBorder(false);
  		menuBox.setMenuItems(inventory);
  		

  		si.saveBuffer();
  		si.cls();
  		si.print(xpos,ypos,  "-----------------------------------------------", ConsoleSystemInterface.DARK_BLUE);
  		si.print(xpos,ypos+1,  "Inventory", ConsoleSystemInterface.BLUE);
  		si.print(xpos,ypos+2,  "-----------------------------------------------", ConsoleSystemInterface.DARK_BLUE);
 		si.print(xpos+2,ypos+3,  "Weapons:   "+player.getEquipedWeaponDescription());
 		si.print(xpos+2,ypos+4,  "           "+player.getSecondaryWeaponDescription());
 		si.print(xpos+2,ypos+5,  "Armor:    "+player.getArmorDescription());
 		si.print(xpos+2,ypos+6,  "Accessory:"+player.getAccDescription());
 		si.print(xpos,ypos+7,  "-----------------------------------------------", ConsoleSystemInterface.DARK_BLUE);
 		menuBox.draw();
 		si.print(xpos,24,  "[Space] to continue, Up and Down to browse");
 		si.refresh();
		Object dummy = menuBox.getSelection();

		
//		si.waitKey(CharKey.SPACE);
		si.restore();
		Equipment.eqMode = false;
		return null;
	}


 	/**
     * Shows a message inmediately; useful for system
     * messages.
     *  
     * @param x the message to be shown
     */
	public void showMessage(String x){
		messageBox.setForeColor(ConsoleSystemInterface.BLUE);
		messageBox.addText(x);
		messageBox.draw();
		si.refresh();
	}
	
	public void showSystemMessage(String x){
		messageBox.setForeColor(ConsoleSystemInterface.BLUE);
		messageBox.setText(x);
		messageBox.draw();
		si.refresh();
		si.waitKey(CharKey.SPACE);
	}
	
	
	public void showMessageHistory(){
		si.saveBuffer();
		si.cls();
		si.print(1, 0, "Message Buffer", CharAppearance.DARK_BLUE);
		for (int i = 0; i < 22; i++){
			if (i >= messageHistory.size())
				break;
			si.print(1,i+2, (String)messageHistory.elementAt(messageHistory.size()-1-i), CharAppearance.BLUE);
		}
		
		si.print(55, 24, "[ Space to Continue ]");
		si.waitKey(CharKey.SPACE);
		si.restore();
	}

	public void showPlayerStats (){
		si.saveBuffer();
		si.cls();
		si.print(3,3, player.getName()+" "+player.getStatusString()+" Gold: "+player.getGold()+" Score: "+player.getScore(), ConsoleSystemInterface.WHITE);
	    si.print(3,4, "Sex: "+ (player.getSex() == Player.MALE ? "M" : "F"), ConsoleSystemInterface.WHITE);
	    si.print(3,5, " Integrity: "+player.getIntegrityPoints()+ "/"+player.getIntegrityMax(), ConsoleSystemInterface.WHITE);
	    si.print(3,6, " Break Points: "+player.getEvadePoints()+ "/"+player.getEvadePointsMax(), ConsoleSystemInterface.WHITE);
	    si.print(3,7, " Mana: "+player.getMana()+ "/"+player.getManaMax(), ConsoleSystemInterface.WHITE);
	    si.print(3,8, "Carrying: "+player.getItemCount()+"/"+player.getCarryMax(), ConsoleSystemInterface.WHITE);
	    si.print(3,9, "Attack: +"+player.getBaseAttack()+" (+"+(player.getAttack()+(player.getWeapon() != null ? player.getWeapon().getAttack() : 0))+")", ConsoleSystemInterface.WHITE);
	    si.print(3,10, "Break: +"+player.getBaseEvadeBreak()+" (+"+(player.getEvadeBreak()+(player.getWeapon() != null ? player.getWeapon().getEvadeBreak() : 0))+")", ConsoleSystemInterface.WHITE);
	    si.print(3,11, "Soul: +"+player.getBaseSoulPower()+" (+"+player.getSoulPower()+")", ConsoleSystemInterface.WHITE);
	    si.print(3,12, "Evasion: "+player.getBaseEvasion()+"%"+" ("+player.getEvasion()+"%)", ConsoleSystemInterface.WHITE);
	    si.print(3,13, "Sight: "+player.getBaseSightRange()+" ("+player.getSightRange()+")", ConsoleSystemInterface.WHITE);
	    si.print(3,14, "Speed: "+((player.getBaseWalkCost()-50)*-1)+ " ("+((player.getWalkCost()-50)*-1)+")", ConsoleSystemInterface.WHITE);
	    si.print(3,15, "Combat: "+((player.getBaseAttackCost()-50)*-1)+ " ("+((player.getAttackCost()-50)*-1)+")", ConsoleSystemInterface.WHITE);
	    si.print(3,16, "Magic: "+((player.getBaseCastCost()-50)*-1)+ " ("+((player.getCastCost()-50)*-1)+")", ConsoleSystemInterface.WHITE);
	    
	    si.print(1,22, "[ Press Space to continue ]");
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.restore();
	}

	private void examineLevelMap(){
		si.saveBuffer();
		si.cls();
		int lw = level.getWidth();
		int lh = level.getHeight();
		int remnantx = (int)((80 - (lw * 5))/2.0d); 
		if (remnantx < 0)
			remnantx = 0;
		int cellColor = 0;char cellChar = ' ';
		int w = (int)((lh-1) /25.0D)+1;
		for (int i = 0; i < w; i++){
			for (int x = 0; x < level.getWidth(); x++){
				if (x > 79) continue;
				int limit = (i+1)*25;
				if (limit > level.getHeight())
					limit = level.getHeight();
				int scry = 0;
				for (int y = i*24; y < limit; y++, scry++){
					if (!level.remembers(x,y)){
						cellColor = ConsoleSystemInterface.BLACK;
						cellChar = ' ';
					}
					else {
						Cell current = level.getMapCell(x, y, 0);
						Feature currentF = level.getFeatureAt(x,y,0);
						if (level.isVisible(x,y)){
							if (current.isSolid()){
								cellColor = ConsoleSystemInterface.BLUE;
								cellChar = '#';
							}
							else if (current.getID().startsWith("DDOOR")){
								cellColor = ConsoleSystemInterface.CYAN;
								cellChar = '*';
							}
							else {
								cellColor = ConsoleSystemInterface.LIGHT_GRAY;
								cellChar = '.';
							}
							if (currentF != null){
								if (currentF.getID().equals("RED_POOL")){
									cellColor = ConsoleSystemInterface.RED;
									cellChar = '~';
								} else if (currentF.getID().equals("RED_WATER")){
									cellColor = ConsoleSystemInterface.DARK_RED;
									cellChar = '~';
								} else if (currentF.getID().equals("YELLOW_POOL") ){
									cellColor = ConsoleSystemInterface.YELLOW;
									cellChar = '~';
								} else if (currentF.getID().equals("YELLOW_WATER")){
									cellColor = ConsoleSystemInterface.BROWN;
									cellChar = '~';
								} else if (currentF.getID().equals("FORGE")){
									cellColor = ConsoleSystemInterface.PURPLE;
									cellChar = '^';
								} else if (currentF.getID().equals("ANKH")){
									cellColor = ConsoleSystemInterface.WHITE;
									cellChar = '^';
								}
							}
						} else {
							if (current.isSolid()){
								cellColor = ConsoleSystemInterface.BLUE;
								cellChar = '#';
							}
							else if (current.getID().startsWith("DDOOR")){
								cellColor = ConsoleSystemInterface.MAGENTA;
								cellChar = '*';
							}
							else{ 
								cellColor = ConsoleSystemInterface.GRAY;
								cellChar = '.';
							}
							if (currentF != null){
								if (currentF.getID().equals("RED_POOL")){
									cellColor = ConsoleSystemInterface.RED;
									cellChar = '~';
								} else if (currentF.getID().equals("RED_WATER")){
									cellColor = ConsoleSystemInterface.DARK_RED;
									cellChar = '~';
								} else if (currentF.getID().equals("YELLOW_POOL") ){
									cellColor = ConsoleSystemInterface.YELLOW;
									cellChar = '~';
								} else if (currentF.getID().equals("YELLOW_WATER")){
									cellColor = ConsoleSystemInterface.BROWN;
									cellChar = '~';
								} else if (currentF.getID().equals("FORGE")){
									cellColor = ConsoleSystemInterface.PURPLE;
									cellChar = '^';
								} else if (currentF.getID().equals("ANKH")){
									cellColor = ConsoleSystemInterface.WHITE;
									cellChar = '^';
								}
							}
						}
						if (player.getPosition().x == x && player.getPosition().y == y){
							cellColor = ConsoleSystemInterface.RED;
							cellChar = '@';
						}
					}
					si.print(remnantx+x, scry, cellChar, cellColor);
				}
			}
			si.refresh();	
			si.waitKey(CharKey.SPACE);
			si.cls();
		}
		
		
		si.restore();
		si.refresh();
		
	}
	
    public Action showSkills() throws ActionCancelException {
    	Debug.enterMethod(this, "showSkills");
    	si.saveBuffer();
		Vector skills = player.getAvailableSkills();
  		MenuBox menuBox = new MenuBox(si);
  		menuBox.setHeight(14);
  		menuBox.setWidth(33);
  		menuBox.setBorder(true);
  		menuBox.setForeColor(ConsoleSystemInterface.BLUE);
  		menuBox.setPosition(24,4);
  		menuBox.setMenuItems(skills);
  		menuBox.setTitle("Skills");
  		menuBox.setPromptSize(0);
  		menuBox.draw();
		si.refresh();
        Skill selectedSkill = (Skill)menuBox.getSelection();
        if (selectedSkill == null){
        	si.restore();
        	Debug.exitMethod("null");
        	return null;
        }
        si.restore();
        if (selectedSkill.isSymbolic()){
        	Debug.exitMethod("null");
        	return null;
        }
        	
		Action selectedAction = selectedSkill.getAction();
		selectedAction.setPerformer(player);
		if (selectedAction.canPerform(player))
			setTargets(selectedAction);
		else
			level.addMessage(selectedAction.getInvalidationMessage());
		
		//si.refresh();
		Debug.exitMethod(selectedAction);
		return selectedAction;
	}

    private final String[] SOUL_OPTIONS = new String[]{
    		"VENUS_SPIRIT",
    		"MERCURY_SPIRIT",
    		"MARS_SPIRIT",
    		"URANUS_SPIRIT",
    		"NEPTUNE_SPIRIT",
    		"JUPITER_SPIRIT"
    };
    
    

    private Action selectCommand (CharKey input){
		Debug.enterMethod(this, "selectCommand", input);
		int com = getRelatedCommand(input.code);
		informPlayerCommand(com);
		Action ret = actionSelectedByCommand;
		actionSelectedByCommand = null;
		Debug.exitMethod(ret);
		return ret;
	}
	
	public void commandSelected (int commandCode){
		switch (commandCode){
			case CommandListener.PROMPTQUIT:
				processQuit();
				break;
			case CommandListener.PROMPTSAVE:
				processSave();
				break;
			case CommandListener.HELP:
				si.saveBuffer();
				Display.thus.showHelp();
				si.restore();
				break;
			case CommandListener.LOOK:
				doLook();
				break;
			case CommandListener.EXAMINELEVELMAP:
				examineLevelMap();
				break;
			case CommandListener.SHOWSTATS:
				showPlayerStats();
				break;
			case CommandListener.SHOWINVEN:
				try {
					actionSelectedByCommand = showInventory();
				} catch (ActionCancelException ace){

				}
				break;
			case CommandListener.SHOWSKILLS:
				try {
					actionSelectedByCommand = showSkills();
				} catch (ActionCancelException ace){

				}
				break;
			case CommandListener.SHOWMESSAGEHISTORY:
				showMessageHistory();
				break;
			case CommandListener.CHANGE_TACTICS:
				player.changeTactic();
				break;			
			case CommandListener.CHAR_DUMP:
				showMessage("Character File Dumped.");
				GameFiles.saveChardump(player);
				break;
			case CommandListener.SWITCHMUSIC:
				boolean enabled = STMusicManagerNew.thus.isEnabled();
				if (enabled){
					showMessage("Turn off music");
					STMusicManagerNew.thus.stopMusic();
					STMusicManagerNew.thus.setEnabled(false);
				} else {
					showMessage("Turn on music");
					STMusicManagerNew.thus.setEnabled(true);
					if (!level.isDay() && level.hasNoonMusic())
						STMusicManagerNew.thus.playKey(level.getMusicKeyNoon());
					else
						STMusicManagerNew.thus.playKey(level.getMusicKeyMorning());
				}
				break;
		}
	}
/*
	private boolean cheatConsole(CharKey input){
		switch (input.code){
		case CharKey.F2:
			//player.addHearts(5);
			break;
		case CharKey.F3:
			player.setInvincible(250);
			break;
		case CharKey.F4:
			player.informPlayerEvent(Player.EVT_NEXT_LEVEL);
			break;
		case CharKey.F5:
			//player.heal();
			break;
		case CharKey.F6:
			
			break;
		case CharKey.F7:
			player.getLevel().setIsDay(!player.getLevel().isDay());
			break;
		case CharKey.F8:
			player.informPlayerEvent(Player.EVT_BACK_LEVEL);
			break;
		default:
			return false;
		}
		return true;
	}
	*/
//	Runnable interface
	public void run (){}
	
//	IO Utility    
	public void waitKey (){
		CharKey x = new CharKey(CharKey.NONE);
		while (x.code == CharKey.NONE)
			x = si.inkey();
	}

	private void cleanViewPort(){
	    Debug.enterMethod(this, "cleanViewPort");
    	String spaces = "";
    	for (int i= 0; i<= VP_END.x - VP_START.x; i++){
    		spaces+=" ";
    	}
    	for (int i= VP_START.y; i<= VP_END.y; i++){
    		si.print(VP_START.x, i,spaces);
    	}
    	Debug.exitMethod();
	}

	private void drawLineTo(int x, int y, char icon, int color){
		Position target = new Position(x,y);
		Line line = new Line(PC_POS, target);
		Position tmp = line.next();
		while (!tmp.equals(target)){
			tmp = line.next();
			si.print(tmp.x, tmp.y, icon, color);
		}
		
	}
	
	private Position getNearestMonsterPosition(){
		VMonster monsters = level.getMonsters();
		Monster nearMonster = null;
		int minDist = 150;
		int maxDist = 15;
		for (int i = 0; i < monsters.size(); i++){
			Monster monster = (Monster) monsters.elementAt(i);
			if (monster.getPosition().z() != level.getPlayer().getPosition().z())
				continue;
			int distance = Position.flatDistance(level.getPlayer().getPosition(), monster.getPosition());
			if (distance < maxDist && distance< minDist && player.sees(monster)){
				minDist = distance;
				nearMonster = monster;
			}
		}
		if (nearMonster != null)
			return nearMonster.getPosition();
		else
			return null;
	}

	public Vector getMessageBuffer() {
		//return new Vector(messageHistory.subList(0,21));
		return new Vector(messageHistory.subList(messageHistory.size()-21,messageHistory.size()));
	}
	public ActionSelector getSelector(){
		return selector;
	}
	
	class UISelector implements ActionSelector {
		/** 
		 * Returns the Action that the player wants to perform.
	     * It may also forward a command instead
	     * 
	     */
		public Action selectAction(Actor who){
	    	Debug.enterMethod(this, "selectAction", who);
		    CharKey input = null;
		    Action ret = null;
		    while (ret == null){
		    	if (gameOver)
		    		return null;
				input = si.inkey();
				ret = selectCommand(input);
				if (ret != null)
					return ret;
				if (input.code == CharKey.DOT || input.code == CharKey.DELETE) {
					Debug.exitMethod("null");
					((Player)who).doNothing();
					return null;
				}
				/*if (cheatConsole(input)){
					continue;
				}*/
				if (input.isArrow()){
					int direction = Action.toIntDirection(input);
					Monster vMonster = player.getLevel().getMonsterAt(Position.add(player.getPosition(), Action.directionToVariation(direction)));
					if (vMonster != null ){
						attack.setDirection(direction);
						Debug.exitMethod(attack);
						return attack;
					}
					advance.setDirection(direction);
					Debug.exitMethod(advance);
					return advance;
				} else
				if (input.code == WEAPONCODE){
					
				}else{
	            	ret = getRelatedAction(input.code);
	            	try {
	            		Position nearest =getNearestMonsterPosition(); 
	                	if (ret != null){
		            		ret.setPerformer(player);
		            		if (ret.canPerform(player))
		            			setTargets(ret);
		            		else
		            			level.addMessage(ret.getInvalidationMessage());
	                     	Debug.exitMethod(ret);
	                    	return ret;
						}

					}
					catch (ActionCancelException ace){
						//si.cls();
		 				//refresh();
		 				addMessage(new Message("Cancelled", player.getPosition()));
						ret = null;
					}
					//refresh();
				}
			}
			Debug.exitMethod("null");
			return null;
		}
		
		public String getID(){
			return "UI";
		}
	    
		public ActionSelector derive(){
	 		return null;
	 	}
	}
}