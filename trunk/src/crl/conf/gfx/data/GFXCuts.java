package crl.conf.gfx.data;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import sz.util.Debug;
import sz.util.ImageUtils;

import crl.conf.console.data.CharCuts;
import crl.game.Game;
import crl.ui.consoleUI.cuts.CharChat;
import crl.ui.graphicsUI.GFXChat;

public class GFXCuts {
	public static GFXCuts thus;
	private BufferedImage
		PRT_DRACULA, PRT_CHRIS, PRT_SOLIEYU, PRT_SOLIEYU_D, PRT_MELDUCK, PRT_CLARA,PRT_MAIDEN;
	public BufferedImage
		PRT_M1, PRT_M2, PRT_M3, PRT_M4, PRT_M5, PRT_M6,
		PRT_F1, PRT_F2, PRT_F3, PRT_F4, PRT_F5, PRT_F6;
	{
		try {
			BufferedImage PRT = ImageUtils.createImage("gfx/crl_portraits2x.gif");
			PRT_DRACULA = ImageUtils.crearImagen(PRT, 8, 205, 84, 86);
			PRT_CHRIS = ImageUtils.crearImagen(PRT, 368, 205, 84, 86);
			PRT_SOLIEYU = ImageUtils.crearImagen(PRT, 188, 205, 84, 86);
			PRT_SOLIEYU_D = ImageUtils.crearImagen(PRT, 278, 204, 84, 86);
			PRT_MELDUCK = ImageUtils.crearImagen(PRT, 188, 303, 84, 86);
			PRT_CLARA = ImageUtils.crearImagen(PRT, 8, 303, 84, 86);
			PRT_MAIDEN = ImageUtils.crearImagen(PRT, 98, 303, 84, 86);
			PRT_M1 = ImageUtils.crearImagen(PRT, 8, 10, 84, 86);
			PRT_M2 = ImageUtils.crearImagen(PRT, 98, 10, 84, 86);
			PRT_M3 = ImageUtils.crearImagen(PRT, 188, 10, 84, 86);
			PRT_M4 = ImageUtils.crearImagen(PRT, 278, 10, 84, 86);
			PRT_M5 = ImageUtils.crearImagen(PRT, 368, 10, 84, 86);
			PRT_M6 = ImageUtils.crearImagen(PRT, 458, 10, 84, 86);
			PRT_F1 = ImageUtils.crearImagen(PRT, 8, 107, 84, 86);
			PRT_F2 = ImageUtils.crearImagen(PRT, 98, 107, 84, 86);
			PRT_F3 = ImageUtils.crearImagen(PRT, 188, 107, 84, 86);
			PRT_F4 = ImageUtils.crearImagen(PRT, 278, 107, 84, 86);
			PRT_F5 = ImageUtils.crearImagen(PRT, 368, 107, 84, 86);
			PRT_F6 = ImageUtils.crearImagen(PRT, 458, 107, 84, 86);
		} catch (Exception iae){
			Game.crash(iae.getMessage(), iae);
		}
	}
	public static void initializeSingleton(){
		thus = new GFXCuts();
	}
	private Hashtable hashCuts = new Hashtable();
	{
		GFXChat temp = null;
		temp = new GFXChat();
		temp.add("Count Dracula", "We meet again Vampire Killer. You are old now.", PRT_DRACULA);
		temp.add("Christopher Belmont", "I came here to fulfill my fate as a Belmont; age bears no relevance.", PRT_CHRIS);
		temp.add("Count Dracula", "Look at your own self! And look at me, just reborn from warm innocent blood, you stand no chance against my power!", PRT_DRACULA);
		temp.add("Christopher Belmont", "It is for that one same blood that my whip shall seek revenge against thee, dark lord.", PRT_CHRIS);
		temp.add("Count Dracula", "HAHAHAHA! Don't make me laugh, pitiful excuse for a warrior, you shall regret these words!", PRT_DRACULA);
		hashCuts.put("PRELUDE_DRACULA1", temp);
		temp = new GFXChat();
		temp.add("Solieyu Belmont", "Father! I finally understand, I am here to confront my destiny, the destiny marked by my legacy!", PRT_SOLIEYU);
		temp.add("Solieyu Belmont", "... Father? FATHER!", PRT_SOLIEYU);
		temp.add("Count Dracula", "Your father belongs to me now, you are late, son of a Belmont", PRT_DRACULA);
		temp.add("Solieyu Belmont", "No! NO! this.. this cannot be! You miserable monster! Die!", PRT_SOLIEYU);
		temp.add("Count Dracula", "HAHAHAHA!", PRT_DRACULA);
		hashCuts.put("PRELUDE_DRACULA2", temp);
		temp = new GFXChat();
		temp.add("Count Dracula", "Don't be such a fool! Your soul has always been my possession! ", PRT_DRACULA);
		temp.add("Solieyu Belmont", "What! What is this! What happens to my body! ARGH!", PRT_SOLIEYU_D);
		temp.add("Solieyu Belmont", "WARGH!!! WARRRGH!", PRT_SOLIEYU_D);
		temp.add("Count Dracula", "HAHAHAHA! Now, get out of my sight shameful creature! I have important things to do", PRT_DRACULA);
		hashCuts.put("PRELUDE_DRACULA3", temp);
		temp = new GFXChat();
		temp.add("Melduck", "We couldn't be on a worse situation... with the charriot like this we cannot make it to Petra, and this creepy forest...", PRT_MELDUCK);
		temp.add("Melduck", ". . . wait . . . Did you hear that?", PRT_MELDUCK);
		temp.add("%NAME", "This forest... I can feel... it is suffering...");
		temp.add("%NAME", "We are running out of time, do you have a clue about the castle location?");
		temp.add("Melduck", "Petra is to the northwest, and the castle of Dracula is just east of there... you better proceed on foot... but quickly, I'm afraid I can't get past the forest with my feet like this...", PRT_MELDUCK);
		temp.add("%NAME", "I will come back with help, dont worry");
		hashCuts.put("INTRO_1", temp);
		temp = new GFXChat();
		temp.add("???", "Who are you?", PRT_CLARA);
		temp.add("%NAME", "I am %NAME");
		temp.add("???", "Leave this castle while you can, this is not a place for humans!", PRT_CLARA);
		temp.add("%NAME", "I can't, my fate is to go in and confront the dark count!");
		temp.add("???", "Don't make me laugh! You don't even know what awaits you inside stranger!", PRT_CLARA);
		temp.add("???", "I will go in, we will meet inside, if you survive!", PRT_CLARA);
		hashCuts.put("CLARA1", temp);
		temp = new GFXChat();
		temp.add("%NAME", "What is this, a garden inside this foul castle?");
		temp.add("%NAME", "And here we have yet another evil forger of darkness whom soul must be freed!");
		temp.add("???", ". . . Stop where you are, son of a Belmont!", PRT_MAIDEN);
		temp.add("%NAME", "What? How do you...");
		temp.add("???", "You have come pretty far on the castle; this place is safe for you, for now.", PRT_MAIDEN);
		temp.add("%NAME", "But... who are ---");
		temp.add("???", "I am known as Heliann, the blacksmith maiden. I inhabit the villa of Castlevania, I am here to help the Belmonts to find their path.", PRT_MAIDEN); 
		temp.add("Heliann", "We are running out of time though... the count is using the souls from the grand Belmonts to perform a ritual that will be catastrophic for the world, to open the portal to hell!", PRT_MAIDEN);
		temp.add("%NAME", "If they couldn't stop him, I doubt I will be able to!");
		temp.add("Heliann", "Only way to know if you are ready is to confront your fate; death will be the price to pay if you are not the chosen one to carry on with the Belmont legacy!", PRT_MAIDEN);
		temp.add("Heliann", "Take this key, it opens the castle dungeon, from there you can reach the clock tower, and finally, the castle keep. That is the only way to go. Be careful %NAME Belmont.", PRT_MAIDEN);
		hashCuts.put("MAIDEN1", temp);
		temp = new GFXChat();
		temp.add("%NAME", "Your reign of blood ends here, Count Dracula!");
		temp.add("Count Dracula", "The color of your soul... Amusing... A Belmont!", PRT_DRACULA);
		temp.add("%NAME", "And I am here to vanquish you for good. Prepare to fight!");
		temp.add("Count Dracula", "HAHAHA! Humans! Mankind! Carrying hope as their standard, is it true that you cannot see everything is doomed?", PRT_DRACULA);
		temp.add("Count Dracula", "Can you not see that you are not the heir of the night hunters? They are mine, already!", PRT_DRACULA);
		temp.add("%NAME", "No! our blood will never be yours, our fate will never dissapear, as long as you are a threat to men!");
		temp.add("Count Dracula", "You already rennounced everything when you ran away from your destiny!", PRT_DRACULA);
		temp.add("Count Dracula", "Yes, the son of the Belmont, the one true hunter, with his demise he condemned this world!", PRT_DRACULA);
		temp.add("%NAME", "Stop talking! It's time to fight!");
		temp.add("Count Dracula", "HAHAHA!", PRT_DRACULA);
		hashCuts.put("DRACULA1", temp);
		temp = new GFXChat();
		temp.add("Count Dracula", "Argh! You are strong Belmont! But not enough... HAHAHA!", PRT_DRACULA);
		temp.add("%NAME", "What?");
		temp.add("Count Dracula", "TASTE MY TRUE POWER!", PRT_DRACULA);
		hashCuts.put("DRACULA2", temp);
		temp = new GFXChat();
		temp.add("Count Dracula", "HAHAHA! It is worthless! As long as light exists on this world, I shall return as darkness made flesh!", PRT_DRACULA);
		temp.add("%NAME", "And my heir, the new Belmonts, we will be there to vanquish you again. DIE!");
		temp.add("Count Dracula", "This cannot be! NOOOOOOOOOOOOOOOOOOOO!!!!!!", PRT_DRACULA);
		hashCuts.put("DRACULA3", temp);

	}

	public GFXChat get(String ID){
		GFXChat ret = (GFXChat) hashCuts.get(ID);
		if (ret == null)
			Game.crash("Couldnt find GFXChat "+ID, new Exception());
		return ret;
	}
	

}
