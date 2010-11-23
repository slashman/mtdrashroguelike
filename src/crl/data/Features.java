package crl.data;

import crl.feature.Feature;
import crl.player.Player;
import crl.ui.AppearanceFactory;

public class Features {
	public static Feature[] getFeatureDefinitions(AppearanceFactory apf){
        Feature [] ret = new Feature [10];
		ret[0] = new Feature("MAGIC_BARRIER", apf.getAppearance("MAGIC_BARRIER"), 15, "Magic Barrier");
		ret[1] = new Feature("ROCK", apf.getAppearance("ROCK"), 20, "Rock");
		ret[2] = new Feature("COIN", apf.getAppearance("COIN"), 9999, "Coin");
		ret[3] = new Feature("BIGCOIN", apf.getAppearance("BIGCOIN"), 9999, "Big Coin");
		
		ret[4] = new Feature("RED_POOL", apf.getAppearance("RFOUNTAIN_F"), 9999, "Mana water");
		ret[5] = new Feature("YELLOW_POOL", apf.getAppearance("YFOUNTAIN_F"), 9999, "Health water");
		ret[6] = new Feature("RED_WATER", apf.getAppearance("RSFOUNTAIN_F"), 9999, "Mana water");
		ret[7] = new Feature("YELLOW_WATER", apf.getAppearance("YSFOUNTAIN_F"), 9999, "Health water");
		ret[8] = new Feature("FORGE", apf.getAppearance("FORGE"), 9999, "Magic Anvil");
		ret[9] = new Feature("ANKH", apf.getAppearance("ANKH"), 9999, "Ankh");
		
		ret[0].setSolid(true);
		ret[0].setDestroyable(true);
		ret[0].setRelevant(false);
		ret[1].setSolid(true);
		ret[1].setDestroyable(true);
		ret[1].setRelevant(false);
		ret[2].setGoldPrize(10);
		ret[3].setGoldPrize(50);
		ret[4].setRelevant(false);
		ret[5].setRelevant(false);
		ret[6].setRelevant(false);
		ret[7].setRelevant(false);
		ret[4].setManaPrize(200);
		ret[5].setHealPrize(200);
		ret[6].setManaPrize(20);
		ret[7].setHealPrize(20);
		ret[8].setEffect("REPAIR");
		ret[9].setEffect("ANKH");
		return ret;
	}
}
