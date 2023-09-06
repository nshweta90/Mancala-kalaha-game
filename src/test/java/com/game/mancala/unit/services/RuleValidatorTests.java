package com.game.mancala.unit.services;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.game.mancala.datamodel.GameDataEntity;
import com.game.mancala.datamodel.PlayerDataEntity;
import com.game.mancala.model.GameState;
import com.game.mancala.model.Move;
import com.game.mancala.model.Pit;
import com.game.mancala.services.RuleValidator;
import com.game.mancala.util.Constants;
import com.game.mancala.util.UtilTest;

class RuleValidatorTests { 
	
	private static final int PLAYER1_ID = 2;
	private static final int PLAYER2_ID = 3;
	 
	
	@Test
	void test_whenStoneLandsInCurrentPlayerBigPit_CurrentPlayergetOneMorechance() {
		
		GameDataEntity gameData = create2PlayerGameEntity("0-0;1-6;2-6;3-6;4-6;5-6","0-6;1-6;2-6;3-6;4-6;5-6"); 
		RuleValidator validator = new RuleValidator(gameData);
		int nextPlayer = validator.applyRulesAndGetNextPlayer();		
		Assertions.assertEquals(2, nextPlayer); 
	}
 
	@Test
	void test_whenAllStonesInSmallPitListBecomeZero_GameCompleted() {
		
		GameDataEntity gameData = create2PlayerGameEntity("0-0;1-0;2-0;3-0;4-0;5-0","0-6;1-6;2-6;3-6;4-6;5-6"); 
		RuleValidator validator = new RuleValidator(gameData); 
		gameData.setLastMove(new Move(PLAYER2_ID,2));
		validator.applyRulesAndGetNextPlayer();		
		Assertions.assertEquals(GameState.COMPLETE, gameData.getGameState()); 
		Assertions.assertEquals(2, gameData.getWinner()); 
	} 	
	@Test
	void test_whenLastStoneGoesInEmptyPit_OponentsStonesAreCapturedaswellInBigPit() {
		
		GameDataEntity gameData = create2PlayerGameEntity("0-5;1-7;2-1;3-1;4-3;5-8","0-6;1-6;2-3;3-6;4-1;5-6"); 
		gameData.setLastMove(new Move(PLAYER1_ID,2));
		RuleValidator validator = new RuleValidator(gameData);
		validator.applyRulesAndGetNextPlayer();		
		Assertions.assertEquals(7, gameData.getPlayerList().get(0).getBigPit().getStoneCount()); 
	} 
	
	
	private GameDataEntity create2PlayerGameEntity(String player1pits,String player2pits) {
		GameDataEntity gameData = new GameDataEntity(); 
		
		gameData.setId(1);		 
		gameData.setLastMove(new Move(PLAYER1_ID, Constants.BIG_PIT_IDX)); 
		gameData.setActivePlayerID(PLAYER1_ID);
		
		PlayerDataEntity player1 = createPlayerEntity(player1pits,PLAYER1_ID,PLAYER2_ID);		 
		player1.setGame(gameData); 
		
		PlayerDataEntity player2 = createPlayerEntity(player2pits,PLAYER2_ID,PLAYER1_ID);
		player2.setGame(gameData);
		
		gameData.setPlayerList(List.of(player1,player2));
		return gameData;
	}

	private PlayerDataEntity createPlayerEntity(String pits,int playerid,int nextplayerid) {
		PlayerDataEntity player = new PlayerDataEntity();
		player.setId(playerid);		
		player.setNextplayerID(nextplayerid);
		player.setPitList(UtilTest.createPitList(pits));
		player.setBigPit(new Pit(Constants.BIG_PIT_IDX,Constants.ZERO));
		return player;
	}
 
	
}
