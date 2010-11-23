package crl.conf.gfx.data;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import sz.csi.ConsoleSystemInterface;
import sz.util.ImageUtils;

import crl.game.Game;
import crl.ui.*;
import crl.ui.consoleUI.CharAppearance;
import crl.ui.graphicsUI.*;

public class GFXAppearances {
	private Hashtable images = new Hashtable();
	private String tileSet;
	private String tileSetd;
	private int tileSize;
	private Appearance [] defs;
	public GFXAppearances(String pTileSet, String pTileSetd, int pTileSize){
		System.out.println("Using tileset "+pTileSet);
		tileSize = pTileSize;
		tileSet = pTileSet;
		tileSetd =  pTileSetd;
		defs = new Appearance[]{
		    createAppearance("VOID", 1,1),
		    createAppearance("SHADOW", 1,1),
		    createAppearance("CAVE_WALL", 2, 1),
			createAppearance("STONE_WALL", 3, 1),
		    createAppearance("MOSS", 4,1),
			createAppearance("CAVE_FLOOR", 5,1),
			createAppearance("STONE_FLOOR", 6,1),
			createAppearance("WATER", 7,1),
			createAppearance("DDOOR", 8,1),
			createAppearance("METAL_WALL", 1,2),
			createAppearance("AUTO_DOOR_OPEN", 2,2),
			createAppearance("AUTO_DOOR_CLOSED", 3,2),
			createAppearance("GRANITE_FLOOR", 4,2),
			createAppearance("TV_SCREEN", 5,2),
			createAppearance("TV_CAMERA", 5,2),
			createAppearance("LAVA", 7,2),
			createAppearance("TROPICAL_TREE", 8,2),
			createAppearance("MAGIC_BALL",1,11),
			createAppearance("BOOKCASE",2,11),
			createAppearance("PENTAGRAM",3,11),
			createAppearance("RFOUNTAIN_F",4,11),
			createAppearance("YFOUNTAIN_F",5,11),
			createAppearance("RSFOUNTAIN_F",6,11),
			createAppearance("YSFOUNTAIN_F",7,11),
			createAppearance("FORGE",8,11),
			createAppearance("ANKH",1,12),
			createAppearance("RFOUNTAIN",2,12),
			createAppearance("YFOUNTAIN",3,12),
			createAppearance("RSFOUNTAIN",4,12),
			createAppearance("YSFOUNTAIN",5,12),
			createAppearance("TOMB",6,12),
					
			createAppearance("GRASS_FLOOR",1,15),
			createAppearance("FOREST_TREE",2,15),
			createAppearance("TIME_MACHINE_WALL",3,15),
			createAppearance("TIME_MACHINE_FLOOR",4,15),
			createAppearance("ROCKET",5,15),
			
			// Monsters
			createAppearance("GIANT_RAT", 1,3),
			createAppearance("GIANT_BAT", 2,3),
			createAppearance("FLOATING_ORB", 3,3),
			createAppearance("SNAKE", 8,5),
			createAppearance("VIPER", 4,3),
			createAppearance("ARCHER", 5,3),
	        createAppearance("GREMLIN", 6,3),
	        createAppearance("SKELETON", 7,3),
	        createAppearance("BEAR", 8,3), /*Missing*/
	        createAppearance("ORC", 1, 4),
	        createAppearance("DANCING_DEMON", 2,4), 
	        createAppearance("LIZARDMAN", 3,4),
	        createAppearance("NECROMANCER", 4,4),
	        createAppearance("DARK_KNIGHT", 5,4),
	        createAppearance("GIANT_SPIDER", 6,4),
	        createAppearance("CARRION_CREEPER", 7,4),
	        createAppearance("PHANTOM", 8,4),
	        createAppearance("MINOTAUR", 1,5),
	        createAppearance("PURPLE_SLIME", 2,5),
	        createAppearance("LICH", 3,5),
	        createAppearance("BALRON", 4,5),
	        createAppearance("ETTIN", 5,5),
	        createAppearance("GARRINTROT", 6,5),
	        createAppearance("CHIMERA", 7,5),
	                
	        //Characters
			createAppearance("AVATAR", 1,6),
			createAppearance("AVATAR_F", 2,6),
	        
			// Items
	        createAppearance("DAGGER", 1,7),
	        createAppearance("MACE", 2,7),
	        createAppearance("AXE", 3,7),
	        createAppearance("ROPE_SPIKE", 4,7),
	        createAppearance("SWORD", 5,7),
	        createAppearance("GREAT_SWORD", 6,7),
	        createAppearance("BOW_ARROWS", 7,7),
	        createAppearance("PISTOL", 8,7),
	        createAppearance("LIGHT_SWORD", 1,8),
	        createAppearance("PHAZER", 2,8),
	        createAppearance("BLASTER", 3,8),
	        createAppearance("AMULET", 4,8),
	        createAppearance("WAND", 5,8),
	        createAppearance("STAFF", 6,8),
	        createAppearance("TRIANGLE", 7,8),
	        createAppearance("LEATHER", 1,9),
	        createAppearance("CHAIN_MAIL", 2,9),
	        createAppearance("PLATE_MAIL", 3,9),
	        createAppearance("VACUUM_SUIT", 4,9),
	        createAppearance("REFLECT_SUIT", 5,9),
	        createAppearance("YELLOW_POTION", 1,10),
	        createAppearance("RED_POTION", 2,10),
	        createAppearance("GEM", 3,10),
	        createAppearance("COIN", 5,10),
	        createAppearance("BIGCOIN", 6,10),
	        createAppearance("TORCH", 1,13), 
	        createAppearance("ENILNO", 2,13), 
	        createAppearance("BOOTS", 3,13), 
	        createAppearance("CLOAK", 4,13), 
	        createAppearance("HELM", 5,13), 
	        createAppearance("MYSTIC_GEM", 6,13), 
	        createAppearance("ANKH", 1,8), 
	        createAppearance("TRILITHIUM", 7,13), 
	        createAppearance("STRANGE_COIN", 8,13), 
	        createAppearance("BLACK_GEM", 6,14),
	        createAppearance("BLACK_POTION", 1,14),
    		createAppearance("BLUE_POTION", 2,14),
    		createAppearance("GREEN_POTION", 3,14),
    		createAppearance("ORANGE_POTION", 4,14),
    		createAppearance("WHITE_POTION", 5,14),
    		createAppearance("AMBER_GEM", 7,14),
    		createAppearance("WHITE_GEM", 8,14),
    		createAppearance("BRASS_BUTTON", 1, 16),
    		createAppearance("KEG", 1, 16),
    		createAppearance("SCROLL_RECOVER", 1, 18),
    		createAppearance("SCROLL_BLINK", 2, 18),
    		createAppearance("SCROLL_QUICKNESS", 3, 18),
    		createAppearance("SCROLL_EXPLOSION", 4, 18),
    		createAppearance("SCROLL_SUMMON", 5, 18),
    		createAppearance("SCROLL_CHARM", 6, 18),
    		createAppearance("SCROLL_INVOKE", 7, 18),
    		createAppearance("BUCKLER", 1, 19),
    		createAppearance("SMALL_SHIELD", 2, 19),
    		createAppearance("WOODEN_SHIELD", 3, 19),
    		createAppearance("TOWER_SHIELD", 4, 19),
    		createAppearance("MAGIC_SHIELD", 5, 19),
    		createAppearance("MAGIC_RING", 5, 17),
    		

    		createAppearance("BATTLE_AXE", 3,7),
    		createAppearance("LIGHT_MACE", 2,7),
    		createAppearance("HAND_AXE", 3,7),
    		createAppearance("CUDGEL", 2,7),
			createAppearance("SHORT_SWORD", 5,7),
			
			createAppearance("HAMMER", 2,16),
			createAppearance("HAMMER_JUSTICE", 3,16),
			createAppearance("BLACK_SWORD", 4,16),
			createAppearance("DWARVEN_AXE", 5,16),
			createAppearance("BOBBIT_MACE", 6,16),
			createAppearance("RONDORIN_BOW", 7,16),
			createAppearance("SCORPION_DAGGER", 8,16),
			createAppearance("OZYMANDIAS_STAFF", 1,17),
			createAppearance("ARGONAUTS_MACE", 2,17),
			createAppearance("SKULL_SUIT", 3,17),
			createAppearance("WHITE_DRAGON_SUIT", 4,17),

    		
	        
	        createAppearance("MAGIC_ARMOR", 3,9),
	        createAppearance("MAGIC_BARRIER", 7, 12),
	        createAppearance("ROCK", 6,2), 
		};
        
	}

	public Appearance[] getAppearances() {
		return defs;
	}
	
	public GFXAppearance createAppearance(String ID, int xpos, int ypos){
		xpos--;
		ypos--;
		String filename = "gfx/"+tileSet;
		String filenamed = "gfx/"+tileSetd;
		BufferedImage bigImage = (BufferedImage) images.get(filename);
		BufferedImage bigImageD = (BufferedImage) images.get(filenamed);
		if (bigImage == null){
			try {
				bigImage = ImageUtils.createImage(filename);
				bigImageD = ImageUtils.createImage(filenamed);
			} catch (Exception e){
				Game.crash("Error loading image "+filename, e);
			}
			images.put(filename, bigImage);
			images.put(filenamed, bigImageD);
		}
		try {
			BufferedImage img = ImageUtils.crearImagen(bigImage, xpos*tileSize, ypos*tileSize, tileSize, tileSize);
			BufferedImage imgD = ImageUtils.crearImagen(bigImageD, xpos*tileSize, ypos*tileSize, tileSize, tileSize);
			GFXAppearance ret = new GFXAppearance(ID, img,imgD, 0,0);
			return ret;
		} catch (Exception e){
			Game.crash("Error loading image "+filename, e);
		}
		return null;
	}
}