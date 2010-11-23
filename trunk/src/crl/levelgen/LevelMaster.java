package crl.levelgen;

import java.util.Vector;

import javax.swing.JOptionPane;

import crl.levelgen.cave.*;
import crl.levelgen.patterns.*;
import crl.item.*;
import sz.util.*;
import crl.level.*;
import crl.monster.Monster;
import crl.monster.MonsterFactory;

import crl.player.*;

import crl.cuts.Unleasher;
import crl.feature.Feature;
import crl.feature.FeatureFactory;
import crl.game.*;

public class LevelMaster {
	private static Dispatcher currentDispatcher;
	private static boolean firstCave = true;
	public static Level createLevel(String levelID, String previousLevelID, int levelNumber) throws CRLException{
		
		/*Prueba*/
		//JOptionPane.showMessageDialog(null, "createLevel "+levelID+" with former "+previousLevelID);
		Debug.enterStaticMethod("LevelMaster", "createLevel");
		Debug.say("levelID "+levelID);
		boolean overrideLevelNumber = false;
		boolean blockTime = false;
		Level ret = null;
		PatternGenerator.getGenerator().resetFeatures();
		Respawner x = new Respawner(30, 80);
		x.setSelector(new RespawnAI());
		boolean isStatic = false;
		StaticPattern pattern = null;
		if (levelID.startsWith("AVATARCELL")){
			isStatic = true;
			pattern = new AvatarCell();
		}else if (levelID.startsWith("CHIMERALAIR")){
			isStatic = true;
			pattern = new ChimeraLair();
			blockTime = true;
		}else if (levelID.startsWith("EODONPAD")){
			isStatic = true;
			pattern = new EodonPad();
		}else if (levelID.startsWith("CRASHING")){
			isStatic = true;
			pattern = new Crashing();
		}else if (levelID.startsWith("LAUNCHING")){
			isStatic = true;
			pattern = new Launching();
		}else if (levelID.startsWith("SPACE")){
			isStatic = true;
			pattern = new Space();
		}
		
		if (isStatic){
			pattern.setup(StaticGenerator.getGenerator());
			ret = StaticGenerator.getGenerator().createLevel();
			if (pattern.isHaunted()){
				ret.setHaunted(true);
				ret.setNightRespawner(x);
				ret.savePop();
				ret.getMonsters().removeAll();
				ret.getDispatcher().removeAll();
				
			}
			ret.setRespawner(x);
			ret.setInhabitants(pattern.getSpawnInfo());
			ret.setDwellerIDs(pattern.getDwellers());
			ret.setSpawnItemIDs(pattern.getItems());
			ret.setDescription(pattern.getDescription());
			ret.setHostageSafe(pattern.isHostageSafe());
			ret.setMusicKeyMorning(pattern.getMusicKeyMorning());
			ret.setMusicKeyNoon(pattern.getMusicKeyNoon());
			
			if (pattern.getBoss() != null){
				Monster monsBoss = MonsterFactory.getFactory().buildMonster(pattern.getBoss());
				monsBoss.setPosition(pattern.getBossPosition());
				ret.setBoss(monsBoss);
			}
			if (pattern.getUnleashers() != null){
				ret.setUnleashers(pattern.getUnleashers());
			}
			ret.setMapLocationKey(pattern.getMapKey());
		} 
		
		
		if (levelID.startsWith("DRASH1")){
			DrashLevelGenerator dlg = new DrashLevelGenerator();
			int gems = 0;
			boolean darkness= false;
			boolean darkMonsters = false;
			int darknessie = 0;
			if (levelNumber >= 17) {
				gems = 2;
				darknessie = 2;
			}else if (levelNumber >= 14) {
				gems = 2;
				darknessie = 1;
			} else if (levelNumber >=5){
				gems = 1;
			}
			if (levelNumber >= 9){
				darkMonsters = true;
			}
			
			if (levelNumber >= 18)
				dlg.init("METAL_WALL", "CAVE_FLOOR", "LAVA", "DDOOR", gems, darkness, darkMonsters, levelNumber);
			else if (levelNumber >= 13)
				dlg.init("CAVE_WALL", "CAVE_FLOOR", "WATER", "DDOOR", gems, darkness, darkMonsters, levelNumber);
			else if (levelNumber >= 8)
				dlg.init("CAVE_WALL", "STONE_FLOOR", "MOSS", "DDOOR", gems, darkness, darkMonsters, levelNumber);
			else if (levelNumber >= 4)
				dlg.init("STONE_WALL", "CAVE_FLOOR", "MOSS", "DDOOR", gems, darkness, darkMonsters, levelNumber);
			else 
				dlg.init("STONE_WALL", "STONE_FLOOR", "MOSS", "DDOOR", gems, darkness, darkMonsters, levelNumber);
			
			//ret = dlg.generateLevel(Util.rand(40,100),Util.rand(40,100));
			ret = dlg.generateLevel(Util.rand(40,50),Util.rand(40,50));
			//ret.setDispatcher(new Dispatcher());
			
			//System.out.println("levelnumer "+levelNumber);
			dlg.populate(ret, levelNumber);
			dlg.scatterItems(ret, levelNumber);
			ret.setDescription("Drash Arena, Lv"+levelNumber);
			ret.setDarkness(darknessie);
			String musicKey = null;
			/*if (levelNumber > 15)
				musicKey = "DRASH6";
			else if (levelNumber > 12)
				musicKey = "DRASH5";
			else if (levelNumber > 9)
				musicKey = "DRASH4";
			else if (levelNumber > 6)
				musicKey = "DRASH3";
			else if (levelNumber > 3)
				musicKey = "DRASH2";
			else 
				musicKey = "DRASH1";*/
			musicKey = "DRASH"+levelNumber;
			ret.setMusicKeyMorning(musicKey);
			ret.setMusicKeyNoon(musicKey);
		} 
		ret.setID(levelID);
		if (!overrideLevelNumber)
			ret.setLevelNumber(levelNumber);
		//if (levelID.startsWith("AVATARCELL")){
		if (isStatic){
			ret.removeActor(x);
		}
		ret.setTimeStopBlocked(blockTime);
		Debug.exitMethod(ret);
		return ret;

	}

