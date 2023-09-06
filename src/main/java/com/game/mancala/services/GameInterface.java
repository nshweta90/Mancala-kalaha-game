package com.game.mancala.services;

import java.util.List;

import com.game.mancala.datamodel.GameDataEntity;
import com.game.mancala.exceptions.GameInitializationException;
import com.game.mancala.exceptions.IllegalMoveException;
import com.game.mancala.model.Game;
import com.game.mancala.model.Move;
import com.game.mancala.model.Player;

public interface GameInterface {
 
	public Game startGame(List<Player> playerDetails) throws GameInitializationException;
	public Game playTurn(GameDataEntity game ,Move move);
	public List<Game> getAllGames();
	public Game getGame(Integer gameId);
	public GameDataEntity loadGameData(Integer gameID); 
	public void validateMove(Move move, GameDataEntity loadedGame) throws IllegalMoveException;
	public void validateGameState(GameDataEntity loadedGame) throws IllegalMoveException;
	 
}
