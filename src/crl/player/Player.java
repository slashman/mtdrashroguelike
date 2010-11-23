package crl.player;

import sz.fov.FOV;
import sz.util.*;

import java.util.*;

import crl.item.*;
import crl.action.*;
import crl.action.spells.*;
import crl.actor.*;
import crl.monster.*;
import crl.player.advancements.*;
import crl.ui.*;
import crl.level.*;
import crl.feature.*;
import crl.game.Game;
import crl.game.SFXManager;


public class Player extends Actor {
	private Game game;
	private boolean doNotRecordScore = false;
	public final static int
	SEX_MALE = 1,
	SEX_FEMALE = 2;
	
	private String name;
	private int sex;
	
	private int attack;
	private int evadeBreak;
	private int evasion;
	private int soulPower;
	private int baseSightRange;
	
	private int evadePoints;
	private int evadePointsMax;
	
	private int integrityMax;
	private int integrityPoints;
	
	private int mana;
	private int manaMax;
	
	private int carryMax;
	
	private int gems;
	private int score;
	
	//private int playerLevel;
    //private int xp;
    //private int nextLevelXP = 8000; //5000
	//private int gold;
	//private int[] weaponSkillsCounters = new int[9];
	//private int[] weaponSkills = new int[9];
	//private boolean justJumped = false;

	private int walkCost = 50;
	private int attackCost = 50;
	private int castCost = 50;
	
	
	
	private GameSessionInfo gameSessionInfo;
	
	//Status Auxiliars
	private int invisibleCount;
	private int petrifyCount;
	private int faintCount;
	
	private Hashtable hashFlags = new Hashtable();
	public void setFlag(String flagID, boolean value){
		hashFlags.put(flagID, new Boolean(value));
	}
	
	public boolean getFlag(String flagID){
		Boolean val =(Boolean)hashFlags.get(flagID); 
		return val != null && val.booleanValue();
	}
	
	public static Advancement[] ADVANCEMENTS;
	static {
		ADVANCEMENTS = new Advancement[]{
			new AdvCircle1(),
			new AdvCircle2(),
			new AdvCircle3(),
			new AdvCircle4(),
			new AdvCircle5(),
			new AdvCircle6(),
			new AdvCircle7(),
			new AdvCircle8(),
			new AdvCombatCharge(),
			new AdvCombatStun(),
			new AdvCombatHalfSlash(),
			new AdvCombatCorner(),
			new AdvCombatMirage(),
			new AdvCombatCounter(),
			new AdvCombatDrashback(),
			new AdvCombatKnockback(),
			new AdvCombatPierce(),
			new AdvCombatSlash(),
			new AdvCombatCinetic(),
			new AdvCombatPowerBlow(),
			new AdvDiscipline1(),
			new AdvDiscipline2(),
			new AdvDiscipline3(),
			new AdvDiscipline4(),
			new AdvSoul1(),
			new AdvSoul2(),
			new AdvSoul3(),
			new AdvSoul4(),
			new AdvTemper1(),
			new AdvTemper2(),
			new AdvTemper3(),
			new AdvTemper4(),
			new AdvResistance(),
			new AdvLifeBurst(),
			new AdvDrain(),
			
		};
	}
	
	private Vector tmpAvailableAdvancements = new Vector();
	
	public Vector getAvailableAdvancements(){
		tmpAvailableAdvancements.clear();
		out: for (int i = 0; i < ADVANCEMENTS.length; i++){
			if (getFlag(ADVANCEMENTS[i].getID())){
				//Already has the advancement
				continue;
			}
			String[] requirements = ADVANCEMENTS[i].getRequirements();
			for (int j = 0; j < requirements.length; j++){
				if (!getFlag(requirements[j])){
					//Misses a requirement
					continue out;
				}
			}
			tmpAvailableAdvancements.add(ADVANCEMENTS[i]);
		}
		return tmpAvailableAdvancements;
	}
	
	//Relationships
	private transient PlayerEventListener playerEventListener;

	public void addHistoricEvent(String description){
		gameSessionInfo.addHistoryItem(description);
	}
	
	private int prayerCount;
	public void addScore(int x){
		score+=x;
		prayerCount += x;
		if (prayerCount > 300){
			increasePiety(1);
			prayerCount = 0;
		}
	}
	
	public int getScore(){
		return score;
	}

	public void informPlayerEvent(int code){
		if (playerEventListener != null)
			playerEventListener.informEvent(code);
	}

	public void informPlayerEvent(int code, Object param){
		playerEventListener.informEvent(code, param);
	}
	
	
	public void selfDamage(int damageType, int dam){
		damageIntegrity(dam);
		if (isDead()){
			switch (damageType){
				case DAMAGE_WALKED_ON_LAVA:
					gameSessionInfo.setDeathCause(GameSessionInfo.BURNED_BY_LAVA);
					break;
				case DAMAGE_SHOCKED:
					gameSessionInfo.setDeathCause(GameSessionInfo.SHOCKED);
					break;
				case DAMAGE_POISON:
					gameSessionInfo.setDeathCause(GameSessionInfo.POISONED_TO_DEATH);
					break;
				case DAMAGE_LIFEBURST:
					gameSessionInfo.setDeathCause(GameSessionInfo.LIFEBURST);
					break;
				case DAMAGE_KEG:
					gameSessionInfo.setDeathCause(GameSessionInfo.KEG);
					break;
			}
			gameSessionInfo.setDeathLevel(getLevel().getLevelNumber());
			gameSessionInfo.setDeathLevelDescription(getLevel().getDescription());
			informPlayerEvent (DEATH);
		}

	}

	public void checkDeath(){
		if (isDead()) 
			informPlayerEvent (DEATH);
	}

	
	private Hashtable inventory = new Hashtable();

	public String getSecondaryWeaponDescription(){
		if (getSecondaryWeapon() != null)
			return getSecondaryWeapon().getAttributesDescription();
		else
			return "";
	}
	
	public String getEquipedWeaponDescription(){
		if (weapon != null)
			return weapon.getAttributesDescription();
		else
			return "Nothing";
	}