	public static Dispatcher getCurrentDispatcher() {
		return currentDispatcher;
	}

	protected int placeKeys(Level ret){
		Debug.enterMethod(this, "placeKeys");
		//Place the magic Keys
		int keys = Util.rand(1,4);
		Position tempPosition = new Position(0,0);
		for (int i = 0; i < keys; i++){
			int keyx = Util.rand(1,ret.getWidth()-1);
			int keyy = Util.rand(1,ret.getHeight()-1);
			int keyz = Util.rand(0, ret.getDepth()-1);
			tempPosition.x = keyx;
			tempPosition.y = keyy;
			tempPosition.z = keyz;
			if (ret.isWalkable(tempPosition)){
				Feature keyf = FeatureFactory.getFactory().buildFeature("KEY");
				keyf.setPosition(tempPosition.x, tempPosition.y, tempPosition.z);
				ret.addFeature(keyf);
			} else {
				i--;
			}
		}
		Debug.exitMethod(keys);
		return keys;
		
	}
	public static void placeItems(Level ret, Player p, int levelNo){
		int items = Util.rand(2,4);
		//int items = 300;
		for (int i = 0; i < items; i++){
			Item item = ItemFactory.getItemFactory().createItemForLevel(levelNo);
			if (item == null)
				continue;
			int xrand = 0;
			int yrand = 0;
			Position pos = null;
			do {
				xrand = Util.rand(1, ret.getWidth()-1);
				yrand = Util.rand(1, ret.getHeight()-1);
				pos = new Position(xrand, yrand);
			} while (!ret.isWalkable(pos));
			//System.out.println("Placing "+item.getAttributesDescription());
			ret.addItem(pos, item);
		}
	}
	
	public static void lightCandles(Level l){
		int candles = Util.rand(25,35);
		for (int i = 0; i < candles; i++){
			int xrnd = Util.rand(1, l.getWidth() -1);
			int yrnd = Util.rand(1, l.getHeight() -1);
			if (l.getMapCell(xrnd, yrnd, 0).isSolid()){
				i--;
				continue;
			}
				
			Feature vFeature = FeatureFactory.getFactory().buildFeature("CANDLE");
			vFeature.setPosition(xrnd,yrnd,0);
			l.addFeature(vFeature);
			
		}
	}
	
}