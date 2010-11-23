package crl.data;

import sz.csi.ConsoleSystemInterface;
import crl.level.Cell;
import crl.ui.AppearanceFactory;
import crl.ui.consoleUI.CharAppearance;

public class Cells {
	public static Cell [] getCellDefinitions(AppearanceFactory apf){
		Cell [] ret = new Cell [28];

		ret [0] = new Cell("CAVE_WALL", "cave wall", "A natural cavern", apf.getAppearance("CAVE_WALL"), true,true);
		ret [1] = new Cell("STONE_WALL", "stone wall", "Wall", apf.getAppearance("STONE_WALL"), true,true);
		ret [2] = new Cell("MOSS", "mossy floor", "Mossy floor", apf.getAppearance("MOSS"));
		ret [3] = new Cell("CAVE_FLOOR", "cave floor", "A natural floor", apf.getAppearance("CAVE_FLOOR"));
		ret [4] = new Cell("STONE_FLOOR", "stone floor", "Floor made of stone", apf.getAppearance("STONE_FLOOR"));
		ret [5] = new Cell("WATER", "pool of water", "Pool of water", apf.getAppearance("WATER"));
		ret [6] = new Cell("DDOOR", "dimensional door", "Teleportation Device", apf.getAppearance("DDOOR"));
		ret [7] = new Cell("METAL_WALL", "metallic wall", "Shiny metal wall", apf.getAppearance("METAL_WALL"), true,true);
		ret [8] = new Cell("AUTO_DOOR_OPEN", "automatic door (open)", "Automatic Door", apf.getAppearance("AUTO_DOOR_OPEN"));
		ret [9] = new Cell("AUTO_DOOR_CLOSED", "automatic door (closed)", "Automatic Door", apf.getAppearance("AUTO_DOOR_CLOSED"), true,true);
		ret [10]= new Cell("GRANITE_FLOOR", "granite floor", "Floor made of granite", apf.getAppearance("GRANITE_FLOOR"));
		ret [11]= new Cell("TV_SCREEN", "TV Screen", "T.V. Screen", apf.getAppearance("TV_SCREEN"), true, true);
		ret [12]= new Cell("TV_CAMERA", "TV Camera", "T.V. Camera", apf.getAppearance("TV_CAMERA"), true,true);
		ret [13]= new Cell("LAVA", "Lava", "lava", apf.getAppearance("LAVA"));
		ret [14]= new Cell("TROPICAL_TREE", "tropical Tree", "Tropical Tree", apf.getAppearance("TROPICAL_TREE"));
		
		ret [15]= new Cell("MAGIC_BALL", "magic ball", "Wizards Magic Ball", apf.getAppearance("MAGIC_BALL"), true, false);
		ret [16]= new Cell("BOOKCASE", "bookcase", "Bookcase", apf.getAppearance("BOOKCASE"), true, true);
		ret [17]= new Cell("PENTAGRAM", "pentagram", "Summoning Pentagram", apf.getAppearance("PENTAGRAM"));
		
		ret [18]= new Cell("RFOUNTAIN", "red big fountain", "Red big fountain", apf.getAppearance("RFOUNTAIN"));
		ret [19]= new Cell("YFOUNTAIN", "yellow big fountain", "Yellow big fountain", apf.getAppearance("YFOUNTAIN"));
		ret [20]= new Cell("RSFOUNTAIN", "red fountain", "Red fountain", apf.getAppearance("RSFOUNTAIN"));
		ret [21]= new Cell("YSFOUNTAIN", "yellow fountain", "Yellow fountain", apf.getAppearance("YSFOUNTAIN"));
		
		ret [22]= new Cell("TOMB", "tomb", "Tomb", apf.getAppearance("TOMB"), true, false);
		
		ret [23]= new Cell("GRASS_FLOOR", "grass", "Grass Floor", apf.getAppearance("GRASS_FLOOR"));
		ret [24]= new Cell("FOREST_TREE", "tree", "Forest Tree", apf.getAppearance("FOREST_TREE"), true, true);
		ret [25]= new Cell("TIME_MACHINE_WALL", "time machine", "Time Machine Shell", apf.getAppearance("TIME_MACHINE_WALL"), true, true);
		ret [26]= new Cell("TIME_MACHINE_FLOOR", "time machine", "Time Machine", apf.getAppearance("TIME_MACHINE_FLOOR"));
		ret [27]= new Cell("ROCKET", "rocket", "Rocket", apf.getAppearance("ROCKET"));
		
		ret [5].setWater(true);
		ret [13].setDamageOnStep(20);
		return ret;
		
	}

}