	public String getArmorDescription(){
		if (armor != null)
			return armor.getAttributesDescription();
		else
			return "Nothing";
	}

	public String getAccDescription(){
		if (accesory == null)
			return "Nothing";
		else
			return accesory.getAttributesDescription();
	}


	public void addItem(Item toAdd){
		if (!canCarry()){
			if (level != null){
				level.addMessage("You can't carry anything more");
				level.addItem(getPosition(), toAdd);
			}
			return;
		}
		
		String toAddID = toAdd.getFullID();
		Equipment equipmentx = (Equipment) inventory.get(toAddID);
		if (equipmentx == null)
			inventory.put(toAddID, new Equipment(toAdd, 1));
		else
			equipmentx.increaseQuantity();
		
		
	}

	private Item weapon;
	private Item secondaryWeapon;
	private Item armor;
	private Item accesory;

	public int getItemCount(){
		int eqCount = 0;
		Enumeration en = inventory.elements();
		while (en.hasMoreElements())
			eqCount += ((Equipment)en.nextElement()).getQuantity();
		return eqCount;
	}
	public boolean canCarry(){
		return getItemCount() < carryMax;
		//return true;
	}

	private void removeItem(Equipment toRemove){
		
		inventory.remove(toRemove.getItem().getFullID());
	}
	
	public boolean hasItem (Item item){
		return inventory.containsKey(item.getFullID());
	}

	/*public void removeItem(Item toRemove){
		inventory.remove(toRemove.getDefinition().getID());
	}*/

	public Vector getInventory(){
		Vector ret = new Vector();
		Enumeration x = inventory.elements();
		while (x.hasMoreElements()){
			ret.add(x.nextElement());
		}
		return ret;
	}


	public String getName() {
		return name;
	}

	private String classString;
	public String getClassString(){
		return classString;
	}

	public void setName(String value) {
		name = value;
	}

	public PlayerEventListener getPlayerEventListener() {
		return playerEventListener;
	}

	public void setPlayerEventListener(PlayerEventListener value) {
		playerEventListener = value;
	}

	public void bounceBack(Position variation, int dep){
		if (level.getMapCell(getPosition()).isStair() || isInvincible() || hasEnergyField())
			return;
		out: for (int run = 0; run < dep; run++){
			Position landingPoint = Position.add(getPosition(), variation);
			Cell landingCell = getLevel().getMapCell(landingPoint);
			
			if (landingCell == null){
				if (run < dep-1){
					continue out;
				} else {
					if (!level.isValidCoordinate(landingPoint))
						break out;
					landingPoint = level.getDeepPosition(landingPoint);
					if (landingPoint == null){
						level.addMessage("You are thrown into a endless pit!");
						gameSessionInfo.setDeathCause(GameSessionInfo.ENDLESS_PIT);
						integrityPoints = -1;
						informPlayerEvent(Player.DEATH);
						return;
					} else {
						landOn(landingPoint);
						break out;
					}
				}
			}
			
			if (landingCell != null && !landingCell.isSolid()
			 && landingCell.getHeight() <= getLevel().getMapCell(getPosition()).getHeight())
				setPosition(landingPoint);
			else
				break;
		}
	}

	public GameSessionInfo getGameSessionInfo() {
		return gameSessionInfo;
	}

	public void setGameSessionInfo(GameSessionInfo value) {
		gameSessionInfo = value;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int value) {
		sex = value;
	}

	public void updateStatus(){
		Enumeration countersKeys = hashCounters.keys();
		while (countersKeys.hasMoreElements()){
			String key = (String) countersKeys.nextElement();
			Integer counter = (Integer)hashCounters.get(key);
			if (counter.intValue() == 0){
				hashCounters.remove(key);
			} else {
				hashCounters.put(key, new Integer(counter.intValue()-1));
			}
		}
		
		if (!isRunning){
			setLastWalkingDirection(Action.SELF);
			resetChargeCounter();
			//setIsDodging(false);
		}
		if (!isAttacking){
			consecutiveHits = 0;
			lastAttackDirection = Action.SELF;
		}
		isRunning = false;
		isAttacking = false;
		
		if (isInvisible()) invisibleCount--;
    	if (hasIncreasedJumping()) jumpingCounter--;
    	if (isInvincible()) invincibleCount--;
    	if (hasEnergyField()) energyFieldCounter--;
    	if (isBloodThirsty()) bloodThirstyCounter--;
    	if (isPoisoned()){
    		if (Util.chance(50)){
    			selfDamage(DAMAGE_POISON, 3);
    		}
    		if (!isPoisoned())
    			level.addMessage("The poison leaves your blood.");
    	}
    	
    	
    	if (isPetrified()) petrifyCount--;
    	if (isFainted()) faintCount--;
    	
    	if (isPoisoned()){
    		if (Util.chance(40)){
    			level.addMessage("You feel the poison coursing through your veins!");
    			selfDamage(Player.DAMAGE_POISON, 3);
    		}
    	}
        //manaHealCount++;
        //evadeHealCount++;
        //integrityHealCount++;
        /*if (manaHealCount > 30-getSoulPower()*2){
        	recoverMana(3);
        	manaHealCount = 0;
        }
        if (evadeHealCount > 20 && Util.chance(30)){
        	recoverEvade(5);
        	evadeHealCount = 0;
        }*/
    	recoverEvade(1);
	}
	
	private int manaHealCount;
	private int evadeHealCount;
	//private int integrityHealCount;

	public void act(){
		if (hasCounter("PARALIZED")){
			//if (Util.chance(40)){
				level.addMessage("You cannot move!");
				updateStatus();
			/*}
			else
				super.act();*/
		} else if (isPetrified()){
			level.addMessage("You are petrified!");
			updateStatus();
			see();
			UserInterface.getUI().refresh();
		} else if (isFainted()){
			updateStatus();
			see();
			UserInterface.getUI().refresh();
		} else {
			super.act();
		}
		 
	}

	public void land(){
		landOn (getPosition());
	}

