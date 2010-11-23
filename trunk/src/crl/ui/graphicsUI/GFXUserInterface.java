package crl.ui.graphicsUI;


import java.awt.*;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import sz.gadgets.MenuBox;
import sz.util.*;
import crl.action.Action;
//import crl.action.vkiller.Whip;
import crl.ui.consoleUI.CharAppearance;
import crl.ui.consoleUI.effects.CharEffect;
import crl.ui.effects.*;
import crl.ui.graphicsUI.components.GFXButton;
import crl.ui.graphicsUI.components.GFXInventoryBox;
import crl.ui.graphicsUI.components.GFXMenuBox;
import crl.ui.graphicsUI.effects.GFXEffect;
import crl.player.*;
import crl.item.*;
import crl.level.*;
import crl.monster.*;
import crl.feature.*;
import crl.game.Game;
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

public class GFXUserInterface extends UserInterface implements CommandListener, Runnable{
	//Properties
	private int xrange;
	private int yrange;
	public static int UPLEFTBORDER;
	private Font FNT_MESSAGEBOX;
	private BufferedImage 
		IMG_STATUSSCR_BGROUND, 
		TILE_LINE_STEPS, 
		TILE_LINE_AIM,
		TILE_SCAN,
		IMG_ICON;
	public Position PC_POS;
	private Color COLOR_REDPOOL_MINIMAP;
    private Color COLOR_REDFOUNTAIN_MINIMAP;
    private Color COLOR_YELLOWPOOL_MINIMAP;
    private Color COLOR_YELLOWFOUNTAIN_MINIMAP;
    private Color COLOR_INVENTORY_BACKGROUND;
	private Color COLOR_INVENTORY_BORDER_IN;
	private Color COLOR_INVENTORY_BORDER_OUT;
	private Color COLOR_MSGBOX_ACTIVE;
	private Color COLOR_MSGBOX_INACTIVE;
	private int GADGETSIZE;
	public static int TILESIZE;
	private int POS_LEVELDESC_X = 178;
    private int POS_LEVELDESC_Y = 570;

	
	private int inte(String n){
		return Integer.parseInt(n);
	}
	
	private Color getColor(String rgba){
		String[] components = rgba.split(",");
		if (components.length == 4)
			return new Color (inte(components[0]), inte(components[1]), inte(components[2]),inte(components[3]));
		else
			return new Color (inte(components[0]), inte(components[1]), inte(components[2]));
	}
	
	private Rectangle getRectangle(String dims){
		String[] components = dims.split(",");
		return new Rectangle (inte(components[0]), inte(components[1]), inte(components[2]),inte(components[3]));
	}
	
