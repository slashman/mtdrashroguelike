package crl.game;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import sz.util.Debug;
import sz.util.FileUtil;
import crl.monster.MonsterDefinition;
import crl.player.Equipment;
import crl.player.GameSessionInfo;
import crl.player.HiScore;
import crl.player.MonsterDeath;
import crl.player.Player;
import crl.player.Skill;
import crl.ui.Appearance;
import crl.ui.UserInterface;

public class GameFiles {
	public static HiScore[] loadScores(){
		Debug.enterStaticMethod("GameFiles", "loadScores");
		HiScore[] ret = new HiScore[10];
        try{
            BufferedReader lectorArchivo = FileUtil.getReader("hiscore.tbl");
            for (int i = 0; i < 10; i++) {
            	String line = lectorArchivo.readLine();
            	String [] regs = line.split(";");
            	HiScore x = new HiScore();
            	x.setName(regs[0]);
            	x.setScore(Integer.parseInt(regs[1]));
            	x.setDate(regs[2]);
            	x.setTurns(regs[3]);
            	x.setDeathString(regs[4]);
            	x.setDeathLevel(Integer.parseInt(regs[5]));
            	ret[i] = x;
            }
            Debug.exitMethod(ret);
            return ret;
        }catch(IOException ioe){
        	Game.crash("Invalid or corrupt hiscore table", ioe);
    	}
    	return null;
	}
	
	public static void saveHiScore (Player player){
		Debug.enterStaticMethod("GameFiles", "saveHiscore");
		int score = player.getScore();
		String name = player.getName();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String now = sdf.format(new Date());

		HiScore [] scores = loadScores();

		try{
			BufferedWriter fileWriter = FileUtil.getWriter("hiscore.tbl");
			for (int i = 0; i < 10; i++){
				if (score > scores[i].getScore()){
           			fileWriter.write(name+";"+score+";"+now+";"+player.getGameSessionInfo().getTurns()+";"+player.getGameSessionInfo().getShortDeathString()+";"+player.getGameSessionInfo().getDeathLevel());
           			fileWriter.newLine();
            		score = -1;
            		if (i == 9)
	            		break;
            	}
            	fileWriter.write(scores[i].getName()+";"+scores[i].getScore()+";"+scores[i].getDate()+";"+scores[i].getTurns()+";"+scores[i].getDeathString()+";"+scores[i].getDeathLevel());
            	fileWriter.newLine();
            }
            fileWriter.close();
            Debug.exitMethod();
        }catch(IOException ioe){
        	ioe.printStackTrace(System.out);
			Game.crash("Invalid or corrupt hiscore table", ioe);
        }
	}