	private Position getFreeSquareAround(Position destinationPoint){
		Position tryP = Position.add(destinationPoint, Action.directionToVariation(Action.UP));
		if (level.getMapCell(tryP) != null && !level.getMapCell(tryP).isSolid()){
			return tryP;
		} 
		
		tryP = Position.add(destinationPoint, Action.directionToVariation(Action.DOWN));
		if (level.getMapCell(tryP) != null && !level.getMapCell(tryP).isSolid()){
			return tryP;
		}
		
		tryP = Position.add(destinationPoint, Action.directionToVariation(Action.LEFT));
		if (level.getMapCell(tryP) != null && !level.getMapCell(tryP).isSolid()){
			return tryP;
		}
					
		tryP = Position.add(destinationPoint, Action.directionToVariation(Action.RIGHT));
		if (level.getMapCell(tryP) != null && !level.getMapCell(tryP).isSolid()){
			return tryP;
		}
		return null;
	}
		
	public void landOn (Position destinationPoint){
		Debug.enterMethod(this, "landOn", destinationPoint);
		Cell destinationCell = level.getMapCell(destinationPoint);
		
        if (destinationCell == null){
        	destinationPoint = level.getDeepPosition(destinationPoint);
        	if (destinationPoint == null) {
        		level.addMessage("You fall into a endless pit!");
				gameSessionInfo.setDeathCause(GameSessionInfo.ENDLESS_PIT);
				integrityPoints = -1;
				informPlayerEvent(Player.DEATH);
				Debug.exitMethod();
				return;
        	}else {
        		destinationCell = level.getMapCell(destinationPoint);
        	}
        }
        
        setPosition(destinationPoint);
        
		if (destinationCell.getDamageOnStep() > 0){
			if (!isInvincible()){
				level.addMessage("You are injured by the "+destinationCell.getShortDescription()+"!");
				selfDamage(Player.DAMAGE_WALKED_ON_LAVA, 2);
			}
		}

		if (destinationCell.getHeightMod() != 0){
			setPosition(Position.add(destinationPoint, new Position(0,0, destinationCell.getHeightMod())));
		}
		
		

		Vector destinationItems = level.getItemsAt(destinationPoint);
		if (destinationItems != null){
			if (destinationItems.size() == 1)
				level.addMessage("There is a "+((Item)destinationItems.elementAt(0)).getDescription()+" here");
			else 
				level.addMessage("There are several items here");
		}
		Feature destinationFeature = level.getFeatureAt(destinationPoint);
		while (destinationFeature != null){
			if (destinationFeature.getHeightMod() != 0){
				setPosition(Position.add(destinationPoint, new Position(0,0, destinationFeature.getHeightMod())));
			}
			if (destinationFeature.getScorePrize() > 0){
				level.addMessage("You pickup the "+destinationFeature.getDescription()+".");
				score += destinationFeature.getScorePrize();
				level.destroyFeature(destinationFeature);
			}
			if (destinationFeature.getGoldPrize() > 0){
				level.addMessage("You pickup the "+destinationFeature.getDescription()+".");
				gold += destinationFeature.getGoldPrize();
				level.destroyFeature(destinationFeature);
			}
			if (destinationFeature.getHealPrize() > 0){
				level.addMessage("You feel much better!");
				recoverIntegrity(destinationFeature.getHealPrize());
				level.destroyFeature(destinationFeature);
			}
			if (destinationFeature.getManaPrize() > 0){
				level.addMessage("You feel power running into your body!");
				recoverMana(destinationFeature.getManaPrize());
				level.destroyFeature(destinationFeature);
			}
			if (destinationFeature.getEffect() != null){
				if (destinationFeature.getEffect().equals("REPAIR")){
					if (getArmor() != null){
						getArmor().fix(30);
						level.addMessage("You repair your armor. The magic anvil dissapears!");
						level.destroyFeature(destinationFeature);
					}
				}
				
				if (destinationFeature.getEffect().equals("ANKH")){
					level.addMessage("You feel enlightened and healed.");
					recoverIntegrity(500);
					recoverMana(500);
					level.destroyFeature(destinationFeature);
				}
			}
			
			Feature pred = destinationFeature;
			destinationFeature = level.getFeatureAt(destinationPoint);
			if (destinationFeature == pred)
				destinationFeature = null;
		}
		if (level.isExit(getPosition())){
			String exit = level.getExitOn(getPosition());
			if (exit.equals("_START")){
				//Do nothing. This must be changed with startsWith("_");
			} else if (exit.equals("_NEXT")){
				if (level.getGemCost() > gems){
					level.addMessage("The teleporter won't work, try inserting a gem on your gauntlet.");
				} else {
					gems -= level.getGemCost();
					informPlayerEvent(Player.EVT_NEXT_LEVEL);
				}
			} else if (exit.equals("_BACK")){
				//informPlayerEvent(Player.EVT_BACK_LEVEL);
			} else {
				informPlayerEvent(Player.EVT_GOTO_LEVEL, exit);
			}
			
		}
		Debug.exitMethod();
	}

	public void addGem(){
		gems++;
	}
	
	public boolean isFainted() {
		return faintCount > 0;
	}

	public void setFainted(int counter) {
		faintCount = counter;
	}
	
	public boolean isPetrified() {
		return petrifyCount > 0;
	}

	public void setPetrify(int counter) {
		petrifyCount = counter;
	}
	
	public boolean isInvisible() {
		return invisibleCount > 0;
	}

	public void setInvisible(int counter) {
		invisibleCount = counter;
	}

	public boolean isPoisoned(){
		return getCounter("POISON") > 0;
	}
	
	private int invincibleCount;

	public boolean isInvincible(){
		return invincibleCount > 0;
	}

	public void setInvincible(int counter) {
		invincibleCount = counter;
	}

	private int jumpingCounter;

	public void increaseJumping(int counter) {
		jumpingCounter = counter;
    }

    public boolean hasIncreasedJumping(){
    	return jumpingCounter > 0;
    }

    
    private int fireballWhipCounter;

	
 
	



	private int energyFieldCounter;

	public void setEnergyField(int counter){
		energyFieldCounter = counter;
	}

