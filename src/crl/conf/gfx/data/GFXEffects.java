package crl.conf.gfx.data;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;
import java.util.Vector;

import sz.csi.ConsoleSystemInterface;
import sz.util.ImageUtils;
import sz.util.Position;
import crl.game.Game;
import crl.ui.consoleUI.effects.CharAnimatedMissileEffect;
import crl.ui.consoleUI.effects.CharBeamMissileEffect;
import crl.ui.consoleUI.effects.CharDirectionalMissileEffect;
import crl.ui.consoleUI.effects.CharEffect;
import crl.ui.consoleUI.effects.CharFlashEffect;
import crl.ui.consoleUI.effects.CharIconEffect;
import crl.ui.consoleUI.effects.CharIconMissileEffect;
import crl.ui.consoleUI.effects.CharMeleeEffect;
import crl.ui.consoleUI.effects.CharSequentialEffect;
import crl.ui.consoleUI.effects.CharSplashEffect;
import crl.ui.effects.Effect;
import crl.ui.graphicsUI.effects.GFXAnimatedEffect;
import crl.ui.graphicsUI.effects.GFXAnimatedMissileEffect;
import crl.ui.graphicsUI.effects.GFXBeamEffect;
import crl.ui.graphicsUI.effects.GFXCircleBlastEffect;
import crl.ui.graphicsUI.effects.GFXDirectionalMissileEffect;
import crl.ui.graphicsUI.effects.GFXEffect;
import crl.ui.graphicsUI.effects.GFXFlashEffect;
import crl.ui.graphicsUI.effects.GFXIconEffect;
import crl.ui.graphicsUI.effects.GFXMeleeEffect;
import crl.ui.graphicsUI.effects.GFXSequentialEffect;
import crl.ui.graphicsUI.effects.GFXSplashEffect;

public class GFXEffects {
	private BufferedImage IMG_EFFECTS;
	private BufferedImage IMG_SLASHES;
	
	{
		
	}
		
	
	private BufferedImage[] load(int frames, int xpos, int ypos) {
		BufferedImage[] ret = new BufferedImage[frames];
		for (int x = 0; x < frames; x++){
			try {
				ret[x] = ImageUtils.crearImagen(IMG_EFFECTS, (xpos+x)*tileSize, ypos * tileSize, tileSize,tileSize);
			} catch (Exception e){
				Game.crash("Error loading effect", e);
			}
		}
		return ret;
	}
	
	private BufferedImage[] load(int frames, int xpos, int ypos, int times) {
		BufferedImage[] ret = new BufferedImage[frames*times];
		for (int x = 0; x < frames; x++){
			try {
				BufferedImage img = ImageUtils.crearImagen(IMG_EFFECTS, (xpos+x)*tileSize, ypos * tileSize, tileSize,tileSize);
				for (int i = 0; i < times; i++){
					ret[x*times+i] = img;
				}
			} catch (Exception e){
				Game.crash("Error loading effect", e);
			}
		}
		return ret;
	}
	
	private Image[] load2(int xpos, int ypos){
		return load(2, xpos, ypos);
	}
	
	private Image[] load8(int xpos, int ypos){
		return load(8, xpos, ypos);
	}
	
	private Image[] load8x(){
		BufferedImage[] ret = new BufferedImage[8];
		for (int x = 0; x < 8; x++){
			try {
				ret[x] = ImageUtils.crearImagen(IMG_SLASHES, x*16, 0, 16,16);
			} catch (Exception e){
				Game.crash("Error loading effect", e);
			}
		}
		return ret;
	}
	
	private Image[] load4(int xpos, int ypos){
		return load(4, xpos, ypos);
	}
	
	private Image[] load1(int xpos, int ypos){
		return load(1, xpos, ypos);
	}
	
	private GFXEffect [] effects;