	private void initProperties(Properties p){
		xrange = inte(p.getProperty("XRANGE"));
		yrange = inte(p.getProperty("YRANGE"));
		POS_LEVELDESC_X = inte(p.getProperty("POS_LEVELDESC_X"));
		POS_LEVELDESC_Y = inte(p.getProperty("POS_LEVELDESC_Y"));
		
		UPLEFTBORDER = inte(p.getProperty("UPLEFTBORDER"));
		PC_POS = new Position(inte(p.getProperty("PC_POS_X")), inte(p.getProperty("PC_POS_Y")));
		TILESIZE = inte(p.getProperty("TILESIZE"));
		COLOR_REDPOOL_MINIMAP = getColor(p.getProperty("COLOR_REDPOOL_MINIMAP"));
		COLOR_REDFOUNTAIN_MINIMAP = getColor(p.getProperty("COLOR_REDFOUNTAIN_MINIMAP"));
		COLOR_YELLOWPOOL_MINIMAP = getColor(p.getProperty("COLOR_YELLOWPOOL_MINIMAP"));
		COLOR_YELLOWFOUNTAIN_MINIMAP = getColor(p.getProperty("COLOR_YELLOWFOUNTAIN_MINIMAP"));
		COLOR_INVENTORY_BACKGROUND = getColor(p.getProperty("COLOR_INVENTORY_BACKGROUND"));
		COLOR_INVENTORY_BORDER_IN = getColor(p.getProperty("COLOR_INVENTORY_BORDER_IN"));
		COLOR_INVENTORY_BORDER_OUT = getColor(p.getProperty("COLOR_INVENTORY_BORDER_OUT"));
		COLOR_MSGBOX_ACTIVE = getColor(p.getProperty("COLOR_MSGBOX_ACTIVE"));
		COLOR_MSGBOX_INACTIVE = getColor(p.getProperty("COLOR_MSGBOX_INACTIVE"));
		try {
			FNT_MESSAGEBOX = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File(p.getProperty("FNT_MESSAGEBOX","res/Commodore Rounded v1-1.ttf")))).deriveFont(Font.PLAIN, inte(p.getProperty("FNT_MESSAGEBOX_SIZE","16")));
		} catch (FontFormatException ffe){
			Game.crash("Error loading the font", ffe);
		} catch (IOException ioe){
			Game.crash("Error loading the font", ioe);
		}
		
		/*-- Load UI Images */
		try {
			IMG_STATUSSCR_BGROUND = 
				ImageUtils.crearImagen(
						p.getProperty("IMG_STATUSSCR_BGROUND"),
						0,
						0,
						inte(p.getProperty("WINDOW_WIDTH")),
						inte(p.getProperty("WINDOW_HEIGHT"))
				);
			GADGETSIZE = inte(p.getProperty("GADGETSIZE"));
			TILE_LINE_AIM  = ImageUtils.crearImagen(p.getProperty("SPRITES_UI_GADGETS"), 0, 0, GADGETSIZE, GADGETSIZE);
			TILE_SCAN  = ImageUtils.crearImagen(p.getProperty("SPRITES_UI_GADGETS"), GADGETSIZE, 0, GADGETSIZE, GADGETSIZE);
			TILE_LINE_STEPS  = ImageUtils.crearImagen(p.getProperty("SPRITES_UI_GADGETS"), GADGETSIZE*2, 0, GADGETSIZE, GADGETSIZE);
			
			//IMG_ICON = ImageUtils.createImage("res/crl_icon.png");
		} catch (Exception e){
			Game.crash(e.getMessage(),e);
		}
		
	}

	//Status Attributes
	private Monster lockedMonster;
	private boolean eraseOnArrival; // Erase the buffer upon the arrival of a new msg
	private Vector messageHistory = new Vector(10);
	private boolean [][] FOVMask;
	
	//Components
	public SwingInformBox messageBox;
	
	// Relations
 	private transient SwingSystemInterface si;
 	private static UISelector selector;
 	
 	// Constants
 	private final int WEAPONCODE = CharKey.SPACE;
	
    
 	
    // Smart Getters
    public Position getAbsolutePosition(Position insideLevel){
    	Position relative = Position.subs(insideLevel, player.getPosition());
		return Position.add(PC_POS, relative);
	}

    /*private boolean cheatConsole(CharKey input){
		switch (input.code){
		case CharKey.F2:
			player.recoverIntegrity(300);
			player.recoverMana(300);
			//player.increaseWhip();
			//player.addHearts(5);
			break;
		case CharKey.F3:
			player.setInvincible(250);
			level.anihilate();
			break;
		case CharKey.F4:
			player.informPlayerEvent(Player.EVT_NEXT_LEVEL);
			break;
		case CharKey.F5:
			//player.heal();
			break;
		case CharKey.F6:
			player.setCounter("POISON", 10);
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
	}*/
    
    public void showMessageHistory(){
    	messageBox.setVisible(false);
		si.saveBuffer();
		si.drawImage(GFXDisplay.IMG_FRAME);
		si.print(3, 3, "Message Buffer", Color.BLUE);
		for (int i = 0; i < 15; i++){
			if (i >= messageHistory.size())
				break;
			si.print(3,i+4, (String)messageHistory.elementAt(messageHistory.size()-1-i), Color.WHITE);
		}
		
		si.print(55, 24, "[ Space to Continue ]", Color.WHITE);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.restore();
		si.refresh();
		messageBox.setVisible(true);
	}
    
    //Interactive Methods
    public void doLook(){
		Position offset = new Position (0,0);
		
		messageBox.setForeground(COLOR_MSGBOX_ACTIVE);
		si.saveBuffer();
		while (true){
			int cellHeight = 0;
			Position browser = Position.add(player.getPosition(), offset);
			String looked = "";
			si.restore();
			if (FOVMask[PC_POS.x + offset.x][PC_POS.y + offset.y]){
				Cell choosen = level.getMapCell(browser);
				if (choosen != null)
					cellHeight = choosen.getHeight();
				Feature feat = level.getFeatureAt(browser);
				Vector items = level.getItemsAt(browser);
				Item item = null;
				if (items != null) {
					item = (Item) items.elementAt(0);
				}
				//Actor actor = level.getActorAt(browser);
				Monster monster = level.getMonsterAt(browser);
				if (choosen != null)
					looked += choosen.getDescription();
				if (level.getBloodAt(browser) != null)
				    looked += "{bloody}";
				if (feat != null)
					looked += ", "+ feat.getDescription();
				if (monster != null)
					looked += ", "+ monster.getExamineDescription();
				if (item != null)
					if (items.size() == 1)
						looked += ", "+ item.getDescription();
					else
						looked += ", "+ item.getDescription()+" and some items";
			}
			messageBox.setText(looked);
			si.drawImage((PC_POS.x + offset.x)*TILESIZE-(GADGETSIZE-TILESIZE)+UPLEFTBORDER, ((PC_POS.y + offset.y)*TILESIZE-(GADGETSIZE-TILESIZE))+UPLEFTBORDER-4*cellHeight, TILE_SCAN);
			si.refresh();
			CharKey x = new CharKey(CharKey.NONE);
			while (x.code != CharKey.SPACE && x.code != CharKey.ESC &&
				   ! x.isArrow())
				x = si.inkey();
			if (x.code == CharKey.SPACE || x.code == CharKey.ESC){
				messageBox.clear();
				level.addMessage("Done");
				si.restore();
				si.refresh();
				break;
			}

			offset.add(Action.directionToVariation(toIntDirection(x)));

			if (offset.x >= xrange) offset.x = xrange;
			if (offset.x <= -xrange) offset.x = -xrange;
			if (offset.y >= yrange) offset.y = yrange;
			if (offset.y <= -yrange) offset.y = -yrange;
     	}
	
	}

    
    

    // Drawing Methods
	public void drawEffect(Effect what){
		if (what == null)
			return;
		if (insideViewPort(getAbsolutePosition(what.getPosition()))){
			((GFXEffect)what).drawEffect(this, si);
		}
	}
	
	public boolean isOnFOVMask(int x, int y){
		return FOVMask[x][y];
		//return true;
	}

	private void drawLevel(){
		Debug.enterMethod(this, "drawLevel");
		//Cell[] [] cells = level.getCellsAround(player.getPosition().x,player.getPosition().y, player.getPosition().z, range);
		Cell[] [] rcells = level.getMemoryCellsAround(player.getPosition().x,player.getPosition().y, player.getPosition().z, xrange,yrange);
		Cell[] [] vcells = level.getVisibleCellsAround(player.getPosition().x,player.getPosition().y, player.getPosition().z, xrange,yrange);
		
		Position runner = new Position(player.getPosition().x - xrange, player.getPosition().y-yrange, player.getPosition().z);
		
		monstersOnSight.removeAllElements();
		featuresOnSight.removeAllElements();
		itemsOnSight.removeAllElements();
		/*for (int x = 0; x < vcells.length; x++){
			for (int y=0; y<vcells[0].length; y++){*/
		for (int y = 0; y < vcells[0].length; y++){
			for (int x=0; x<vcells.length; x++){
				FOVMask[PC_POS.x-xrange+x][PC_POS.y-yrange+y] = false;
				int cellHeight = 0;
				if (vcells[x][y] == null){
					if (rcells[x][y] != null){
						
						GFXAppearance app = (GFXAppearance)rcells[x][y].getAppearance(); 
						if (level.isDay())
							si.drawImage((PC_POS.x-xrange+x)*TILESIZE+UPLEFTBORDER,(PC_POS.y-yrange+y)*TILESIZE+UPLEFTBORDER, app.getDarkImage());
						else
							si.drawImage((PC_POS.x-xrange+x)*TILESIZE+UPLEFTBORDER,(PC_POS.y-yrange+y)*TILESIZE+UPLEFTBORDER, app.getDarkniteImage());
					} else {
						//Draw nothing
						//si.drawImage((PC_POS.x-xrange+x)*TILESIZE,(PC_POS.y-yrange+y)*TILESIZE-17, "gfx/black.gif");
						//si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, CharAppearance.getVoidAppearance().getChar(), CharAppearance.getVoidAppearance().BLACK);
					}
				} else {
					
					cellHeight = vcells[x][y].getHeight();
					FOVMask[PC_POS.x-xrange+x][PC_POS.y-yrange+y] = true;
					String bloodLevel = level.getBloodAt(runner);
					GFXAppearance cellApp = (GFXAppearance)vcells[x][y].getAppearance();
					
					boolean frosty = false;
					if (level.getFrostAt(runner) != 0){
						frosty = true;
						//TODO: Apply a blue tint
					}
					int depthFromPlayer =level.getDepthFromPlayer(player.getPosition().x - xrange + x, player.getPosition().y - yrange + y); 
					if (depthFromPlayer != 0 ){
						si.drawImage((PC_POS.x-xrange+x)*TILESIZE+UPLEFTBORDER,(PC_POS.y-yrange+y)*TILESIZE+UPLEFTBORDER+depthFromPlayer*10, cellApp.getDarkImage());
					} else {
						if (level.isDay())
							si.drawImage((PC_POS.x-xrange+x)*TILESIZE+UPLEFTBORDER,(PC_POS.y-yrange+y)*TILESIZE+UPLEFTBORDER, cellApp.getImage());
						else
							si.drawImage((PC_POS.x-xrange+x)*TILESIZE+UPLEFTBORDER,(PC_POS.y-yrange+y)*TILESIZE+UPLEFTBORDER, cellApp.getNiteImage());
					}
					/*if (bloodLevel != null){
						switch (Integer.parseInt(bloodLevel)){
						case 0:
							si.drawImage((PC_POS.x-xrange+x)*TILESIZE+LEADIN,(PC_POS.y-yrange+y)*TILESIZE+LEADIN-4*cellHeight, BLOOD1);
							break;
						case 1:
							si.drawImage((PC_POS.x-xrange+x)*TILESIZE+LEADIN,(PC_POS.y-yrange+y)*TILESIZE+LEADIN-4*cellHeight, BLOOD2);
							break;
						}
					}*/
					
					/*if (yrange == y && x == xrange && !player.isInvisible()){
						si.drawImage(PC_POS.x*TILESIZE,PC_POS.y*TILESIZE-4*cellHeight, ((GFXAppearance)player.getAppearance()).getImage());
					}
					Feature feat = level.getFeatureAt(runner);
					if (feat != null){
						if (feat.isVisible()) {
							GFXAppearance featApp = (GFXAppearance)feat.getAppearance();
							si.drawImage((PC_POS.x-xrange+x)*TILESIZE-featApp.getSuperWidth(),(PC_POS.y-yrange+y)*TILESIZE-4*cellHeight-featApp.getSuperHeight(), featApp.getImage());
						}
					}
					
					SmartFeature sfeat = level.getSmartFeature(runner);
					if (sfeat != null){
						if (sfeat.isVisible()){
							GFXAppearance featApp = 
								(GFXAppearance)sfeat.getAppearance();
							si.drawImage((PC_POS.x-xrange+x)*TILESIZE-featApp.getSuperWidth(),(PC_POS.y-yrange+y)*TILESIZE-4*cellHeight-featApp.getSuperHeight(), featApp.getImage());
						}
					}

					Vector items = level.getItemsAt(runner);
					Item item = null;
					if (items != null){
						item = (Item) items.elementAt(0);
					}
					if (item != null){
						if (item.isVisible()){
							GFXAppearance itemApp = (GFXAppearance)item.getAppearance();
							si.drawImage((PC_POS.x-xrange+x)*TILESIZE-itemApp.getSuperWidth(),(PC_POS.y-yrange+y)*TILESIZE-4*cellHeight -itemApp.getSuperHeight(), itemApp.getImage());
						}
					}
					
					Monster monster = level.getMonsterAt(runner);
					
					if (monster != null && monster.isVisible()){
						GFXAppearance monsterApp = (GFXAppearance) monster.getAppearance();
						if (monster.canSwim() && level.getMapCell(runner)!= null && level.getMapCell(runner).isWater()){
							//si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, '~', monsterApp.getColor());
							//TODO: Overlap water on the monster, draw it lowly
						}
						else
						if (monster.isFrozen()){
							//TODO: Overlay a cyan layer
							si.drawImage((PC_POS.x-xrange+x)*TILESIZE-monsterApp.getSuperWidth(),(PC_POS.y-yrange+y)*TILESIZE-4*cellHeight-monsterApp.getSuperHeight(), monsterApp.getImage());
						}
						else
							si.drawImage((PC_POS.x-xrange+x)*TILESIZE-monsterApp.getSuperWidth(),(PC_POS.y-yrange+y)*TILESIZE-4*cellHeight-monsterApp.getSuperHeight(), monsterApp.getImage());
					}*/
				}
				runner.x++;
			}
			runner.x = player.getPosition().x-xrange;
			for (int x=0; x<vcells.length; x++){
				int cellHeight = 0;
				if (vcells[x][y] != null){
					cellHeight = vcells[x][y].getHeight();
					
					Feature feat = level.getFeatureAt(runner);
					if (feat != null){
						if (feat.isVisible()) {
							GFXAppearance featApp = (GFXAppearance)feat.getAppearance();
							si.drawImage((PC_POS.x-xrange+x)*TILESIZE+UPLEFTBORDER-featApp.getSuperWidth(),(PC_POS.y-yrange+y)*TILESIZE+UPLEFTBORDER-4*cellHeight-featApp.getSuperHeight(), featApp.getImage());
						}
					}
					
					SmartFeature sfeat = level.getSmartFeature(runner);
					if (sfeat != null){
						if (sfeat.isVisible()){
							GFXAppearance featApp = 
								(GFXAppearance)sfeat.getAppearance();
							si.drawImage((PC_POS.x-xrange+x)*TILESIZE+UPLEFTBORDER-featApp.getSuperWidth(),(PC_POS.y-yrange+y)*TILESIZE+UPLEFTBORDER-4*cellHeight-featApp.getSuperHeight(), featApp.getImage());
						}
					}

					Vector items = level.getItemsAt(runner);
					Item item = null;
					if (items != null){
						item = (Item) items.elementAt(0);
					}
					if (item != null){
						if (item.isVisible()){
							GFXAppearance itemApp = (GFXAppearance)item.getAppearance();
							si.drawImage((PC_POS.x-xrange+x)*TILESIZE+UPLEFTBORDER-itemApp.getSuperWidth(),(PC_POS.y-yrange+y)*TILESIZE+UPLEFTBORDER-4*cellHeight -itemApp.getSuperHeight(), itemApp.getImage());
						}
					}
					
					Monster monster = level.getMonsterAt(runner);
					
					if (monster != null && monster.isVisible()){
						GFXAppearance monsterApp = (GFXAppearance) monster.getAppearance();
						if (monster.isFrozen()){
							//TODO: Overlay a cyan layer
							si.drawImage((PC_POS.x-xrange+x)*TILESIZE+UPLEFTBORDER-monsterApp.getSuperWidth(),(PC_POS.y-yrange+y)*TILESIZE+UPLEFTBORDER-4*cellHeight-monsterApp.getSuperHeight(), monsterApp.getImage());
						}
						else
							si.drawImage((PC_POS.x-xrange+x)*TILESIZE+UPLEFTBORDER-monsterApp.getSuperWidth(),(PC_POS.y-yrange+y)*TILESIZE+UPLEFTBORDER-4*cellHeight-monsterApp.getSuperHeight(), monsterApp.getImage());
					}
					if (yrange == y && x == xrange){
						if (player.isInvisible()){
							si.drawImage(PC_POS.x*TILESIZE+UPLEFTBORDER,PC_POS.y*TILESIZE+UPLEFTBORDER-4*cellHeight, ((GFXAppearance)AppearanceFactory.getAppearanceFactory().getAppearance("SHADOW")).getImage());
						}else{
							si.drawImage(PC_POS.x*TILESIZE+UPLEFTBORDER,PC_POS.y*TILESIZE+UPLEFTBORDER-4*cellHeight, ((GFXAppearance)player.getAppearance()).getImage());
						}
					}
				}
				//runner.y++;
				runner.x++;
			} 
			/*runner.y = player.getPosition().y-yrange;
			runner.x ++;*/
			runner.x = player.getPosition().x-xrange;
			runner.y ++;
		}
		
		
		
		Debug.exitMethod();
	}
	
	public void addMessage(Message message){
		Debug.enterMethod(this, "addMessage", message);
		if (eraseOnArrival){
	 		messageBox.clear(); //Este lo quito
	 		messageBox.setForeground(COLOR_MSGBOX_ACTIVE);
	 		eraseOnArrival = false;
		}
		if (message.getLocation().z != player.getPosition().z || !insideViewPort(getAbsolutePosition(message.getLocation()))){
			Debug.exitMethod();
			return;
		}
		messageHistory.add(message.getText());
		if (messageHistory.size()>500)
			messageHistory.removeElementAt(0);
		messageBox.addText(message.getText());
		Debug.exitMethod();
	}


	
    private void drawPlayerStatus(){
	    Debug.enterMethod(this, "drawPlayerStatus");
	    if (player.getArmor()!= null){
    		si.printAtPixel(582,130,player.getArmor().getDescription(), Color.WHITE);
    		si.printAtPixel(582,150," INT:"+player.getArmor().getIntegrity()+" COV:"+player.getArmor().getCoverage()+"%", Color.WHITE);
    	}
	    if (player.getWeapon()!= null){
	    	si.printAtPixel(582,170,player.getWeapon().getDescription(), Color.WHITE);
	    	si.printAtPixel(582,190," ATK:"+(player.getWeapon().getAttack()+player.getAttack())+" BRK:"+(player.getWeapon().getEvadeBreak()+player.getEvadeBreak()), Color.WHITE);
	    } else {
	    	si.printAtPixel(582,170,"Bare Hands", Color.WHITE);
	    	si.printAtPixel(582,190," ATK:"+player.getAttack()+" BRK:"+player.getEvadeBreak(), Color.WHITE);
	    }
    	
    	si.printAtPixel(582,50,"HEALTH "+player.getIntegrityPoints()+"/"+player.getIntegrityMax(), Color.WHITE);
    	si.printAtPixel(582,70,"EVADE  "+player.getEvadePoints()+"/"+player.getEvadePointsMax()+" "+player.getEvasion()+"%", Color.WHITE);
    	si.printAtPixel(582,90,"MANA   "+player.getMana()+"/"+player.getManaMax(), Color.WHITE);
    	si.printAtPixel(582,110,"SCORE  " + player.getScore(), Color.WHITE);
    	si.printAtPixel(582,210,"GOLD   "+player.getGold(), Color.WHITE);
    	
    	String tactic = player.getCurrentTactic() == Player.TACTIC_NORMAL ? "": "(Brave)";
    	si.printAtPixel(590,260,player.getStatusString()+tactic, Color.WHITE);
    	si.printAtPixel(POS_LEVELDESC_X,POS_LEVELDESC_Y,player.getLevel().getDescription(), Color.WHITE);
		Debug.exitMethod();
    }
    
    
	public void init(SwingSystemInterface psi, UserAction[] gameActions, UserCommand[] gameCommands,
		Action advance, Action target, Action attack, Properties UIProperties){
		Debug.enterMethod(this, "init");
		super.init(gameActions, gameCommands, advance, target, attack);
		initProperties(UIProperties);
		selector = new UISelector();
		//GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setDisplayMode(new DisplayMode(800,600,8, DisplayMode.REFRESH_RATE_UNKNOWN));
		
		/*-- Assign values */
		si = psi;
		FOVMask = new boolean[xrange*2+1][yrange*2+1];
		//FOVMask = new boolean[80][25];
		si.getGraphics2D().setColor(Color.BLACK);
		si.getGraphics2D().fillRect(0,0,800,600);
		si.refresh();
		
		//si.setIcon(IMG_ICON);
		si.setTitle("DrashRL v"+Game.getVersion()+", Santiago Zapata 2006-2007");
		/*-- Init Components*/
		messageBox = new SwingInformBox();
		/*idList = new ListBox(psi);*/
		messageBox.setBounds(getRectangle(UIProperties.getProperty("MSGBOX_BOUNDS")));
		messageBox.setForeground(getColor(UIProperties.getProperty("COLOR_MSGBOX_ACTIVE")));
		messageBox.setBackground(getColor(UIProperties.getProperty("MSGBOX_BGROUND")));
		messageBox.setFont(FNT_MESSAGEBOX);
		messageBox.setEditable(false);
		messageBox.setVisible(false);
		messageBox.setFocusable(false);
		messageBox.setOpaque(false);
		messageBox.setLineWrap(true);
		messageBox.setWrapStyleWord(true);
		
		psi.add(messageBox);
		
		si.setVisible(true);
		
		Debug.exitMethod();
	}

	/** 
	 * Checks if the point, relative to the console coordinates, is inside the
	 * ViewPort 
	 */
	public boolean insideViewPort(int x, int y){
    	//return (x>=VP_START.x && x <= VP_END.x && y >= VP_START.y && y <= VP_END.y);
		return (x>=0 && x < FOVMask.length && y >= 0 && y < FOVMask[0].length) && FOVMask[x][y];
    }

	public boolean insideViewPort(Position what){
    	return insideViewPort(what.x, what.y);
    }

	public boolean isDisplaying(Actor who){
    	return insideViewPort(getAbsolutePosition(who.getPosition()));
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
	
    public Position pickPosition(String prompt) throws ActionCancelException{
    	Debug.enterMethod(this, "pickPosition");
    	messageBox.setForeground(COLOR_MSGBOX_ACTIVE);
    	messageBox.setText(prompt);
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
			if (!player.sees(lockedMonster)  || lockedMonster.isDead()){
				lockedMonster = null;
			}
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
    	
		si.refresh();
		si.saveBuffer();
		
		while (true){
			si.restore();
			int cellHeight = 0;
			String looked = "";
			browser = Position.add(player.getPosition(), offset);
			if (FOVMask[PC_POS.x + offset.x][PC_POS.y + offset.y]){
				Cell choosen = level.getMapCell(browser);
				Feature feat = level.getFeatureAt(browser);
				Vector items = level.getItemsAt(browser);
				if (choosen != null)
					cellHeight = choosen.getHeight();
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
					looked += ", "+ item.getDescription();
			}
			messageBox.setText(prompt+" "+looked);
			//si.print(PC_POS.x + offset.x, PC_POS.y + offset.y, '_', ConsoleSystemInterface.RED);
			drawStepsTo(PC_POS.x + offset.x, (PC_POS.y + offset.y), TILE_LINE_STEPS, cellHeight);
			
			si.drawImage((PC_POS.x + offset.x)*TILESIZE-((GADGETSIZE-TILESIZE)/2)+UPLEFTBORDER, ((PC_POS.y + offset.y)*TILESIZE-((GADGETSIZE-TILESIZE)/2))+UPLEFTBORDER -4*cellHeight, TILE_LINE_AIM);
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
			offset.add(Action.directionToVariation(toIntDirection(x)));

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
		si.refresh();
		//refresh();

		CharKey x = new CharKey(CharKey.NONE);
		while (x.code == CharKey.NONE)
			x = si.inkey();
		if (x.isArrow()){
			int ret = toIntDirection(x);
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
		messageBox.setVisible(false);
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
  		menuBox.setGap(35);
  		//menuBox.setBounds(26,6,30,11);
  		menuBox.setBounds(6,4,70,12);
  		menuBox.setMenuItems(equipped);
  		menuBox.setTitle(prompt);
  		si.saveBuffer();
  		//menuBox.draw();
		Item equiped = (Item)menuBox.getSelection();
		if (equiped == null){
			ActionCancelException ret = new ActionCancelException();
			Debug.exitExceptionally(ret);
			si.restore();
			si.refresh();
			throw ret;
		}
		si.restore();
		si.refresh();
		messageBox.setVisible(true);
		return equiped;
	}
	
	public Item pickItem(String prompt) throws ActionCancelException{
		messageBox.setVisible(false);
  		Vector inventory = player.getInventory();
  		MenuBox menuBox = new MenuBox(si);
  		menuBox.setGap(35);
  		menuBox.setPosition(6,4);
  		menuBox.setWidth(70);
  		menuBox.setItemsPerPage(12);
  		menuBox.setMenuItems(inventory);
  		menuBox.setTitle(prompt);
  		si.saveBuffer();
  		//menuBox.draw();
		Equipment equipment = (Equipment)menuBox.getSelection();
		si.restore();
		if (equipment == null){
			ActionCancelException ret = new ActionCancelException();
			Debug.exitExceptionally(ret);
			si.restore();
			si.refresh();
			throw ret;
		}
		si.restore();
		si.refresh();
		messageBox.setVisible(true);
		return equipment.getItem();
		
		
	}
	
	public void processQuit(){
		messageBox.setForeground(COLOR_MSGBOX_ACTIVE);
		messageBox.setText(quitMessages[Util.rand(0, quitMessages.length-1)]+" (y/n)");
		si.refresh();
		if (prompt()){
			messageBox.setText("[Press Space to continue]");
			si.refresh();
			si.waitKey(CharKey.SPACE);
			messageBox.setVisible(false);
			player.getGameSessionInfo().setDeathCause(GameSessionInfo.QUIT);
			player.getGameSessionInfo().setDeathLevel(level.getLevelNumber());
			informPlayerCommand(CommandListener.QUIT);
		}
		messageBox.clear();
		si.refresh();
	}
	
	public void processSave(){
		if (!player.getGame().canSave()){
			level.addMessage("You cannot save your game here!");
			return;
		}
		messageBox.setForeground(COLOR_MSGBOX_ACTIVE);
		messageBox.setText("Save your game? (y/n)");
		si.refresh();
		if (prompt()){
			messageBox.setText("Saving... I will await your return.. [Press Space to continue]");
			si.refresh();
			si.waitKey(CharKey.SPACE);
			messageBox.setVisible(false);
			informPlayerCommand(CommandListener.SAVE);
		}
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
		//si.cls();
		si.drawImage(0,0, IMG_STATUSSCR_BGROUND);
		messageBox.setVisible(true);
		//cleanViewPort();
	 	drawLevel();
	 	drawPlayerStatus();
		//idList.draw();
		si.refresh();
	  	messageBox.setForeground(COLOR_MSGBOX_INACTIVE);
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
		
		if (a.needsUnderlyingItem()){
			a.setItem(pickUnderlyingItem(a.getPrompUnderlyingItem()));
		}
	}
	
	public Item pickUnderlyingItem(String prompt) throws ActionCancelException{
		Debug.enterMethod(this, "pickUnderlyingItem");
		messageBox.setVisible(false);
  		Vector items = level.getItemsAt(player.getPosition());
  		if (items == null)
  			return null;
  		if (items.size() == 1)
  			return (Item) items.elementAt(0);
  		MenuBox menuBox = new MenuBox(si);
  		menuBox.setGap(35);
  		menuBox.setBounds(6,4,70,12);
  		menuBox.setMenuItems(items);
  		menuBox.setTitle(prompt);
  		si.saveBuffer();
  		//menuBox.draw();
		Item item = (Item)menuBox.getSelection();
		
		if (item == null){
			ActionCancelException ret = new ActionCancelException();
			Debug.exitExceptionally(ret);
			si.restore();
			si.refresh();
			throw ret;
		}
		si.restore();
		si.refresh();
		messageBox.setVisible(true);
		return item;
	}
	public Action showInventory() throws ActionCancelException {
		messageBox.setVisible(false);
		Equipment.eqMode = true;
  		Vector inventory = player.getInventory();
		int xpos = 1, ypos = 0;
  		MenuBox menuBox = new MenuBox(si);
  		menuBox.setGap(35);
  		menuBox.setItemsPerPage(10);
  		menuBox.setWidth(75);
  		menuBox.setPosition(3,8);
  		menuBox.setTitle("Items");
  		menuBox.setMenuItems(inventory);
  		//si.cls();
  		si.saveBuffer();
  		int xx = 17; int yy = 22;
  		int ww=750; int hh = 141;
  		si.getGraphics2D().setColor(COLOR_INVENTORY_BACKGROUND);
		si.getGraphics2D().fillRect(xx+6, yy+6, ww-14, hh-14);
		si.getGraphics2D().setColor(COLOR_INVENTORY_BORDER_OUT);
		si.getGraphics2D().drawRect(xx+6,yy+6,ww-14,hh-14);
		si.getGraphics2D().setColor(COLOR_INVENTORY_BORDER_IN);
		si.getGraphics2D().drawRect(xx+8,yy+8,ww-18,hh-18);
		
  		si.print(xpos+2,ypos+2,  "Inventory", Color.BLUE);
 		si.print(xpos+2,ypos+3,  "Weapons:   "+player.getEquipedWeaponDescription(), Color.WHITE);
 		si.print(xpos+2,ypos+4,  "           "+player.getSecondaryWeaponDescription(), Color.WHITE);
 		si.print(xpos+2,ypos+5,  "Armor:    "+player.getArmorDescription(), Color.WHITE);
 		si.print(xpos+2,ypos+6,  "Accessory:"+player.getAccDescription(), Color.WHITE);
 		//menuBox.draw();
 		//si.print(xpos,24,  "[Space] to continue, Up and Down to browse", Color.WHITE);
 		
 		si.refresh();
		menuBox.getSelection();
		
		si.restore();
		si.refresh();
		Equipment.eqMode = false;
		messageBox.setVisible(true);
		return null;
		
		/*
		Equipment.eqMode = true;
		inventoryBox.setPlayer(player, player.getInventory());
		inventoryBox.setVisible(true);
		inventoryBox.informChoice(Thread.currentThread());
    	try {
 			this.wait();
 		} catch (InterruptedException ie) {
 			
 		}
 		si.recoverFocus();
 		inventoryBox.setVisible(false);
 		si.recoverFocus();
 		Equipment.eqMode = false;
        return null;*/
	}


 	/**
     * Shows a message inmediately; useful for system
     * messages.
     *  
     * @param x the message to be shown
     */
	public void showMessage(String x){
		messageBox.setForeground(COLOR_MSGBOX_ACTIVE);
		messageBox.setText(x);
		si.refresh();
	}
	
	public void showSystemMessage(String x){
		messageBox.setForeground(COLOR_MSGBOX_ACTIVE);
		messageBox.setText(x);
		si.refresh();
		si.waitKey(CharKey.SPACE);
	}
	
	

	public void showPlayerStats (){
		si.saveBuffer();
		messageBox.setVisible(false);
		si.drawImage(IMG_STATUSSCR_BGROUND);
		si.print(3,3, player.getName()+" "+player.getStatusString()+" Gold: "+player.getGold()+" Score: "+player.getScore(), Color.WHITE);
	    si.print(3,4, "Sex: "+ (player.getSex() == Player.MALE ? "M" : "F"), Color.WHITE);
	    si.print(3,5, " Integrity: "+player.getIntegrityPoints()+ "/"+player.getIntegrityMax(), Color.WHITE);
	    si.print(3,6, " Break Points: "+player.getEvadePoints()+ "/"+player.getEvadePointsMax(), Color.WHITE);
	    si.print(3,7, " Mana: "+player.getMana()+ "/"+player.getManaMax(), Color.WHITE);
	    si.print(3,8, "Carrying: "+player.getItemCount()+"/"+player.getCarryMax(), Color.WHITE);
	    si.print(3,9, "Attack: +"+player.getBaseAttack()+" (+"+(player.getAttack()+(player.getWeapon() != null ? player.getWeapon().getAttack() : 0))+")", Color.WHITE);
	    si.print(3,10, "Break: +"+player.getBaseEvadeBreak()+" (+"+(player.getEvadeBreak()+(player.getWeapon() != null ? player.getWeapon().getEvadeBreak() : 0))+")", Color.WHITE);
	    si.print(3,11, "Soul: +"+player.getBaseSoulPower()+" (+"+player.getSoulPower()+")", Color.WHITE);
	    si.print(3,12, "Evasion: "+player.getBaseEvasion()+"%"+" ("+player.getEvasion()+"%)", Color.WHITE);
	    si.print(3,13, "Sight: "+player.getBaseSightRange()+" ("+player.getSightRange()+")", Color.WHITE);
	    si.print(3,14, "Speed: "+((player.getBaseWalkCost()-50)*-1)+ " ("+((player.getWalkCost()-50)*-1)+")", Color.WHITE);
	    si.print(3,15, "Combat: "+((player.getBaseAttackCost()-50)*-1)+ " ("+((player.getAttackCost()-50)*-1)+")", Color.WHITE);
	    si.print(3,16, "Magic: "+((player.getBaseCastCost()-50)*-1)+ " ("+((player.getCastCost()-50)*-1)+")", Color.WHITE);
		si.print(3,21, "[ Press Space to continue ]", Color.WHITE);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.restore();
		si.refresh();
		messageBox.setVisible(true);
	}
	
    public Action showSkills() throws ActionCancelException {
    	Debug.enterMethod(this, "showSkills");
    	si.saveBuffer();
		Vector skills = player.getAvailableSkills();
  		MenuBox menuBox = new MenuBox(si);
  		menuBox.setItemsPerPage(14);
  		menuBox.setWidth(48);
  		menuBox.setPosition(6,4);
  		menuBox.setMenuItems(skills);
  		menuBox.setTitle("Skills");
  		//menuBox.draw();
		si.refresh();
        Skill selectedSkill = (Skill)menuBox.getSelection();
        if (selectedSkill == null){
        	si.restore();
        	si.refresh();
        	Debug.exitMethod("null");
        	return null;
        }
        si.restore();
        si.refresh();
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
		
		
		Debug.exitMethod(selectedAction);
		return selectedAction;
	}

   
   

    private Action selectCommand (CharKey input){
		Debug.enterMethod(this, "selectCommand", input);
		int com = getRelatedCommand(input.code);
		informPlayerCommand(com);
		Action ret = actionSelectedByCommand;
		actionSelectedByCommand = null;
		Debug.exitMethod(ret);
		return ret;
	}
    
    private void examineLevelMap(){
		messageBox.setVisible(false);
		si.saveBuffer();
		si.drawImage(GFXDisplay.IMG_FRAME);
		int lw = level.getWidth();
		int lh = level.getHeight();
		int remnantx = (int)((740 - (lw * 5))/2.0d); 
		int remnanty = (int)((480 - (lh * 5))/2.0d);
		Graphics2D g = si.getGraphics2D();
		Color cellColor = null;
		for (int x = 0; x < level.getWidth(); x++)
			for (int y = 0; y < level.getHeight(); y++){
				if (!level.remembers(x,y))
					cellColor = Color.BLACK;
				else {
					Cell current = level.getMapCell(x, y, 0);
					Feature currentF = level.getFeatureAt(x,y,0);
					if (level.isVisible(x,y)){
						if (current.isSolid() || (currentF != null && currentF.isSolid()))
							cellColor = Color.BLUE;
						else if (current.getID().startsWith("DDOOR"))
							cellColor = Color.CYAN;
						else 
							cellColor = Color.GRAY;
						if (currentF != null){
							if (currentF.getID().equals("RED_POOL")){
								cellColor = COLOR_REDPOOL_MINIMAP;
							} else if (currentF.getID().equals("RED_WATER")){
								cellColor = COLOR_REDFOUNTAIN_MINIMAP;
							} else if (currentF.getID().equals("YELLOW_POOL") ){
								cellColor = COLOR_YELLOWPOOL_MINIMAP;
							} else if (currentF.getID().equals("YELLOW_WATER")){
								cellColor = COLOR_YELLOWFOUNTAIN_MINIMAP;
							} else if (currentF.getID().equals("FORGE")){
								cellColor = Color.ORANGE;
							} else if (currentF.getID().equals("ANKH")){
								cellColor = Color.WHITE;
							}
						}
					} else {
						if (current.isSolid()|| (currentF != null && currentF.isSolid()))
							cellColor = Color.BLUE;
						else if (current.getID().startsWith("DDOOR"))
							cellColor = Color.MAGENTA;
						else 
							cellColor = Color.DARK_GRAY;
						if (currentF != null){
							if (currentF.getID().equals("RED_POOL")){
								cellColor = COLOR_REDPOOL_MINIMAP;
							} else if (currentF.getID().equals("RED_WATER")){
								cellColor = COLOR_REDFOUNTAIN_MINIMAP;
							} else if (currentF.getID().equals("YELLOW_POOL") ){
								cellColor = COLOR_YELLOWPOOL_MINIMAP;
							} else if (currentF.getID().equals("YELLOW_WATER")){
								cellColor = COLOR_YELLOWFOUNTAIN_MINIMAP;
							} else if (currentF.getID().equals("FORGE")){
								cellColor = Color.ORANGE;
							} else if (currentF.getID().equals("ANKH")){
								cellColor = Color.WHITE;
							}
						}
					}
					if (player.getPosition().x == x && player.getPosition().y == y)
						cellColor = Color.RED;
				}
				g.setColor(cellColor);
				g.fillRect(30+remnantx+x*5, 30+remnanty+y*5, 5,5);
			}
			si.refresh();	
		
		
		si.waitKey(CharKey.SPACE);
		messageBox.setVisible(true);
		si.restore();
		si.refresh();
		
	}
	public void commandSelected (int commandCode){
		switch (commandCode){
			case CommandListener.PROMPTQUIT:
				processQuit();
				break;
			case CommandListener.PROMPTSAVE:
				processSave();
				break;
			case CommandListener.EXAMINELEVELMAP:
				examineLevelMap();
				break;
			case CommandListener.HELP:
				/*si.saveBuffer();
				
				helpBox.setVisible(true);
				si.restore();*/
				messageBox.setVisible(false);
				Display.thus.showHelp();
				messageBox.setVisible(true);
				break;
			case CommandListener.LOOK:
				doLook();
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
					si.restore();
					si.refresh();
				}
				break;
			case CommandListener.SHOWMESSAGEHISTORY:
				showMessageHistory();
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
			case CommandListener.CHANGE_TACTICS:
				player.changeTactic();
				break;
			case CommandListener.CHAR_DUMP:
				GameFiles.saveChardump(player);
				showMessage("Character File Dumped.");
				break;
			
		}
	}
	
	

//	Runnable interface
	public void run (){}
	
//	IO Utility    
	public void waitKey (){
		CharKey x = new CharKey(CharKey.NONE);
		while (x.code == CharKey.NONE)
			x = si.inkey();
	}


	private void drawStepsTo(int x, int y, Image tile, int cellHeight){
		Position target = new Position(x,y);
		Line line = new Line(PC_POS, target);
		Position tmp = line.next();
		while (!tmp.equals(target)){
			tmp = line.next();
			si.drawImage(tmp.x*TILESIZE-((GADGETSIZE-TILESIZE)/2)+UPLEFTBORDER, tmp.y*TILESIZE-((GADGETSIZE-TILESIZE)/2)+UPLEFTBORDER-4*cellHeight, tile);
		}
		
	}
	
	public int toIntDirection(Position what){
		switch (what.x()){
			case 1:
				switch (what.y()){
					case 1:
						return Action.DOWNRIGHT;
					case 0:
						return Action.RIGHT;
					case -1:
						return Action.UPRIGHT;
				}
			case 0:
				switch (what.y()){
					case 1:
						return Action.DOWN;
					case -1:
						return Action.UP;
				}
			case -1:
				switch (what.y()){
					case 1:
						return Action.DOWNLEFT;
					case 0:
						return Action.LEFT;
					case -1:
						return Action.UPLEFT;
				}
		}

		return -1;
	}
	
	public static int toIntDirection(CharKey ck){
		if (ck.isUpArrow())
			return Action.UP;
		else
		if (ck.isLeftArrow())
			return Action.LEFT;
		else
		if (ck.isRightArrow())
			return Action.RIGHT;
		else
		if (ck.isDownArrow())
			return Action.DOWN;
		else
		if (ck.isUpRightArrow())
			return Action.UPRIGHT;
		else
		if (ck.isUpLeftArrow())
			return Action.UPLEFT;
		else
		if (ck.isDownLeftArrow())
			return Action.DOWNLEFT;
		else
		if (ck.isDownRightArrow())
			return Action.DOWNRIGHT;
		else if (ck.isSelfArrow())
			return Action.SELF;
		return -1;
	}
	
	public Vector getMessageBuffer() {
		//return new Vector(messageHistory.subList(0,21));
		if (messageHistory.size()>20)
			return new Vector(messageHistory.subList(messageHistory.size()-21,messageHistory.size()));
		else
			return messageHistory;
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
				if (ret != null){
					if (ret != null){
	                	if (ret.canPerform(player))
	                		return ret;
	                	else 
	                		return null;
					}
				}
				if (input.code == CharKey.DOT) {
					((Player)who).doNothing();
					Debug.exitMethod("null");
					return null;
				}
				if (input.code == CharKey.DELETE) {
					((Player)who).doNothing();
					Debug.exitMethod("null");
					return null;
				}
				
				/*if (cheatConsole(input)){
				continue;
				}*/
				
				if (input.isArrow()){
					int direction = toIntDirection(input);
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
					ret = target;
	            	try {
	            		ret.setPerformer(player);
	            		if (ret.canPerform(player))
	            			setTargets(ret);
	            		else
	            			throw new ActionCancelException(); /*090107*/
                     	Debug.exitMethod(ret);
                    	return ret;
					}
					catch (ActionCancelException ace){
		 				addMessage(new Message("Cancelled", player.getPosition()));
						ret = null;
					}

					
				}else{
	            	ret = getRelatedAction(input.code);
	            	
	            	try {
		            	if (ret != null){
		            		ret.setPerformer(player);
		            		if (ret.canPerform(player))
		            			setTargets(ret);
		            		else 
		            			throw new ActionCancelException(); /*090107*/
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