	public boolean hasEnergyField(){
		return energyFieldCounter > 0;
	}

	private int bloodThirstyCounter;
	public boolean isBloodThirsty(){
		return bloodThirstyCounter > 0;
	}

	public void setBloodThirsty (int t){
		bloodThirstyCounter = t;
	}



	public void reduceQuantityOf(Item what){
		String toAddID = what.getFullID();
		//System.out.println("ID "+toAddID);
		Equipment equipment = (Equipment)inventory.get(toAddID);
		equipment.reduceQuantity();
		if (equipment.isEmpty())
			removeItem(equipment);
	}

	



	public void setCarryMax(int value) {
		carryMax = value;
	}


    public Appearance getAppearance(){
		Appearance ret = super.getAppearance();
		if (ret == null){
			if (getSex()==Player.MALE)
				setAppearance(AppearanceFactory.getAppearanceFactory().getAppearance("AVATAR"));
			else
				setAppearance(AppearanceFactory.getAppearanceFactory().getAppearance("AVATAR_F"));
			ret = super.getAppearance();
		}
		return ret; 
    }

	private Vector availableSkills = new Vector(10);

	public synchronized Vector getAvailableSkills(){
		availableSkills.removeAllElements();
		availableSkills.add(skills.get("PRAYER"));
		availableSkills.add(skills.get("MMISILE"));
		
		if (getFlag("MAGIC_CIRCLE1")){
			availableSkills.add(skills.get("SLEEP"));
			availableSkills.add(skills.get("RECOVER"));
		}
		if (getFlag("MAGIC_CIRCLE2")){
			availableSkills.add(skills.get("LIGHT_MISSILE"));
			availableSkills.add(skills.get("CURE"));
			availableSkills.add(skills.get("CREATE_BARRIER"));
			availableSkills.add(skills.get("DESTROY_BARRIER"));
			availableSkills.add(skills.get("LIGHT"));
			availableSkills.add(skills.get("BLINK"));
		}
		if (getFlag("MAGIC_CIRCLE3")){
			availableSkills.add(skills.get("FIRE_MISSILE"));
			availableSkills.add(skills.get("FIRE_BARRAGE"));
			availableSkills.add(skills.get("PROTECTION"));
			availableSkills.add(skills.get("BLAST_WALL"));
		}
		if (getFlag("MAGIC_CIRCLE4")){
			availableSkills.add(skills.get("LIGHTING"));
			availableSkills.add(skills.get("INVOKE"));
			availableSkills.add(skills.get("ENERGY_ARMOR"));
			availableSkills.add(skills.get("QUICKNESS"));
			availableSkills.add(skills.get("POISON"));
		}
		if (getFlag("MAGIC_CIRCLE5")){
			availableSkills.add(skills.get("MIND_MISSILE"));
			availableSkills.add(skills.get("EXPLOSION"));
			availableSkills.add(skills.get("INVISIBILITY"));
			availableSkills.add(skills.get("GREAT_LIGHT"));
		}
		if (getFlag("MAGIC_CIRCLE6")){
			availableSkills.add(skills.get("ICEBALL"));
			availableSkills.add(skills.get("LIGHTING_STORM"));
			availableSkills.add(skills.get("SUMMON"));
			availableSkills.add(skills.get("NOXUM"));
		}
		if (getFlag("MAGIC_CIRCLE7")){
			availableSkills.add(skills.get("TREMOR"));
			availableSkills.add(skills.get("CHARM"));
			availableSkills.add(skills.get("KILL"));
			availableSkills.add(skills.get("VIEW"));
			
		}
		if (getFlag("MAGIC_CIRCLE8")){
			availableSkills.add(skills.get("XRAY"));
			availableSkills.add(skills.get("FLAME_WIND"));
			availableSkills.add(skills.get("DESTRUCTION"));
			availableSkills.add(skills.get("TIME_STOP"));
			//availableSkills.add(skills.get("ARMAGEDDON")); /*Missing*/
		}
		
		
		if (getFlag("COMBAT_CHARGE"))
			availableSkills.add(skills.get("COMBAT_CHARGE"));
		if (getFlag("COMBAT_CORNER"))
			availableSkills.add(skills.get("COMBAT_CORNER"));
		if (getFlag("COMBAT_MIRAGE"))
			availableSkills.add(skills.get("COMBAT_MIRAGE"));
		if (getFlag("COMBAT_DRASHBACK"))
			availableSkills.add(skills.get("COMBAT_DRASHBACK"));
		if (getFlag("COMBAT_STUN"))
			availableSkills.add(skills.get("COMBAT_STUN"));
		if (getFlag("COMBAT_CINETIC"))
			availableSkills.add(skills.get("COMBAT_CINETIC"));
		
		if (getFlag("COMBAT_PIERCE"))
			availableSkills.add(skills.get("COMBAT_PIERCE"));
		if (getFlag("COMBAT_KNOCKBACK"))
			availableSkills.add(skills.get("COMBAT_KNOCKBACK"));
		
		if (getFlag("COMBAT_HALFSLASH"))
			availableSkills.add(skills.get("COMBAT_HALFSLASH"));
		if (getFlag("COMBAT_COUNTER"))
			availableSkills.add(skills.get("COMBAT_COUNTER"));
		if (getFlag("COMBAT_POWER"))
			availableSkills.add(skills.get("COMBAT_POWER"));
		if (getFlag("COMBAT_SLASH"))
			availableSkills.add(skills.get("COMBAT_SLASH"));
		if (getFlag("COMBAT_RESISTANCE"))
			availableSkills.add(skills.get("COMBAT_RESISTANCE"));
		
		if (getFlag("MAGIC_LIFEDRAIN"))
			availableSkills.add(skills.get("LIFEDRAIN"));
		
		if (getFlag("MAGIC_LIFEBURST"))
			availableSkills.add(skills.get("LIFEBURST"));
		return availableSkills;
	}

	public String getWeaponDescription(){
		if (getWeapon() != null)
			if (getWeapon().getReloadTurns() > 0)
				return getWeapon().getDescription()+"("+getWeapon().getRemainingTurnsToReload()+")";
			else
				return getWeapon().getDescription();
		else
			return "None";

	}

