package com.game.mancala.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.game.mancala.datamodel.GameDataEntity;
import com.game.mancala.exceptions.GameInitializationException;
import com.game.mancala.exceptions.GameNotFoundException;
import com.game.mancala.exceptions.IllegalMoveException;
import com.game.mancala.model.Game;
import com.game.mancala.model.Move;
import com.game.mancala.model.Player;
import com.game.mancala.services.GameInterface;

@CrossOrigin
@RestController
public class GameController {

	@Autowired
	private GameInterface gameService;
	 
	@GetMapping("/getAllGames")
	public List<Game> getAllGames() {
		return gameService.getAllGames();
	}
	
	@GetMapping("/getGame/{id}")
	public  Game getGame(@PathVariable(name ="id") Integer gameId) throws GameNotFoundException{ 
		return gameService.getGame(gameId);
	}

	@PostMapping("/startGame")
	public Game startGame(@RequestBody List<Player> playerList) throws GameInitializationException {
		return gameService.startGame(playerList);
	}

	@PostMapping("/playGameMove/{gameID}")
	public Game playMove(@PathVariable(name = "gameID") Integer gameID, @RequestBody Move move)
			throws GameNotFoundException, IllegalMoveException{ 
		GameDataEntity loadedGame = gameService.loadGameData(gameID);
		gameService.validateMove(move, loadedGame);
		gameService.validateGameState(loadedGame);  
		return gameService.playTurn(loadedGame,move);
	}

}