	private int tileSize;
	//private int sfxSet;
	public GFXEffect[] getEffects(Properties p){
		tileSize = Integer.parseInt(p.getProperty("TILESIZE"));
		
		try {
			IMG_EFFECTS = ImageUtils.createImage(p.getProperty("SFXSET"));
			IMG_SLASHES = ImageUtils.createImage(p.getProperty("SFXSET2"));
		} catch (Exception e){
			Game.crash("Error loading the effects file",e);
		}
		effects = new GFXEffect[]{
				//			En Attack.java : 128 "SFX_"+weaponDef.getID()
						new GFXMeleeEffect("SFX_WP_SWORD",load8x(), 40),
						new GFXMeleeEffect("SFX_WP_ENILNO",load8x(), 20),
						new GFXMeleeEffect("SFX_WP_GREAT_SWORD",load8x(), 40),
						new GFXMeleeEffect("SFX_WP_BATTLE_AXE",load8x(), 40),
						new GFXMeleeEffect("SFX_WP_ROPE_SPIKE",load8x(), 40),
						new GFXMeleeEffect("SFX_WP_LIGHT_SWORD",load8x(), 40),
						new GFXMeleeEffect("SFX_WP_BLACK_SWORD",load8x(), 40),
						new GFXMeleeEffect("SFX_WP_DWARVEN_AXE",load8x(), 40),
						new GFXMeleeEffect("SFX_SWORD_SLASH",load8x(), 20),
						new GFXMeleeEffect("SFX_AXE_SLASH",load8x(), 20),
						
						new GFXAnimatedMissileEffect("SFX_RCP89",load(1,0,0), 20),
						new GFXAnimatedMissileEffect("SFX_THROWN_ROCK",load(1,1,0), 20),
						new GFXAnimatedMissileEffect("SFX_FIREBALL",load(1,2,0), 20),
						new GFXAnimatedMissileEffect("SFX_MAGIC_MISSILE",load(1,3,0), 20),
						new GFXAnimatedMissileEffect("SFX_LIGHT_MISSILE",load(1,4,0), 20),
						new GFXAnimatedMissileEffect("SFX_FIRE_MISSILE",load(1,5,0), 20),
						new GFXAnimatedMissileEffect("SFX_ICE_BALL",load(1,6,0), 20),
						new GFXAnimatedMissileEffect("SFX_LIGHTING",load(1,7,0), 20),
						
						new GFXAnimatedMissileEffect("SFX_MIND_MISSILE",load(1,0,1), 20),
						new GFXAnimatedMissileEffect("SFX_SLIME_BLOB",load(1,1,1), 20),
						new GFXAnimatedMissileEffect("SFX_PHANTOM_STARE",load(1,2,1), 20),
						new GFXAnimatedMissileEffect("SFX_SPIT_VENOM",load(1,3,1), 20),
						new GFXAnimatedMissileEffect("SFX_ORB_STARE",load(1,4,1), 20),
						
						new GFXDirectionalMissileEffect("SFX_WP_BOW_ARROWS",load8(0,2), 40),
						new GFXDirectionalMissileEffect("SFX_WP_PISTOL",load8(0,3), 20),
						new GFXDirectionalMissileEffect("SFX_WP_RONDORIN_BOW",load8(0,4), 20),
						new GFXAnimatedMissileEffect("SFX_WP_PHAZER",load(1,0,5), 20),
						new GFXAnimatedMissileEffect("SFX_WP_BLASTER",load(1,1,5), 20),
						
						new GFXSplashEffect("SFX_EXPLOTION",load(1,0,6,4),30), 
						new GFXSplashEffect("SFX_KEGBLAST",load(1,1,6,4),30),
						new GFXIconEffect("SFX_PLACEBLAST", load1(2,6)[0], 10),
						new GFXIconEffect("SFX_MONSTER_CRAWLING", load1(3,6)[0], 10),
						
						new GFXDirectionalMissileEffect("SFX_ARROW_SHOT",load8(0,2), 40),
					};
		return effects;
	}
}