	private final static Hashtable skills = new Hashtable();
	static{
		skills.put("RECOVER", new Skill("Recover", new Recover(), 15));
		skills.put("MMISILE", new Skill("Magic Missile", new MagicMissile(), 2));
		skills.put("PRAYER", new Skill("Prayer", new Prayer(), 1));
		skills.put("BLINK", new Skill("Blink", new Blink(), 10));
		skills.put("SLEEP", new Skill("Sleep", new Sleep(), 5));
		skills.put("LIGHT", new Skill("Light", new Light(), 10));
		skills.put("CREATE_BARRIER", new Skill("Create Barrier", new CreateBarrier(), 5));
		skills.put("DESTROY_BARRIER", new Skill("Destroy Barrier", new DestroyBarrier(), 5));
		skills.put("FIRE_BARRAGE", new Skill("Fire Barrage", new FireBarrage(), 5));
		skills.put("BLAST_WALL", new Skill("Blast Wall", new BlastWall(), 15));
		skills.put("LIGHTING", new Skill("Lighting", new Lighting(), 5));
		skills.put("ENERGY_ARMOR", new Skill("Energy Armor", new EnergyArmor(), 25));
		skills.put("EXPLOSION", new Skill("Explosion", new Explotion(), 10));
		skills.put("INVISIBILITY", new Skill("Invisibility", new Invisibility(), 10));
		skills.put("GREAT_LIGHT", new Skill("Great Light", new GreatLight(), 15));
		skills.put("LIGHTING_STORM", new Skill("Lighting Storm", new LightingStorm(), 15));
		skills.put("TREMOR", new Skill("Tremor", new Tremor(), 30));
		skills.put("FLAME_WIND", new Skill("Flame Wind", new FlameWind(), 40));
		skills.put("KILL", new Skill("Kill", new Kill(), 30));
		skills.put("TIME_STOP", new Skill("Time Stop", new TimeStop(), 30));
		skills.put("COMBAT_CHARGE", new Skill("*Combat Charge"));
		skills.put("COMBAT_CINETIC", new Skill("*Cinetic Bash"));
		skills.put("COMBAT_CORNER", new Skill("*Corner"));
		skills.put("COMBAT_KNOCKBACK", new Skill("*Knockback"));
		skills.put("COMBAT_COUNTER", new Skill("*Counterattack"));
		skills.put("COMBAT_DRASHBACK", new Skill("*DrashBack"));
		skills.put("COMBAT_MIRAGE", new Skill("*Mirage Evade"));
		skills.put("COMBAT_PIERCE", new Skill("*Piercethru"));
		skills.put("COMBAT_POWER", new Skill("*Power Blow"));
		skills.put("COMBAT_SLASH", new Skill("*Slash thru"));
		skills.put("COMBAT_HALFSLASH", new Skill("*HalfSlash thru"));
		skills.put("COMBAT_STUN", new Skill("*Stun"));
		skills.put("COMBAT_RESISTANCE", new Skill("*Resistance"));
		skills.put("LIGHT_MISSILE", new Skill("Light Missile", new LightMissile(), 3));
		skills.put("FIRE_MISSILE", new Skill("Fire Missile", new FireMissile(), 4));
		skills.put("MIND_MISSILE", new Skill("Mind missile", new MindMissile(), 5));
		skills.put("ICEBALL", new Skill("Iceball", new IceBall(), 5));
		skills.put("CURE", new Skill("Cure", new Cure(), 10));
		skills.put("PROTECTION", new Skill("Protection", new Protection(), 15));
		skills.put("QUICKNESS", new Skill("Quickness", new Quickness(), 10));
		skills.put("POISON", new Skill("Poison", new Poison(), 5));
		skills.put("CHARM", new Skill("Charm", new Charm(), 20));
		skills.put("INVOKE", new Skill("Invoke", new Invoke(), 15));
		skills.put("SUMMON", new Skill("Summon", new Summon(), 20));
		skills.put("VIEW", new Skill("View", new View(), 40));
		skills.put("XRAY", new Skill("X-Ray", new XRay(), 25));
		skills.put("NOXUM", new Skill("Mindblast", new Noxum(), 20));
		skills.put("DESTRUCTION", new Skill("<Nameless>", new Destruction(), 40));
		skills.put("LIFEDRAIN", new Skill("LifeDrain", new LifeDrain(), 0));
		skills.put("LIFEBURST", new Skill("LifeBurst", new LifeBurst(), 0));
		
	}

	public final static int DEATH = 0, WIN = 1, DROWNED = 2, KEYINMINENT = 3;

	public final static int
		EVT_FO23RWARD = 7,
		EVT_RE23TURN = 8, 
		EVT_MERCHANT = 9, 
		EVT_SMASHED = 10, 
		EVT_CHAT = 11, 
		EVT_LEVELUP = 12,
		EVT_NEXT_LEVEL = 13,
		EVT_BACK_LEVEL = 14,
		EVT_GOTO_LEVEL = 15;
	
	public final static int MALE = 1, FEMALE = 2;



	public final static int
		DAMAGE_MORPHED_WITH_STRONG_ARMOR = 0,
		DAMAGE_WALKED_ON_LAVA = 1,
		DAMAGE_USING_ITEM = 2,
		DAMAGE_POISON = 3,
		DAMAGE_JINX = 4,
		DAMAGE_SHOCKED = 5,
		DAMAGE_LIFEBURST = 6,
		DAMAGE_KEG = 7;

	public final static String 
		STATUS_STUN = "STUN",
		STATUS_POISON = "POISON",
		STATUS_PETRIFY = "PETRIFY",
		STATUS_FAINTED = "FAINTED";

	public Item getWeapon() {
		return weapon;
	}

	public void setWeapon(Item value) {
		weapon = value;
	}
	
	public Item getSecondaryWeapon() {
		return secondaryWeapon;
	}

	public void setSecondaryWeapon(Item value) {
		secondaryWeapon = value;
	}

