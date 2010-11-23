package crl.levelgen;

import sz.ca.*;
import sz.util.*;

import java.util.*;

import crl.game.CRLException;
import crl.item.Item;
import crl.item.ItemFactory;
import crl.level.Cell;
import crl.level.Dispatcher;
import crl.level.Level;
import crl.level.MapCellFactory;
import crl.levelgen.cave.Wisp;
import crl.levelgen.cave.WispSim;
import crl.monster.Monster;
import crl.monster.MonsterFactory;

public class DrashLevelGenerator{
	private String baseWall, baseFloor, baseLava, baseDoor;
	private int gems, levelNumber;
	private boolean darkness, darkMonsters;
	
	private static Vector SFEATURES = new Vector();
	private static Vector BFEATURES = new Vector();
	static
	{
		String map[] = null;
		int minLevel = 0;
		int chance = 0;
		Hashtable charmap = null;
		LGFeature f = null;
		
		map = new String[]{
			" wwwww.wwwww ",
			"www.....2.www",
			"w..2........w",
			"w.2....1....w",
			"......c......",
			"w........2..w",
			"w...........w",
			"www.2.....www",
			" wwwww.wwwww ",
		};
		minLevel = 1;
		chance = 50;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("w", "STONE_WALL");
		charmap.put("c", "MAGIC_BALL");
		charmap.put("1", "STONE_FLOOR MONSTER NECROMANCER");
		charmap.put("2", "STONE_FLOOR MONSTER GREMLIN");
		
		f = new LGFeature(map, charmap, minLevel, chance);
		BFEATURES.add(f);
		
		map = new String[]{
			" ........... ",
			"........2....",
			"...2.........",
			"..2....1.....",
			"......c......",
			".........2...",
			".............",
			"....2........",
			" ........... ",
		};
		minLevel = 1;
		chance = 50;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("c", "MAGIC_BALL");
		charmap.put("1", "STONE_FLOOR MONSTER NECROMANCER");
		charmap.put("2", "STONE_FLOOR MONSTER GREMLIN");
		f = new LGFeature(map, charmap, minLevel, chance);
		BFEATURES.add(f);
		
		map = new String[]{
				"   wwwwwww..wwwwwww ",
				" wwppp.2....2....ww ",
				"ww................ww",
				"w..................w",
				"wb.......1........bw",
				"wb.2....3x.....2..bw",
				"wb.....1...1......bw",
				"w..................w",
				"w...2........2.....w",
				"ww................ww",
				" ww...........pppww ",
				"  wwwwwww..wwwwwww  ",
		};
		minLevel = 5;
		chance = 50;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("w", "STONE_WALL");
		charmap.put("x", "PENTAGRAM");
		charmap.put("b", "BOOKCASE");
		charmap.put("p", "STONE_FLOOR PRIZE POTION");
		charmap.put("1", "STONE_FLOOR MONSTER NECROMANCER");
		charmap.put("2", "STONE_FLOOR MONSTER SKELETON");
		charmap.put("3", "STONE_FLOOR MONSTER LICH");
		f = new LGFeature(map, charmap, minLevel, chance);
		BFEATURES.add(f);

		map = new String[]{
				"  wwwwwww..wwwwwww  ",
				" ww..222....2....ww ",
				"wwp..ww......ww...ww",
				"w.pp.ww............w",
				"wwwwww...1....wwwwww",
				"wb..2...3x.....2..bw",
				"wb.....1...1......bw",
				"wwwwww........wwwwww",
				"w...2........ww..ppw",
				"ww...........ww22pww",
				" ww..............ww ",
				"  wwwwwww..wwwwwww  ",
		};
		minLevel = 5;
		chance = 50;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("w", "STONE_WALL");
		charmap.put("x", "PENTAGRAM");
		charmap.put("b", "BOOKCASE");
		charmap.put("p", "STONE_FLOOR PRIZE POTION");
		charmap.put("1", "STONE_FLOOR MONSTER NECROMANCER");
		charmap.put("2", "STONE_FLOOR MONSTER SKELETON");
		charmap.put("3", "STONE_FLOOR MONSTER LICH");
		f = new LGFeature(map, charmap, minLevel, chance);
		BFEATURES.add(f);
		map = new String[]{
				 "  ww............ww  ", 
				 " ww..............ww ",
				 "ww....2..2..2.....ww",
				 "w..................w",
				 "w........+.........w",
				 "w....1.1+.+..1.1...w",
				 "w........+.........w",
				 "w..................w",
				 "ww.......2..2..2..ww",
				 " ww..............ww ",
				 "  ww............ww  ",

		};
		minLevel = 3;
		chance = 50;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("w", "STONE_WALL");
		charmap.put("+", "STONE_FLOOR PRIZE GOODIE3");
		charmap.put("1", "STONE_FLOOR MONSTER ARCHER");
		charmap.put("2", "STONE_FLOOR MONSTER FLOATING_ORB");
		f = new LGFeature(map, charmap, minLevel, chance);
		BFEATURES.add(f);
		
		map = new String[]{
				"   wwwwwwwwwwwwwwww   ",   
				"  ww..............ww  ",
				" ww......1..1......ww ",
				".....2..........2.....",
				".........1.+1.........",
				".....2....+.+...2.....",
				".........1.+1.........",
				".....2..........2.....",
				".........1..1........ ",
				" ww................ww ",
				"  ww..............ww  ",
				"   wwwwwwwwwwwwwwww   ",

		};
		minLevel = 3;
		chance = 50;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("w", "STONE_WALL");
		charmap.put("+", "STONE_FLOOR PRIZE GOODIE3");
		charmap.put("1", "STONE_FLOOR MONSTER ARCHER");
		charmap.put("2", "STONE_FLOOR MONSTER FLOATING_ORB");
		f = new LGFeature(map, charmap, minLevel, chance);
		BFEATURES.add(f);

		map = new String[]{
				"  wwwwwww..wwwwwww  ",  
				" wwppp........pppww ",
				"ww.....1111111....ww",
				"w.....c.....c......w",
				"wb................bw",
				"wb.......c........bw",
				"wb................bw",
				"w.....c.....c......w",
				"w..................w",
				"ww.....1111111....ww",
				" wwppp........pppww ",
				"  wwwwwww..wwwwwww  ",

		};
		minLevel = 5;
		chance = 50;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("w", "STONE_WALL");
		charmap.put("p", "STONE_FLOOR PRIZE POTION");
		charmap.put("b", "BOOKCASE");
		charmap.put("c", "MAGIC_BALL");
		charmap.put("1", "STONE_FLOOR MONSTER GIANT_RAT");
		f = new LGFeature(map, charmap, minLevel, chance);
		BFEATURES.add(f);
		
		map = new String[]{
				 "  wwwwwww..wwwwwww  ",
				 " ww..............ww ",
				 "ww.....1111111....ww",
				 "w....wwwwwwwwwww...w",
				 "w..................w",
				 "w....1...++....1...w",
				 "w....1...++....1...w",
				 "w..................w",
				 "w....wwwwwwwwwww...w",
				 "ww.....1111111....ww",
				 " ww..............ww ",
				 "  wwwwwww..wwwwwww  ",  

		};
		minLevel = 10;
		chance = 50;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("w", "STONE_WALL");
		charmap.put("+", "STONE_FLOOR PRIZE GOODIE3");
		charmap.put("1", "STONE_FLOOR MONSTER DARK_KNIGHT");
		f = new LGFeature(map, charmap, minLevel, chance);
		BFEATURES.add(f);
		
		map = new String[]{
				 " www.www ",
				 "ww.....ww",
				 "w..l.l..w",
				 "w..lfl..w",
				 "w..l.l..w",
				 "ww.....ww",
				 " www.www ",  
		};
		minLevel = 2;
		chance = 50;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("w", "STONE_WALL");
		charmap.put("l", "LAVA");
		charmap.put("f", "STONE_FLOOR FEATURE FORGE");
		f = new LGFeature(map, charmap, minLevel, chance);
		BFEATURES.add(f);
		
		map = new String[]{
				".. ....... ..",
				".............",
				" .1........1 ",
				".....x2x.....",
				"..  .2+2..3..",
				".....x2x.... ",
				" .1........1.",
				" ... ... ....", 


		};
		minLevel = 15;
		chance = 50;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("x", "PENTAGRAM");
		charmap.put("+", "STONE_FLOOR PRIZE GOODIE3");
		charmap.put("1", "STONE_FLOOR MONSTER BALRON");
		charmap.put("2", "STONE_FLOOR MONSTER LICH");
		charmap.put("3", "STONE_FLOOR MONSTER NECROMANCER");
		f = new LGFeature(map, charmap, minLevel, chance);
		BFEATURES.add(f);

		map = new String[]{
				 " www.....www ",
				 "ww.........ww",
				 "w.1.wwwww..1w",
				 "....wx2xw....",
				 ".....2+2..3..",
				 "....wx2xw....",
				 "w.1.wwwww..1.",
				 "ww.........ww",
				 " www.....www ", 


		};
		minLevel = 15;
		chance = 50;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("x", "PENTAGRAM");
		charmap.put("w", "STONE_WALL");
		charmap.put("+", "STONE_FLOOR PRIZE GOODIE3");
		charmap.put("1", "STONE_FLOOR MONSTER BALRON");
		charmap.put("2", "STONE_FLOOR MONSTER LICH");
		charmap.put("3", "STONE_FLOOR MONSTER NECROMANCER");
		f = new LGFeature(map, charmap, minLevel, chance);
		BFEATURES.add(f);
		
		map = new String[]{
				 "  ...........  ",
				 " .3.....1...c3 ",
				 " ...1........ .",
				 ".3..c...+..1...",
				 "......1......3.",
				 "........2c.... ",
				 " .c..+......c  ",
				 " ...1........ .",
				 " . .......1.   ",   


		};
		minLevel = 10;
		chance = 50;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("c", "TOMB");
		charmap.put("+", "STONE_FLOOR PRIZE GOODIE3");
		charmap.put("1", "STONE_FLOOR MONSTER PHANTOM");
		charmap.put("2", "STONE_FLOOR MONSTER LICH");
		charmap.put("3", "STONE_FLOOR MONSTER SKELETON");
		f = new LGFeature(map, charmap, minLevel, chance);
		BFEATURES.add(f);
		
		map = new String[]{
				 " ............. ",
				 " .3.....1...c3.",
				 "....1wwwwww....",
				 ".3..cw+.+..1...",
				 " ....w1...w..3.",
				 " ....w.+2cw... ",
				 "..c..wwwwww.c..",
				 " ...1......... ",
				 "  ........1..  ",    


		};
		minLevel = 10;
		chance = 50;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("c", "TOMB");
		charmap.put("w", "STONE_WALL");
		charmap.put("+", "STONE_FLOOR PRIZE GOODIE3");
		charmap.put("1", "STONE_FLOOR MONSTER PHANTOM");
		charmap.put("2", "STONE_FLOOR MONSTER LICH");
		charmap.put("3", "STONE_FLOOR MONSTER SKELETON");
		f = new LGFeature(map, charmap, minLevel, chance);
		BFEATURES.add(f);

		map = new String[]{
				  "  ..........   ",
				  ".....r...r.... ",
				  ". .............",
				  " .r.........r..",
				  "...............",
				  " ......s.......",
				  "...............",
				  "..r.........r..",
				  " ..............",
				  ".....r...r.... ",
				  " . ......... ..",   


		};
		minLevel = 12;
		chance = 50;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("r", "STONE_FLOOR FEATURE ROCK");
		charmap.put("s", "STONE_FLOOR FEATURE ANKH");
		f = new LGFeature(map, charmap, minLevel, chance);
		BFEATURES.add(f);
		
		map = new String[]{
				 "  wwwwwww..wwwwwww  ",
				 " ww..............ww ",
				 "ww.....1111111....ww",
				 "w....wwwwwwwwwww...w",
				 "w....w.........w...w",
				 "w....1...++....1...w",
				 "w....1...++....1...w",
				 "w....w.........w...w",
				 "w....wwwwwwwwwww...w",
				 "ww.....1111111....ww",
				 " ww..............ww ",
				 "  wwwwwww..wwwwwww  ",  
		};
		minLevel = 16;
		chance = 50;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("w", "STONE_WALL");
		charmap.put("+", "STONE_FLOOR PRIZE GOODIE3");
		charmap.put("1", "STONE_FLOOR MONSTER GARRINTROT");
		f = new LGFeature(map, charmap, minLevel, chance);
		BFEATURES.add(f);

		map = new String[]{
				 "  wwwwwwwwwwwwwwww  ",
				 " wwwwwwwwwwwwwwwwww ",
				 "www...123123123...www",
				 "ww...wwwwwwwwwww...ww",
				 "ww123w...+++...w321ww",
				 "ww...1...+++...1...ww",
				 "ww...1...+++...1...ww",
				 "ww...w...+++...w...ww",
				 "ww321wwwwwwwwwww123ww",
				 "www...123123123...www",
				 " wwwwwwwwwwwwwwwwwww ",
				 "  wwwwwwwwwwwwwwwww  ",  
		};
		minLevel = 17;
		chance = 50;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("w", "STONE_WALL");
		charmap.put("+", "STONE_FLOOR PRIZE GOODIE3");
		charmap.put("1", "STONE_FLOOR MONSTER ETTIN");
		charmap.put("2", "STONE_FLOOR MONSTER BALRON");
		charmap.put("3", "STONE_FLOOR MONSTER LICH");
		f = new LGFeature(map, charmap, minLevel, chance);
		BFEATURES.add(f);
		
		map = new String[]{
				" ... ",
				".....",
				"..r..",
				".....",
				" ... ",

		};
		minLevel = 1;
		chance = 60;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("r", "RSFOUNTAIN FEATURE RED_WATER");
		f = new LGFeature(map, charmap, minLevel, chance);
		SFEATURES.add(f);
		
		map = new String[]{
				" ... ",
				".....",
				"..r..",
				".....",
				" ... ",

		};
		minLevel = 1;
		chance = 60;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("r", "YSFOUNTAIN FEATURE YELLOW_WATER");
		f = new LGFeature(map, charmap, minLevel, chance);
		SFEATURES.add(f);
		
		map = new String[]{
				" ... ",
				".1.1.",
				"..r..",
				".1.1.",
				" ... ",

		};
		minLevel = 1;
		chance = 60;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("r", "RSFOUNTAIN FEATURE RED_WATER");
		charmap.put("1", "STONE_FLOOR MONSTER SNAKE");
		f = new LGFeature(map, charmap, minLevel, chance);
		SFEATURES.add(f);
		
		map = new String[]{
				" ..... ",
				".ww.ww.",
				".wrrrw.",
				".......",
				".wrrrw.",
				".ww.ww.",
				" ..... ",

		};
		minLevel = 1;
		chance = 60;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("w", "STONE_WALL");
		charmap.put("r", "STONE_FLOOR MONSTER GIANT_RAT");
		f = new LGFeature(map, charmap, minLevel, chance);
		SFEATURES.add(f);
		map = new String[]{
				 " ....... ",
				 "..s...s..",
				 ".........",
				 "....R....",
				 ".........",
				 "..s...s..",
				 " ....... ",

		};
		minLevel = 1;
		chance = 30;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("s", "STONE_FLOOR FEATURE ROCK");
		charmap.put("R", "RFOUNTAIN FEATURE RED_POOL");
		f = new LGFeature(map, charmap, minLevel, chance);
		SFEATURES.add(f);
		map = new String[]{
				 " ....... ",
				 "..s...s..",
				 ".........",
				 "....R....",
				 ".........",
				 "..s...s..",
				 " ....... ",

		};
		minLevel = 1;
		chance = 30;
		charmap = new Hashtable();
		charmap.put(".", "STONE_FLOOR");
		charmap.put("s", "STONE_FLOOR FEATURE ROCK");
		charmap.put("R", "YFOUNTAIN FEATURE YELLOW_POOL");
		f = new LGFeature(map, charmap, minLevel, chance);
		SFEATURES.add(f);
	}
	
