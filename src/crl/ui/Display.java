package crl.ui;

import java.io.File;
import java.util.Vector;

import crl.player.HiScore;
import crl.player.Player;
import crl.player.advancements.Advancement;

import crl.game.Game;


public abstract class Display {
	public static Display thus;
	public abstract int showTitleScreen();	
	public abstract void showIntro(Player player);
	public abstract boolean showResumeScreen(Player player);
	public abstract void showEndgame(Player player);	
	public abstract void showHiscores(HiScore[] scores);
	public abstract void showHelp();
	public abstract int showSavedGames(File[] saveFiles);
	//public abstract void showChat(String chatID, Game game);
	public abstract void showScreen(Object screenID);
	public abstract Advancement levelUp(Player p);
}