	public Item getArmor() {
		return armor;
	}

	public void setArmor(Item value) {
		armor = value;
	}

	public Item getAccesory() {
		return accesory;
	}

	public void setAccesory(Item value) {
		accesory = value;
	}

	public String getStatusString(){
		String status = "";
		if (hasCounter("INCREASE_SIGHT"))
			status +="Light";
		if (hasCounter("INCREASE_SIGHT2"))
			status +="Light2";
		if (isCharging())
			status +="Charge";
		if (isDrashBack())
			status +="Rage";
		if (hasStoredEnergy())
			status +="Power";
		if (isDodging())
			status +="Dodging";
		if (isPoisoned())
    		status +="Poison";
		if (isProtected())
    		status +="Protect";
		if (getCounter("QUICK") > 0)
			status +="Quick";
		if (hasCounter("PARALIZED"))
			status +="Paralyzed";
		
	   	if (isInvisible())
			status +="Invisible";
		if (hasEnergyField())
			status +="Energy";
    	
    	if (hasIncreasedJumping())
    		status +="Spring";
    	if (isInvincible())
    		status +="Invin";
    	if (isBloodThirsty())
    		status +="Blood";
    	
    	if (isPetrified())
    		status +="Stone";
    	return status;
    }

	
	
	
	
	public int getSightRange(){
		return baseSightRange  + 
			(hasCounter("INCREASE_SIGHT2") ? 5 : (hasCounter("INCREASE_SIGHT") ||  hasCounter("TORCH") ? 2 : 0))+
			(getAccesory() != null && getAccesory().getDefinition().getEffectOnWear().equals("SIGHT2") ? 2 : 0)
			- level.getDarkness()
			;
	}

	public int getBaseSightRange() {
		return baseSightRange;
	}

	public void setBaseSightRange(int baseSightRange) {
		this.baseSightRange = baseSightRange;
	}


	public void setFOV(FOV fov){
		this.fov = fov;
	}
	
	private transient FOV fov;
	
	public void see(){
		fov.startCircle(getLevel(), getPosition().x, getPosition().y, getSightRange());
	}
	
	public void darken(){
		level.darken();
	}
	
	public Position getNearestMonsterPosition(){
		VMonster monsters = level.getMonsters();
		Monster nearMonster = null;
		int minDist = 150;
		for (int i = 0; i < monsters.size(); i++){
			Monster monster = (Monster) monsters.elementAt(i);
			int distance = Position.flatDistance(level.getPlayer().getPosition(), monster.getPosition());
			if (distance < minDist){
				minDist = distance;
				nearMonster = monster;
			}
		}
		if (nearMonster != null)
			return nearMonster.getPosition();
		else
			return null;
	}

	public int getAttackCost() {
		int accesoryBonus = 0;
		if (getAccesory() != null){
			if (getAccesory().getEffectOnWear().equals("COMBAT1"))
				accesoryBonus -= 10;
			else if (getAccesory().getEffectOnWear().equals("COMBAT2SOUL2")){
				accesoryBonus -= 20;
			}
		}
		int attackCosts = attackCost + 
		(weapon != null ? weapon.getAttackCost() : 0)+
		accesoryBonus;
		return attackCosts > 1  ? attackCosts : 1;
	
	}
	
	public int getBaseAttackCost(){
		return attackCost;
	}

	public void setAttackCost(int attackCost) {
		this.attackCost = attackCost;
	}

	public int getBaseCastCost(){
		return castCost;
	}
	
	public int getCastCost() {
		return castCost;
	}

	public void setCastCost(int castCost) {
		this.castCost = castCost;
	}

	public int getBaseWalkCost(){
		return walkCost;
	}
	
	public int getWalkCost() {
		int accesoryBonus = 0;
		if (getAccesory() != null){
			if (getAccesory().getEffectOnWear().equals("SPEED1"))
				accesoryBonus -= 5;
		}
		
		int walkCosts = walkCost - 
			(isCharging() ? 10 : 0) - 
			(getCounter("QUICK") > 0 ? 10 : 0) +
			accesoryBonus;
		return walkCosts > 1  ? walkCosts : 1;
	}

	public void setWalkCost(int walkCost) {
		this.walkCost = walkCost;
	}
	
	public void reduceCastCost(int param){
		this.castCost -= param;
	}

	public void increaseSoulPower(int param){
		this.soulPower += param;
	}
	
	public void reduceWalkCost(int param){
		this.walkCost -= param;
	}
	
	public void increaseCarryMax(int param){
		this.carryMax += param;
	}
	
	
	public void reduceAttackCost(int param){
		this.attackCost -= param;
	}
	
	public void increaseAttack(int param){
		this.attack += param;
	}

	public int getAttack() {
		int accesoryBonus = 0;
		if (getAccesory() != null){
			if (getAccesory().getEffectOnWear().equals("DAMAGE1"))
				accesoryBonus ++;
			else if (getAccesory().getEffectOnWear().equals("DAMAGE2"))
				accesoryBonus += 2;
			else if (getAccesory().getDefinition().getEffectOnWear().equals("COMBAT2SOUL2"))
				accesoryBonus +=2;
		}
		
		return attack + accesoryBonus;
	}
	
	public int getBaseAttack(){
		return attack;
	}
	
	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getCarryMax() {
		return carryMax;
	}
	
	public int getBaseSoulPower(){
		return soulPower;
	}
	
	public int getSoulPower() {
		int bonus =0;
		Item equip = null;
		
		for (int i = 0; i < 3; i++){
			switch (i){
			case 0:
				equip = getAccesory();
				break;
			case 1:
				equip = getWeapon();
				break;
			case 2:
				equip = getArmor();
				break;
			}
			if (equip != null){
				if (equip.getDefinition().getEffectOnWear().equals("SOUL1"))
					bonus ++;
				else if (equip.getDefinition().getEffectOnWear().equals("SOUL1MANA10"))
					bonus ++;
				else if (equip.getDefinition().getEffectOnWear().equals("SOUL2MANA20"))
					bonus +=2;
				else if (equip.getDefinition().getEffectOnWear().equals("COMBAT2SOUL2"))
					bonus +=2;
			}
		}
		return soulPower + bonus;
	}
	
