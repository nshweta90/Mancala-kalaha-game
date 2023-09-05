package com.game.mancala.util;

import com.game.mancala.datamodel.GameDataEntity;
import com.game.mancala.datamodel.PlayerDataEntity;

public final class MancalaUtil { 
	
	private MancalaUtil () {}
	public static PlayerDataEntity getPlayerByID(GameDataEntity gameDataEntity, int playerID){
		 return gameDataEntity.getPlayerList().stream().filter(p->p.getId() == playerID).findFirst().orElseThrow();    	 
	}
	
	public static PlayerDataEntity getCurrentPlayer(GameDataEntity game) {
   	 return  getPlayerByID(game, game.getActivePlayerID());
   }
}