	public void init(String baseWall, String baseFloor, String baseLava, String baseDoor, int gems, boolean darkness, boolean darkMonsters, int levelNumber){
		this.baseWall = baseWall;
		this.baseFloor = baseFloor;
		this.baseLava = baseLava;
		this.baseDoor = baseDoor;
		this.gems = gems;
		this.darkness = darkness;
		this.darkMonsters = darkMonsters;
		this.levelNumber = levelNumber;
	}
	private int xdim, ydim;
	public Level generateLevel(int xdim, int ydim) throws CRLException{
		/** Uses Cave Cellular automata, by SZ
		 * Init (1) 30%
		 * If 0 and more than 3 1 around, 1
		 * If 1 and less than 2 1 around, 0
		 * If 0 and more than 7 0 around, 2
		 * If 2 and more than 2 0 around, 0
		 * 
		 * Run for 5 turns
		 */
		this.xdim = xdim;
		this.ydim = ydim;
		CARandomInitializer vInit = new CARandomInitializer(new double [] {0.3}, true);
		CARule [] vRules = new CARule []{
			new CARule(0, CARule.MORE_THAN, 3, 1, 1),
			new CARule(1, CARule.LESS_THAN, 2, 1, 0),
			new CARule(0, CARule.MORE_THAN, 7, 0, 2),
			new CARule(2, CARule.MORE_THAN, 2, 0, 0),
		};

		int[][] intMap = null;
		
		Matrix map = new Matrix(xdim,ydim);
		vInit.init(map);
		SZCA.runCA(map, vRules, 5, false);
		intMap = map.getArrays();
		
		String[][] tiles = new String[intMap.length][intMap[0].length];
		Level ret = new Level();
		ret.setDispatcher(new Dispatcher());
		for (int x = 0; x < intMap.length; x++)
			for (int y = 0; y < intMap[0].length; y++)
				if (intMap[x][y] == 0)
					tiles[x][y] = baseFloor;
				else if (intMap[x][y] == 1)
					tiles[x][y] = baseWall;
				else if (intMap[x][y] == 2)
					tiles[x][y] = baseLava;
				else if (intMap[x][y] == 4){
					tiles[x][y] = baseFloor;
					//ret.addBlood(new Position(x,y,0), 8);
				} else
					tiles[x][y] = "HUH???";
		
		Cell[][] cells = renderLevel(tiles);
		
		Cell[][][] levelCells = new Cell[1][][];
		levelCells[0] = cells;
		
		
		ret.setCells(levelCells);
		
		Vector features = new Vector(3);
		int bigFeatures = Util.chance(50) ? 1 : 0;
		
		for (int i = 0; i < bigFeatures; i++){
			LGFeature f = (LGFeature) Util.randomElementOf(BFEATURES);
			if (!Util.chance(f.getProb())){
				i--;
				continue;
			}
				
			if (f.getMinLevel() <= levelNumber)
				features.add(f);
			else
				i--;
		}
		
		int smallFeatures = Util.rand(2,5);
		for (int i = 0; i < smallFeatures; i++){
			LGFeature f = (LGFeature) Util.randomElementOf(SFEATURES);
			if (Util.chance(f.getProb()))
				features.add(f);
			else
				i--;
		}
		
		boolean overlaps = false;
		Vector placedAreas = new Vector(5);
		for (int i = 0; i < features.size(); i++){
			LGFeature f = (LGFeature) features.elementAt(i);
			Position randomPosition = new Position (Util.rand(1,tiles[0].length-2), Util.rand(1,tiles.length-2));
			in: for (int j = 0; j < placedAreas.size(); j++){
				if (((Area)placedAreas.elementAt(j)).overlaps(new Area(randomPosition, f.getSize()))){
					overlaps = true;
					break in;
				}
			}
			if (!overlaps && isValidArea(randomPosition, f.getSize())){
				placedAreas.add(new Area(randomPosition, f.getSize()));
				renderFeature(f, randomPosition, ret);
			}
		}
		
		Position start = null;
		while (start == null){
			start = new Position(Util.rand(5, intMap.length-5), Util.rand(5, intMap[0].length - 5));
			//if (intMap[start.x][start.y] != 0)
			if (cells[start.x][start.y].isSolid() || cells[start.x][start.y].isWater()){
				start = null;
				continue;
			}
			in: for (int j = 0; j < placedAreas.size(); j++){
				if (((Area)placedAreas.elementAt(j)).contains(start.x, start.y)){
					start = null;
					break in;
				}
			}
		}
		
		Position end = null;
		while (end == null){
			end = new Position(Util.rand(5, intMap.length-5), Util.rand(5, intMap[0].length - 5));
			//if (intMap[end.x][end.y] != 0 || Position.flatDistance(start, end) < xdim / 2){
			if (cells[end.x][end.y].isSolid() || cells[end.x][end.y].isWater() || Position.flatDistance(start, end) < xdim / 2){
				end = null;
			}
		}
		levelCells[0][end.x][end.y] = MapCellFactory.getMapCellFactory().getMapCell(baseDoor);
		Vector gemsV = new Vector();
		//Put the gems
		for (int i = 0; i < gems; i++){
			int xpos = Util.rand(0,intMap.length-1);
			int ypos = Util.rand(0,intMap[0].length-1);
			//if (intMap[xpos][ypos] == 0){
			if (!cells[xpos][ypos].isSolid() && !cells[xpos][ypos].isWater()){
				gemsV.add(new Position(xpos, ypos));
			} else {
				i--;
			}
		}
		
		Cell cbaseWall = MapCellFactory.getMapCellFactory().getMapCell(baseWall);
		Cell cbaseFloor = MapCellFactory.getMapCellFactory().getMapCell(baseFloor);
		for (int i = 1; i < gems; i++){
			Position gem1 = (Position)gemsV.elementAt(i);
			Position gem2 = null;
			if (i+1 == gems)
				gem2 = (Position)gemsV.elementAt(0);
			else
				gem2 = (Position)gemsV.elementAt(i);
				
			WispSim.setWisps(new Wisp(gem1, 120,30,3),new Wisp(gem2, 100,30,3));
			WispSim.run(ret.getCells()[0], cbaseFloor);
		}
		if (gems > 0){
			Position gem1 = (Position)gemsV.elementAt(0);
			WispSim.setWisps(new Wisp(gem1, 120,30,3),new Wisp(start, 100,30,3));
			WispSim.run(ret.getCells()[0], cbaseFloor);
		}
		WispSim.setWisps(new Wisp(start, 120,30,3),new Wisp(end, 100,30,3));
		WispSim.run(ret.getCells()[0], cbaseFloor);
		
		for (int i = 0; i < gems; i++){
			Item gem = ItemFactory.getItemFactory().createItem("GEM");
			ret.addItem((Position)gemsV.elementAt(i), gem);
		}
		
		ret.setGemCost(gems);
		ret.addExit(start, "_BACK");
		ret.addExit(end, "_NEXT");
		//populate(ret);
		return ret;
	}
	