	public boolean sees(Position p){
		 return level.isVisible(p.x, p.y);
	}
	 
	 public boolean sees(Monster m){
		 return sees(m.getPosition());
	 }
	 
	 public void setSoulPower(int sp){
		 this.soulPower = sp;
	 }

	public boolean isDoNotRecordScore() {
		return doNotRecordScore;
	}

	public void setDoNotRecordScore(boolean doNotRecordScore) {
		this.doNotRecordScore = doNotRecordScore;
	}
	
	

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	
	
	private boolean justEvaded;
	
	public boolean tryHit(Monster attacker){
		return tryHit(attacker, 0);
	}
	
	public boolean tryHit(Monster attacker, int damagePlus){
		//see if evades it
		boolean partiallyEvaded = false;
		int evasion = getEvasion();
		if (isDodging()){
			evasion += (100-evasion)/2.0D;
		}
		if (isParalized()){
			evasion = 0;
		}
			
		/*if (isCharging() && !getFlag("COMBAT_RESISTANCE"))
			evasion = 0;*/
		
		if (evadePoints > 0 && 
			(
				Util.chance(evasion) || (getCurrentTactic() == TACTIC_BRAVE && getFlag("COMBAT_RESISTANCE"))
			)
			){
			if (getCurrentTactic() == TACTIC_NORMAL){
				if (isDodging())
					level.addMessage("You quickly dodge the attack!");
				else
					level.addMessage("You dodge the attack!");
				SFXManager.play("wav/evade.wav");
				evadePoints -= attacker.getBreak();
				if (evadePoints < 0)
					evadePoints = 0;
				justEvaded = true;
				moveRandomly();
				return false;
			} else {
				level.addMessage("You partially evade the blow!");
				evadePoints -= attacker.getBreak();
				if (evadePoints < 0)
					evadePoints = 0;
				partiallyEvaded = true;
			}
		} 
			//UserInterface.addMessage("You are hit!");
		if (this instanceof Player){
			Player me = (Player) this;
			int damage = attacker.getDamage()+damagePlus;
			if (partiallyEvaded)
				damage = (int)((double)damage/2.0d);
			if (me.getSecondaryWeapon() != null && me.getSecondaryWeapon().getDefinition().getEquipCategory().equals(ItemDefinition.CAT_SHIELD) && Util.chance(me.getSecondaryWeapon().getCoverage())){
				level.addMessage("You block the attack with your "+me.getSecondaryWeapon().getDescription()+"!");
				me.getSecondaryWeapon().damage(damage);
			}else if (me.getArmor() != null && Util.chance(me.getArmor().getCoverage())){
				me.getArmor().damage(damage);
				level.addMessage("Your "+me.getArmor().getDescription()+" absorbs the blow!");
				SFXManager.play("wav/shot.wav");
			}else{
				level.addMessage("The "+attacker.getDescription()+" hits you!");
				switch (Util.rand(0,1)){
				case 0:
					SFXManager.play("wav/hit1.wav");
					break;
				case 1:
					SFXManager.play("wav/hit2.wav");
				}
				damageIntegrity(attacker, damage);
			}
			if (getFlag("COMBAT_COUNTER") && Util.chance(40)){
				level.addMessage("You counter attack!");
				attacker.tryHit(this, getWeapon(), false, false, Action.SELF);
			}
			return true;
		} else {
			damageIntegrity(attacker, attacker.getDamage()+damagePlus);
			return true;
		}
			
		
	}
	
	public void checkDeath(Monster damager){
		if (isDead()){
			die();
			gameSessionInfo.setDeathCause(GameSessionInfo.KILLED);
			gameSessionInfo.setKillerMonster(damager);
			gameSessionInfo.setDeathLevel(level.getLevelNumber());
			informPlayerEvent(Player.DEATH);
		}
	}
	
	private boolean isDead(){
		return integrityPoints < 1;
	}

	private void damageIntegrity(Monster damager, int damage){
		
		SFXManager.play("wav/dra_uhhh.wav");
		integrityPoints -= damage;
		
		if (isProtected()){
			level.addMessage("You feel protected!");
			damage = (int)((double)damage * 0.5d);
		}
		
		if (damage <= 0){
			level.addMessage("You shun the attack!");
			return;
		}
		if (getIntegrityPoints() < 10){
			level.addMessage("*** You are almost dead! ***");
		}
		checkDeath(damager);
	}
	
	private void damageIntegrity(int damage){
		SFXManager.play("wav/dra_uhhh.wav");
		integrityPoints -= damage;
		
		if (isProtected()){
			level.addMessage("You feel protected!");
			damage = (int)((double)damage * 0.5d);
		}
		
		if (damage <= 0){
			level.addMessage("You shun the attack!");
			return;
		}
		//checkDeath();
	}
	
	private void moveRandomly(){
		int direction = Util.rand(0,7);
		Position var = Action.directionToVariation(direction);
		Position dest = Position.add(getPosition(), var);
		if (level.isWalkable(dest) && level.getMonsterAt(dest)== null)
			//setPosition(dest);
			landOn(dest);
	}

	public int getEvadePoints() {
		return evadePoints;
	}

	public void setEvadePoints(int evadePoints) {
		this.evadePoints = evadePoints;
	}

	public int getEvadePointsMax() {
		return evadePointsMax;
	}

	public int getBaseEvadePointsMax(){
		return evadePointsMax;
	}
	
	public void setEvadePointsMax(int evadePointsMax) {
		this.evadePointsMax = evadePointsMax;
	}

	public int getBaseEvasion(){
		return evasion;
	}
	
	public int getEvasion() {
		int accesoryBonus = 0;
		if (getAccesory() != null){
			if (getAccesory().getEffectOnWear().equals("EVA5"))
				accesoryBonus += 5;
			else if (getAccesory().getEffectOnWear().equals("EVA15"))
				accesoryBonus += 15;
		}
		
		return evasion + 
			(getCounter("QUICK")>0 ? 10 : 0) +
			accesoryBonus;
	}

