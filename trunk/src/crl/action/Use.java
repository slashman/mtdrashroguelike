package crl.action;

import java.util.Vector;

import sz.util.Line;
import sz.util.Position;
import sz.util.Util;
import crl.action.spells.Recover;
import crl.action.spells.TimeStop;
import crl.action.spells.View;
import crl.action.spells.XRay;
import crl.feature.Feature;
import crl.item.ItemDefinition;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.monster.MonsterFactory;
import crl.player.Equipment;
import crl.player.Player;
import crl.ui.ActionCancelException;
import crl.ui.UserInterface;
import crl.ui.effects.EffectFactory;

public class Use extends Action{
	public String getID(){
		return "Use";
	}
	
	public boolean needsItem(){
		return true;
    }

    public String getPromptItem(){
    	return "What do you want to use?";
	}

	public void execute(){

		Player aPlayer = (Player) performer;
		ItemDefinition def = targetItem.getDefinition();
		String[] effect = def.getEffectOnUse().split(" ");
		
		if (def.getID().equals("SOUL_RECALL")){
			aPlayer.informPlayerEvent(Player.EVT_GOTO_LEVEL, "TOWN");
			aPlayer.getLevel().setLevelNumber(0);
			aPlayer.landOn(Position.add(aPlayer.getLevel().getExitFor("_NEXT"), new Position(-1,0,0)));
			aPlayer.reduceQuantityOf(targetItem);
			return;
		}
		
		if (def.getID().equals("BRASS_BUTTON")){
			if (aPlayer.getLevel().getMapCell(aPlayer.getPosition()).getID().equals("ROCKET")){
				Vector inventory = aPlayer.getInventory();
				for (int i = 0; i < inventory.size(); i++){
					Equipment eq = (Equipment)inventory.elementAt(i);
					if (eq.getItem().getDefinition().getID().equals("TRILITHIUM")){
						if (eq.getQuantity() >= 15){
							for (int ii = 0; ii < 15	; ii++){
								aPlayer.reduceQuantityOf(eq.getItem());
							}
							int levelNumber = aPlayer.getLevel().getLevelNumber();
							aPlayer.addHistoricEvent("reached planet X!");
							aPlayer.informPlayerEvent(Player.EVT_GOTO_LEVEL, "SPACE");
							aPlayer.getLevel().setLevelNumber(levelNumber);
							aPlayer.setPosition(aPlayer.getLevel().getExitFor("_BACK"));
							return;
						}
					}
				}
				
			}
		}
		
		if (def.getID().equals("KEG")){
			aPlayer.getLevel().addMessage("You light the Powder Keg! RUN!");
			aPlayer.getLevel().addSmartFeature("KEG", aPlayer.getPosition());
			aPlayer.reduceQuantityOf(targetItem);
			return;
		}
		
		if (effect[0].equals("")){
			performer.getLevel().addMessage("You don\'t find a use for the "+targetItem.getDescription());
			//aPlayer.addItem(targetItem);
			return;
		}       
		if (effect[0].equals("GEMS")){
			aPlayer.addGem();
		}
		
		for (int cmd = 0; cmd < effect.length; cmd+=2){
			String message = targetItem.getUseMessage();
			if (message.equals(""))
				message = "You use the "+targetItem.getDescription();
			performer.getLevel().addMessage(message);
			if (effect[cmd].equals("INCREASE_DEFENSE"))
				aPlayer.setCounter("PROTECTION", Integer.parseInt(effect[cmd+1]));
			else
			if (effect[cmd].equals("INVINCIBILITY"))
				aPlayer.setInvincible(Integer.parseInt(effect[cmd+1]));
			else
			if (effect[cmd].equals("INVISIBILITY"))
				aPlayer.setInvisible(Integer.parseInt(effect[cmd+1]));
			else
			if (effect[cmd].equals("ENERGY_FIELD"))
				aPlayer.setEnergyField(Integer.parseInt(effect[cmd+1]));
			else
			
			if (effect[cmd].equals("INCREASE_JUMPING"))
				aPlayer.increaseJumping(Integer.parseInt(effect[cmd+1]));
			else
			if (effect[cmd].equals("RECOVERP"))
				aPlayer.recoverIntegrityP(Integer.parseInt(effect[cmd+1]));
			else
			if (effect[cmd].equals("RECOVER"))
				aPlayer.recoverIntegrity(Integer.parseInt(effect[cmd+1]));
			else
			if (effect[cmd].equals("RECOVERMANAP"))
				aPlayer.recoverManaP(Integer.parseInt(effect[cmd+1]));
			else
			if (effect[cmd].equals("RECOVERMANA"))
				aPlayer.recoverMana(Integer.parseInt(effect[cmd+1]));
			else
			if (effect[cmd].equals("SIGHT"))
				aPlayer.setCounter("TORCH", Integer.parseInt(effect[cmd+1]));
			else
			if (effect[cmd].equals("CAST")){
				try {
					if (effect[cmd+1].equals("VIEW")){
						Position runner = new Position(0,0);
						for (int i = 0; i < 100; i++){
							runner.x = Util.rand(1,performer.getLevel().getWidth()-1);
							runner.y = Util.rand(1,performer.getLevel().getHeight()-1);
							performer.getLevel().setSeen(runner.x, runner.y);
						}
					} else if (effect[cmd+1].equals("STOP_TIME")){
						performer.getLevel().stopTime(5 + aPlayer.getSoulPower()*4);
					} else if (effect[cmd+1].equals("XRAY")){
						XRay action = new XRay();
						action.setPerformer(performer);
						action.execute();
					} else if (effect[cmd+1].equals("RECOVER")){
						Player player = (Player)performer;
						player.getLevel().addMessage("You feel better");
						player.recoverIntegrityP(10+player.getSoulPower()*3);
						player.recoverEvade(10+player.getSoulPower()*3);
					} else if (effect[cmd+1].equals("BLINK")){
						Player player = (Player) performer;
						Level level = player.getLevel();
						level.addMessage("HURTIS MATERIA!");
						targetPosition = UserInterface.getUI().pickPosition("WHERE?");
						if (targetPosition.equals(performer.getPosition())){
				        	return;
				        }
						drawEffect(EffectFactory.getSingleton().createLocatedEffect(player.getPosition(), "SFX_FLASH_TELEPORT"));
						Line line = new Line(player.getPosition(), targetPosition);
						Position runner = line.next();
						int i = 0;
						for (; i < 10; i++){
							runner = line.next();
				        	Cell destinationCell = performer.getLevel().getMapCell(runner);
							if (
								(
								!level.isSolid(runner) &&
								(destinationCell.getHeight() == level.getMapCell(player.getPosition()).getHeight() ||
								destinationCell.getHeight() -1  == level.getMapCell(player.getPosition()).getHeight() ||
								destinationCell.getHeight() == level.getMapCell(player.getPosition()).getHeight()-1))
							)
								player.setPosition(new Position(runner));
							else
								break;
						}
						//drawEffect(new LineMissileEffect(start, "\\|/--/|\\", Appearance.WHITE, start, targetPosition, i, 30));
						drawEffect(EffectFactory.getSingleton().createDirectedEffect(performer.getPosition(), targetPosition, "SFX_TELEPORT", i));
						player.see();
					} else if (effect[cmd+1].equals("QUICKNESS")){
						Player player = (Player)performer;
						player.getLevel().addMessage("HURTIS HURTIS!");
						player.setCounter("QUICK", 25);
					} else if (effect[cmd+1].equals("EXPLOSION")){
						targetPosition = UserInterface.getUI().pickPosition("WHERE?");
						Level aLevel = performer.getLevel();
						aLevel.addMessage("FUEGO GRAVITA!");
						
						if (Position.distance(aPlayer.getPosition(), targetPosition) > 4){
							aLevel.addMessage("Too far.");
							return;
						}
						int damage = 1 + 3 * aPlayer.getSoulPower();
	
						aLevel.addEffect(EffectFactory.getSingleton().createLocatedEffect(targetPosition, "SFX_EXPLOTION"));
						
						
						for (int x = targetPosition.x -1; x <= targetPosition.x+1; x++)
							for (int y = targetPosition.y -1; y <= targetPosition.y+1; y++){
								Position destinationPoint = new Position(x,y);
								Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
					        	if (destinationFeature != null && destinationFeature.isDestroyable()){
									destinationFeature.damage(aPlayer, damage);
						        }
								Monster targetMonster = performer.getLevel().getMonsterAt(destinationPoint);
								if (targetMonster != null){
									targetMonster.tryMagicHit(aPlayer, damage, damage, 120, targetMonster.wasSeen(), "fireball");
								}
							}
						aLevel.addMessage(message);
					} else if (effect[cmd+1].equals("INVOKE")){
						Level aLevel = performer.getLevel();
						aLevel.addMessage("KAM!");
						Position randPos = null;
						if (Util.chance(75)){
							int count = 50;
							out: while (count > 0){
								int xran = 1-Util.rand(0,2);
								int yran = 1-Util.rand(0,2);
								
								randPos = Position.add(performer.getPosition(), new Position(xran, yran));
								if (aLevel.isWalkable(randPos) && aLevel.getActorAt(randPos) == null){
									break out;
								}
								count--;
							}
							if (count > 0){
								Monster m = MonsterFactory.getFactory().buildMonster(Util.randomElementOf(new String[] {
										"GIANT_SPIDER",
										"PHANTOM",
										"LIZARDMAN",
										"ORC"
									}));
								aLevel.addMessage("A "+m.getDescription()+" rises from nowhere!");
								m.setCounter("CHARM", 9999999);
								m.setPosition(randPos);
								aLevel.addMonster(m);
							} else {
								aLevel.addMessage("You feel an energy shock!");
							}
						} else {
							aLevel.addMessage("Nothing happens.");
						}
					}else if (effect[cmd+1].equals("SUMMON")){
						Level aLevel = performer.getLevel();
						aLevel.addMessage("KAM KALAM!");
						Position randPos = null;
						if (Util.chance(75)){
							int count = 50;
							out: while (count > 0){
								int xran = 1-Util.rand(0,2);
								int yran = 1-Util.rand(0,2);
								
								randPos = Position.add(performer.getPosition(), new Position(xran, yran));
								if (aLevel.isWalkable(randPos) && aLevel.getActorAt(randPos) == null){
									break out;
								}
								count--;
							}
							if (count > 0){
								Monster m = MonsterFactory.getFactory().buildMonster(Util.randomElementOf(new String[] {
										"ETTIN",
										"BALRON",
										"LICH",
										"PHANTOM"
									}));
								aLevel.addMessage("A "+m.getDescription()+" rises from nowhere!");
								m.setCounter("CHARM", 9999999);
								m.setPosition(randPos);
								aLevel.addMonster(m);
							} else {
								aLevel.addMessage("You feel an energy shock!");
							}
						} else {
							aLevel.addMessage("Nothing happens.");
						}
					} else if (effect[cmd+1].equals("CHARM")){
						targetPosition = UserInterface.getUI().pickPosition("WHO?");
						Level x = performer.getLevel();
						x.addMessage("MENI MANIPULA!");
						Monster m = x.getMonsterAt(targetPosition);
						if (m == null){
							x.addMessage("Nothing happens.");
						} else {
							if (Util.chance(75)){
								if (m.isMagus()){
									x.addMessage("The "+m.getDescription()+" resists the effects!");
								} else {
									x.addMessage("The "+m.getDescription()+" turns to help!");
									m.setCounter("CHARM", 5+aPlayer.getSoulPower()*5);
								}
							} else {
								x.addMessage("The "+m.getDescription()+" wasn't charmed.");
							}
							
						}
					}
				} catch (ActionCancelException ace){
					aPlayer.getLevel().addMessage("Cancelled");
				}
				
				/*
				 * RECOVER
				 * BLINK
				QUICKNESS
				EXPLOSION
				SUMMON
				CHARM
				INVOKE*/
				
			}else
			if (effect[cmd].equals("DAMAGE")) {
				if (aPlayer.isInvincible())
					aPlayer.getLevel().addMessage("The damage is repelled!");
				else
					aPlayer.selfDamage(Player.DAMAGE_USING_ITEM, Integer.parseInt(effect[cmd+1]));
			} else
			if (effect[cmd].equals("POISON")) {
				aPlayer.setCounter("POISON", 10);
			} else
			if (effect[cmd].equals("CURE")) {
				if (aPlayer.isPoisoned())
					aPlayer.setCounter("POISON", 0);
			}else 
			if (effect[cmd].equals("BLACK_GEM")){
				aPlayer.setFlag("BLACK_GEM", true);
			} else
			if (effect[cmd].equals("WHITE_GEM")){
				aPlayer.setFlag("WHITE_GEM", true);
			} else 
			if (effect[cmd].equals("AMBER_GEM")){
				aPlayer.setFlag("AMBER_GEM", true);
			}
		}
		if (def.isSingleUse())
			aPlayer.reduceQuantityOf(targetItem);


	}

	



}