	private boolean isValidArea(Position where, Position dimension){
		return (where.x() > 0 && 
				where.y() > 0 && 
				where.x() + dimension.x() < xdim && 
				where.y() + dimension.y() < ydim);
	}
	
	private void renderFeature(LGFeature f, Position where, Level l){
		StaticGenerator.getGenerator().renderOverLevel(l, f.getMap(), f.getTable(), where, levelNumber);
	}
	
	protected Cell[][] renderLevel(String[][] cellIds) throws CRLException{
		MapCellFactory mcf = MapCellFactory.getMapCellFactory();
		Cell[][] ret = new Cell[cellIds.length][cellIds[0].length];
		for (int x = 0; x < cellIds.length; x++)
			for (int y = 0; y < cellIds[0].length; y++){
				//System.out.println(cellIds[x][y]);
				ret[x][y] = mcf.getMapCell(cellIds[x][y]);
			}
		return ret;
	}
	
	public void populate(Level ret, int levelNumber){
		int pop = Util.rand(10,15);
		for (int i = 0; i < pop; i++){
			Monster monster = MonsterFactory.getFactory().getMonsterForLevel(levelNumber);
			Position loc = null;
			while (loc == null){
				loc = new Position(Util.rand(1, ret.getWidth()), Util.rand(1, ret.getHeight()));
				if (!ret.isWalkable(loc) || ret.getExitOn(loc)!= null)
					loc = null;
			}
			monster.setPosition(loc);
			ret.addActor(monster);
		}
		
	}
	