	public void setEvasion(int evasion) {
		this.evasion = evasion;
	}

	public int getIntegrityMax() {
		int accesoryBonus = 0;
		if (getAccesory() != null){
			if (getAccesory().getEffectOnWear().equals("INTEGRITY20"))
				accesoryBonus += 20;
		}
		return integrityMax+accesoryBonus;
	}
	
	public int getBaseIntegrityMax(){
		return integrityMax;
	}

	public void setIntegrityMax(int integrityMax) {
		this.integrityMax = integrityMax;
	}

	public int getIntegrityPoints() {
		return integrityPoints;
	}

	public void setIntegrityPoints(int integrityPoints) {
		this.integrityPoints = integrityPoints;
	}

	public int getBaseEvadeBreak(){
		return evadeBreak;
	}
	
	public int getEvadeBreak() {
		return evadeBreak;
	}

	public void setEvadeBreak(int evadeBreak) {
		this.evadeBreak = evadeBreak;
	}
	
	public void recoverEvade(int i){
		evadePoints += i;
		if (evadePoints > getEvadePointsMax())
			evadePoints = getEvadePointsMax();
    }
	
	public void recoverIntegrity(int i){
		integrityPoints += i;
		if (integrityPoints > getIntegrityMax())
			integrityPoints = getIntegrityMax();
    }
	
	public void recoverIntegrityP(int i){
		recoverIntegrity (getIntegrityMax()*i / 100);
    }
	
	public void recoverManaP(int i){
		recoverMana(getManaMax()*i / 100);
    }

	public void recoverMana(int i){
		mana += i;
		if (mana > getManaMax())
			mana = getManaMax();
    }
	
	public void reduceMana(int i){
		mana -= i;
		if (mana < 0)
			mana = 0;
	}
	
	public int getBaseManaMax(){
		return manaMax;
	}
	public int getManaMax() {
		int bonus =0;
		Item equip = null;
		
		for (int i = 0; i < 3; i++){
			switch (i){
			case 0:
				equip = getAccesory();
				break;
			case 1:
				equip = getWeapon();
				break;
			case 2:
				equip = getArmor();
				break;
			}
			if (equip != null){
				if (equip.getDefinition().getEffectOnWear().equals("SOUL2MANA10"))
					bonus +=10;
				else if (equip.getDefinition().getEffectOnWear().equals("SOUL3MANA20"))
					bonus +=20;
			}
		}
		return manaMax+bonus;
	}

	public void setManaMax(int manaMax) {
		this.manaMax = manaMax;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}
	
	private int chargeCounter;
	private int lastWalkingDirection;
	private boolean isRunning;
	
	public void setRunning(boolean val){
		isRunning = val;
	}
	
	public void increaseChargeCounter(){
		chargeCounter++;
	}
	
	public void resetChargeCounter(){
		chargeCounter = 0;
	}

	public int getLastWalkingDirection() {
		return lastWalkingDirection;
	}

	public void setLastWalkingDirection(int lastWalkingDirection) {
		this.lastWalkingDirection = lastWalkingDirection;
	}
	
	public boolean isCharging(){
		return getCurrentTactic() == TACTIC_BRAVE && getFlag("COMBAT_CHARGE") && chargeCounter > 0;
		
	}
	
	public boolean isProtected(){
		return getCounter("PROTECTION") > 0;
	}
	
	public boolean isDrashBack(){
		return getFlag("COMBAT_DRASHBACK") && consecutiveHits > 3;
	}
	
	public boolean isDodging(){
		return hasCounter("DODGE");
	}
	
	private int consecutiveHits;
	public void doHit(boolean hit){
		if (hit){
			consecutiveHits++;
		} else {
			consecutiveHits = 0;
		}
	}
	
	private int lastAttackDirection;
	public void setLastAttackDirection(int value){
		lastAttackDirection = value;
	}
	public int getLastAttackDirection(){
		return lastAttackDirection;
	}
	private boolean isAttacking;
	public void setIsAttacking(){
		isAttacking = true;
	}
	//private boolean storeEnergy;
	
	public void doNothing(){
		if (getFlag("COMBAT_POWER"))
			setCounter("STORE_ENERGY", 2);
	}
	
	public boolean hasStoredEnergy(){
		return hasCounter("STORE_ENERGY");
	}
	
	/*private boolean isDodging;
	
	public void setIsDodging(boolean val){
		isDodging = val;
	}
	
	public boolean isDodging(){
		return isDodging;
	}*/
	
	private int gold;
	
	public void addGold(int val){
		gold += val;
	}
	
	public int getGold(){
		return gold;
	}
	
	public void reduceGold(int val){
		gold -= val;
		if (gold <0)
			gold = 0;
	}
	
	public void setParalized(int count){
		if (getAccesory() != null && getAccesory().getDefinition().getEffectOnWear().equals("PROTECTPARA"))
			return;
		setCounter("PARALIZED", count);
	}
	
	public boolean isParalized(){
		return hasCounter("PARALIZED");
	}
	private int pietyCounter = 5;
	
	public void increasePiety(int x){
		pietyCounter +=x;
		if (pietyCounter> 10)
			pietyCounter = 10;
	}
	
	public void decreasePiety(int x){
		pietyCounter -=x;
	}
	
	public int getPiety(){
		return pietyCounter;
	}
	
	private int currentTactic;
	
	public final static int
		TACTIC_NORMAL = 0, TACTIC_BRAVE = 1;
	
	public void setCurrentTactic(int tactic){
		currentTactic = tactic;
	}
	
	
	public int getCurrentTactic(){
		return currentTactic;
	}
	
	public void changeTactic(){
		int nextTactic = 0;
		switch (getCurrentTactic()){
		case Player.TACTIC_BRAVE:
			nextTactic = Player.TACTIC_NORMAL;
			break;
		case Player.TACTIC_NORMAL:
			nextTactic = Player.TACTIC_BRAVE;
			break;
		}
		setCurrentTactic(nextTactic);
		UserInterface.getUI().refresh();
	}
}