	public static void saveMemorialFile(Player player){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss");
			String now = sdf.format(new Date());
			BufferedWriter fileWriter = FileUtil.getWriter("memorials/"+player.getName()+" ("+now+").life");
			GameSessionInfo gsi = player.getGameSessionInfo();
			gsi.setDeathLevelDescription(player.getLevel().getDescription());
			String heshe = (player.getSex() == Player.MALE ? "He" : "She");
			
			fileWriter.write(" Mt Drash "+Game.getVersion()+ " Player Record");fileWriter.newLine();
			fileWriter.newLine();fileWriter.newLine();
			fileWriter.write(player.getName()+ ", "+gsi.getDeathString()+" on the "+gsi.getDeathLevelDescription()+"...");fileWriter.newLine();
			fileWriter.write(heshe+" survived for "+gsi.getTurns()+" turns and scored "+player.getScore()+".");fileWriter.newLine();
			fileWriter.newLine();
			fileWriter.write(heshe +" had the following skills:");fileWriter.newLine();
			Vector skills = player.getAvailableSkills();
			for (int i = 0; i < skills.size(); i++){
				fileWriter.write(((Skill)skills.elementAt(i)).getMenuDescription());fileWriter.newLine();
			}
			fileWriter.newLine();
			Vector history = gsi.getHistory();
			for (int i = 0; i < history.size(); i++){
				fileWriter.write(heshe + " " + history.elementAt(i));
				fileWriter.newLine();
			}
			fileWriter.newLine();
			fileWriter.write(heshe +" vanquished "+gsi.getTotalDeathCount()+" monsters");fileWriter.newLine();
			
			int i = 0;
			Enumeration monsters = gsi.getDeathCount().elements();
			while (monsters.hasMoreElements()){
				MonsterDeath mons = (MonsterDeath) monsters.nextElement();
				fileWriter.write(mons.getTimes() +" "+mons.getMonsterDescription());fileWriter.newLine();
				
				i++;
			}
			fileWriter.newLine();
			fileWriter.write("-- Final Stats --");fileWriter.newLine();fileWriter.newLine();
			
			fileWriter.write(player.getName()+" "+player.getStatusString()+" Gold: "+player.getGold()+" Score: "+player.getScore());fileWriter.newLine();
			fileWriter.write("Sex: "+ (player.getSex() == Player.MALE ? "M" : "F"));fileWriter.newLine();
			fileWriter.write(" Integrity: "+player.getIntegrityPoints()+ "/"+player.getIntegrityMax());fileWriter.newLine();
			fileWriter.write(" Break Points: "+player.getEvadePoints()+ "/"+player.getEvadePointsMax());fileWriter.newLine();
			fileWriter.write(" Mana: "+player.getMana()+ "/"+player.getManaMax());fileWriter.newLine();
			fileWriter.write("Carrying: "+player.getItemCount()+"/"+player.getCarryMax());fileWriter.newLine();
			fileWriter.write("Attack: +"+player.getBaseAttack());fileWriter.newLine();
			fileWriter.write("Break: +"+player.getBaseEvadeBreak());fileWriter.newLine();
			fileWriter.write("Soul: +"+player.getBaseSoulPower());fileWriter.newLine();
			fileWriter.write("Evasion: "+player.getBaseEvasion()+"%");fileWriter.newLine();
			fileWriter.write("Sight: "+player.getBaseSightRange());fileWriter.newLine();
			fileWriter.write("Speed: "+(player.getBaseWalkCost()-50)*-1);fileWriter.newLine();
			fileWriter.write("Combat: "+(player.getBaseAttackCost()-50)*-1);fileWriter.newLine();
			fileWriter.write("Magic: "+(player.getBaseCastCost()-50)*-1);fileWriter.newLine();
		    
			Vector inventory = player.getInventory();
			fileWriter.newLine();
			fileWriter.write("-- Inventory --");fileWriter.newLine();
			fileWriter.write("Weapon "+player.getEquipedWeaponDescription());fileWriter.newLine();
			fileWriter.write("Armor "+player.getArmorDescription());fileWriter.newLine();
			fileWriter.write("Accesory "+player.getAccDescription());fileWriter.newLine();fileWriter.newLine();
			
			for (Iterator iter = inventory.iterator(); iter.hasNext();) {
				Equipment element = (Equipment) iter.next();
				fileWriter.write(element.getQuantity()+ " - "+ element.getMenuDescription());fileWriter.newLine();
			}
			fileWriter.newLine();
			fileWriter.write("-- Last Messages --");fileWriter.newLine();
			Vector messages = UserInterface.getUI().getMessageBuffer();
			for (int j = 0; j < messages.size(); j++){
				fileWriter.write(messages.elementAt(j).toString());fileWriter.newLine();
			}
			
			fileWriter.close();
		} catch (IOException ioe){
			Game.crash("Error writing the memorial file", ioe);
		}
		
	}
	
	public static void saveGame(Game g, Player p){
		String filename = "savegame/"+p.getName()+".sav";
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename));
			p.setSelector(null);
			os.writeObject(g);
			os.close();
		} catch (IOException ioe){
			Game.crash("Error saving the game", ioe);
		}
	}
	
	public static void saveGame(Game g, Player p, boolean dontCrash){
		String filename = "savegame/"+p.getName()+".sav";
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename));
			p.setSelector(null);
			os.writeObject(g);
			os.close();
		} catch (IOException ioe){
			if (dontCrash)
				System.out.println("Error saving the game");
			else
				Game.crash("Error saving the game", ioe);
		}
	}
	
	public static void permadeath(Player p){
		String filename = "savegame/"+p.getName()+".sav";
		if (FileUtil.fileExists(filename)) {
			FileUtil.deleteFile(filename);
		}
	}
	
	public static void saveChardump(Player player){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss");
			String now = sdf.format(new Date());
			BufferedWriter fileWriter = FileUtil.getWriter("memorials/"+player.getName()+" {Alive}("+now+").life");
			GameSessionInfo gsi = player.getGameSessionInfo();
			gsi.setDeathLevelDescription(player.getLevel().getDescription());
			String heshe = (player.getSex() == Player.MALE ? "He" : "She");
			String hisher = (player.getSex() == Player.MALE ? "his" : "her");
			
			fileWriter.write(" Mt Drash "+Game.getVersion()+ " Character Dump");fileWriter.newLine();
			fileWriter.newLine();fileWriter.newLine();
			fileWriter.write(player.getName()+ ", fights for "+hisher+" freedom on the Garrintroot Arena");fileWriter.newLine();
			fileWriter.write(heshe+" has survived for "+gsi.getTurns()+" turns and has scored "+player.getScore()+".");fileWriter.newLine();
			fileWriter.newLine();
			fileWriter.write(heshe +" has the following skills:");fileWriter.newLine();
			Vector skills = player.getAvailableSkills();
			for (int i = 0; i < skills.size(); i++){
				fileWriter.write(((Skill)skills.elementAt(i)).getMenuDescription());fileWriter.newLine();
			}
			fileWriter.newLine();
			Vector history = gsi.getHistory();
			for (int i = 0; i < history.size(); i++){
				fileWriter.write(heshe + " " + history.elementAt(i));
				fileWriter.newLine();
			}
			fileWriter.newLine();
			fileWriter.write(heshe +" has vanquished "+gsi.getTotalDeathCount()+" monsters");fileWriter.newLine();
			
			int i = 0;
			Enumeration monsters = gsi.getDeathCount().elements();
			while (monsters.hasMoreElements()){
				MonsterDeath mons = (MonsterDeath) monsters.nextElement();
				fileWriter.write(mons.getTimes() +" "+mons.getMonsterDescription());fileWriter.newLine();
				
				i++;
			}
			fileWriter.newLine();
			fileWriter.write("-- Current Stats --");fileWriter.newLine();fileWriter.newLine();
			
			fileWriter.write(player.getName()+" "+player.getStatusString()+" Gold: "+player.getGold()+" Score: "+player.getScore());fileWriter.newLine();
			fileWriter.write("Sex: "+ (player.getSex() == Player.MALE ? "M" : "F"));fileWriter.newLine();
			fileWriter.write(" Integrity: "+player.getIntegrityPoints()+ "/"+player.getIntegrityMax());fileWriter.newLine();
			fileWriter.write(" Break Points: "+player.getEvadePoints()+ "/"+player.getEvadePointsMax());fileWriter.newLine();
			fileWriter.write(" Mana: "+player.getMana()+ "/"+player.getManaMax());fileWriter.newLine();
			fileWriter.write("Carrying: "+player.getItemCount()+"/"+player.getCarryMax());fileWriter.newLine();
			fileWriter.write("Attack: +"+player.getBaseAttack());fileWriter.newLine();
			fileWriter.write("Break: +"+player.getBaseEvadeBreak());fileWriter.newLine();
			fileWriter.write("Soul: +"+player.getBaseSoulPower());fileWriter.newLine();
			fileWriter.write("Evasion: "+player.getBaseEvasion()+"%");fileWriter.newLine();
			fileWriter.write("Sight: "+player.getBaseSightRange());fileWriter.newLine();
			fileWriter.write("Speed: "+(player.getBaseWalkCost()-50)*-1);fileWriter.newLine();
			fileWriter.write("Combat: "+(player.getBaseAttackCost()-50)*-1);fileWriter.newLine();
			fileWriter.write("Magic: "+(player.getBaseCastCost()-50)*-1);fileWriter.newLine();
		    
			Vector inventory = player.getInventory();
			fileWriter.newLine();
			fileWriter.write("-- Inventory --");fileWriter.newLine();
			fileWriter.write("Weapon "+player.getEquipedWeaponDescription());fileWriter.newLine();
			fileWriter.write("Armor "+player.getArmorDescription());fileWriter.newLine();
			fileWriter.write("Accesory "+player.getAccDescription());fileWriter.newLine();fileWriter.newLine();
			
			for (Iterator iter = inventory.iterator(); iter.hasNext();) {
				Equipment element = (Equipment) iter.next();
				fileWriter.write(element.getQuantity()+ " - "+ element.getMenuDescription());fileWriter.newLine();
			}
			fileWriter.newLine();
			fileWriter.write("-- Latest Messages --");fileWriter.newLine();
			Vector messages = UserInterface.getUI().getMessageBuffer();
			for (int j = 0; j < messages.size(); j++){
				fileWriter.write(messages.elementAt(j).toString());fileWriter.newLine();
			}
			
			fileWriter.close();
		} catch (IOException ioe){
			Game.crash("Error writing the character dump", ioe);
		}
		
	}
}