	public void scatterItems(Level ret, int levelNumber){
		int items = Util.rand(6,8);
		for (int i = 0; i < items; i++){
			Position tempPosition = new Position(0,0);
			while(true) {
				int cluex = Util.rand(1,ret.getWidth()-1);
				int cluey = Util.rand(1,ret.getHeight()-1);
				int cluez = Util.rand(0, ret.getDepth()-1);
				tempPosition.x = cluex;
				tempPosition.y = cluey;
				tempPosition.z = cluez;
				if (ret.isWalkable(tempPosition) && ret.getMapCell(tempPosition).getDamageOnStep() == 0){
					Item clue = ItemFactory.getItemFactory().createItemForLevel(levelNumber);
					if (clue != null)
						ret.addItem(tempPosition, clue);
					break;
				}
			}
		}
	}
}


class LGFeature {
	private Position size;
	private String[] map;
	private Hashtable table;
	
	private int prob;
	private int minLevel;
	public String[] getMap() {
		return map;
	}
	public LGFeature(String[] map, Hashtable table, int minLevel, int prob) {
		this.map = map;
		this.table = table;
		this.minLevel = minLevel;
		this.prob = prob;
		size = new Position(map[0].length(), map.length);
	}
	public Position getSize() {
		return size;
	}
	
	public Hashtable getTable(){
		return table;
	}
	public int getMinLevel() {
		return minLevel;
	}
	public int getProb() {
		return prob;
	}
	
	
}