package com.game.mancala.services;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.NoSuchElementException;

import com.game.mancala.datamodel.GameDataEntity;
import com.game.mancala.datamodel.PlayerDataEntity;
import com.game.mancala.model.GameState;
import com.game.mancala.model.Pit;
import com.game.mancala.util.Constants;
import com.game.mancala.util.MancalaUtil;

public class RuleValidator {

	private GameDataEntity  gameInstance;
	
	

	public RuleValidator(GameDataEntity  game) {
		this.gameInstance = game;
	}

	/**
	 * Apply and check the game rules.The method should be called after each move to
	 * validate and check game state.	 
	 * Rule 1: If last stone of move landed in  players big pit - player gets one more chance
	 * 
	 * Rule 2: If last stone of a move landed on an empty pit on current player side
	 * and there are some gems in the opposite player pit then gems in the 2 pits
	 * will be captured in the current players big pit.
	 *  
	 * Rule 3: If all the small	 
	 * pits of any player become empty - other players combine their stones in their
	 * big pit player with maximum stones in pit wins the game.
	 * 
	 * @return nextPlayerID
	 */
	public int applyRulesAndGetNextPlayer() { 
		 
		int nextPlayerID = MancalaUtil.getCurrentPlayer(gameInstance).getNextplayerID();
		int currentPlayerID = MancalaUtil.getCurrentPlayer(gameInstance).getId();

		if (gameInstance.getLastMove() != null) {
			int pitIndex = gameInstance.getLastMove().getPitIndex();

			if (Constants.BIG_PIT_IDX == gameInstance.getLastMove().getPitIndex()) {
				nextPlayerID = currentPlayerID;
			} else

			if (MancalaUtil.getCurrentPlayer(gameInstance).getPitList().get(pitIndex).getStoneCount() == 1) {
				PlayerDataEntity oppositePlayer = MancalaUtil.getPlayerByID(gameInstance, nextPlayerID);
				MancalaUtil.getCurrentPlayer(gameInstance).getPitList().get(pitIndex).grabStones();
				int opositPitStoneCount = oppositePlayer.getPitList().get(5 - pitIndex).grabStones();
				MancalaUtil.getCurrentPlayer(gameInstance).getBigPit().addStone(opositPitStoneCount + 1);
			}
			checkForGameWinner();
		}
		return nextPlayerID;

	}

	private void checkForGameWinner() { 
		gameInstance.getPlayerList().stream().filter(this::allSmallPitsEmpty).findFirst()
				.ifPresent(player -> completeGame());
		
	}
	
	/**
	 * Method calculates and set winner of the game
	 * It marks the game as complete and saves completion time
	 **/

	private void completeGame() {
	 
		PlayerDataEntity winner = gameInstance.getPlayerList().stream()
				.max(Comparator.comparing(p -> p.getBigPit().getStoneCount())).orElseThrow(NoSuchElementException::new);
		
		gameInstance.setGameState(GameState.COMPLETE);
		gameInstance.setEndtartTime(LocalDateTime.now());
		gameInstance.setWinner(winner.getId()); 
	}

	
	private boolean allSmallPitsEmpty(PlayerDataEntity player) {  
		return player.getPitList().stream().allMatch(Pit::isPitEmpty);
	}

}
