package com.game.mancala.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game.mancala.config.GameConfiguration;
import com.game.mancala.datamapper.GameDataMapper;
import com.game.mancala.datamapper.PlayerDataMapper;
import com.game.mancala.datamodel.GameDataEntity;
import com.game.mancala.datamodel.PlayerDataEntity;
import com.game.mancala.exceptions.GameInitializationException;
import com.game.mancala.exceptions.GameNotFoundException;
import com.game.mancala.exceptions.IllegalMoveException;
import com.game.mancala.model.Game;
import com.game.mancala.model.GameState;
import com.game.mancala.model.Move;
import com.game.mancala.model.Pit;
import com.game.mancala.model.Player;
import com.game.mancala.model.SmallPit;
import com.game.mancala.repository.GameRepository;
import com.game.mancala.repository.PlayerRepository;
import com.game.mancala.util.Constants;
import com.game.mancala.util.MancalaUtil;

import lombok.extern.slf4j.Slf4j;

 
 
@Slf4j
@Service

public class GamePlayService implements GameInterface {  
	 

	@Autowired 
	private GameDataMapper gameMapper ;  
	
	@Autowired 
	private PlayerDataMapper playerMapper ;
	
	@Autowired
	private GameRepository gameRepo ;
	
	@Autowired
	private PlayerRepository playerRepo ;  
	

	@Autowired
	GameConfiguration config; 
	
	
	public static final String GAME_NOT_CONFIGURED_TO_BE_PLAYED_WITH_S_PLAYERS = "Game not configured to be played with %s players";

	public static final String GAME_WITH_ID_S_COULD_NOT_BE_FOUND = "Game With Id %s could not be found!";

	public static final String INVALID_MOVE_MESSAGE = "This move is invalid for game %s and player %s";

	public static final String NO_MOVES_POSSIBLE_ON_COMPLETED_GAME = "No moves possible on completed game!";
	
 
	 
	/**
	 * Validate and Start New Game for given players
	 * @param playerList
	 * @param gameEntity
	 */
	public Game startGame(List<Player> playerDetails) throws GameInitializationException {		
		if( config.getNumberOfPlayers().intValue() != playerDetails.size())
			throw new GameInitializationException(String.format(GAME_NOT_CONFIGURED_TO_BE_PLAYED_WITH_S_PLAYERS, playerDetails.size()));
			
		List<PlayerDataEntity> playerList = initGamePlayers(playerDetails);
		GameDataEntity gameEntity = gameRepo.save(initNewGame()); 			
		updateAndSavePlayers(playerList, gameEntity);	 
	    updateAndSaveGame(playerList, gameEntity);	
	    
	    log.debug(" GamePlayService.startGame() :: Game Initialized . Game ID -"+gameEntity.getId() );
	    return gameMapper.mapEntityToBO(gameEntity);
	}

	
	private void updateAndSaveGame(List<PlayerDataEntity> playerList, GameDataEntity gameEntity) {
		gameEntity.getPlayerList().addAll(playerList);	 
		gameEntity.setActivePlayerID(gameEntity.getPlayerList().get(Constants.ZERO).getId());
		gameRepo.save(gameEntity);
	}

	/**
	 * Method Updates Player entities with the Next Player Id's for given game
	 * @param playerList
	 * @param gameEntity
	 */
	private void updateAndSavePlayers(List<PlayerDataEntity> playerList, GameDataEntity gameEntity) {
		playerList.stream().forEach(p -> p.setGame(gameEntity));	
		playerRepo.saveAll(playerList);	 
		playerRepo.saveAll(updatePlayersWithNextPlayerID(playerRepo.findByGameId(gameEntity.getId())));
	}

	/**
	 * Method creates the Default  names for game players in case no name provided for player
	 * @param playerDetails
	 * @return
	 */
	private  List<PlayerDataEntity> initGamePlayers(List<Player> playerDetails) {	 
		
		playerDetails.stream().forEach( p->{
			initAllPits(p);
			 if(StringUtils.isEmpty(p.getPlayerName()))
				 p.setPlayerName("Player"+ (playerDetails.indexOf(p)+1));			  
		} );
		return  playerDetails.stream().map(p -> playerMapper.mapBoToEntity(p)).toList() ;
		
	}
	
	/**
	 * Initialize the all pits in the game with default values
	 * @param player
	 */
	
	private void initAllPits(Player player) { 
		player.setPitList(IntStream.range(0,Constants.BIG_PIT_IDX).mapToObj(i -> new SmallPit(i,config.getStonesPerPit())).toList()); 
		player.setBigPit( new Pit(Constants.BIG_PIT_IDX,0));
	}
	
	/**
	 * Initiate New game with status and startTime
	 * @return GameDataEntity
	 */

	private GameDataEntity initNewGame() {		
		GameDataEntity gameInst = new GameDataEntity(); 
		gameInst.setStartTime(LocalDateTime.now());	
		gameInst.setGameState(GameState.INIT);
		return gameInst;
	} 

	
	/**Validates the game rules after each Move 
	 * @param game
	 * @return The Id of next player who will play the game
	 */
	public int applyRules(GameDataEntity game) {		
	  RuleValidator rules = new RuleValidator(game); 
	  return  rules.applyRulesAndGetNextPlayer();
	}
	 
	/**Play the game with move details containing Player id and the pit from which he wants to play
	 * @param game
	 * @param move
	 */
	public Game playTurn(GameDataEntity game ,Move move) {	  		
		game.setLastMove(new Move(-1,-1));
		game.setGameState(GameState.INPROGRESS); 
		handleStoneDistribution(game, move); 
		game.setActivePlayerID(applyRules(game)); 		
		saveProgress(game); 
		
		log.debug(" GamePlayService.playTurn() :: Move Completed for game "+ game.getId()+ " Player id " + move.getPlayerID());
		return gameMapper.mapEntityToBO(game);
		
	}

	
	private void saveProgress(GameDataEntity game) {
		playerRepo.saveAll(game.getPlayerList());		 
		gameRepo.save(game);
	}

	/**
	 * 
	 * @param game
	 * @param move
	 */
	private void handleStoneDistribution(GameDataEntity game, Move move) {
		
		PlayerDataEntity activePlayer = MancalaUtil.getPlayerByID(game,move.getPlayerID());
		int stonesToDistribute = activePlayer.getPitList().get(move.getPitIndex()).grabStones();
		 
		if (stonesToDistribute > 0) {
			stonesToDistribute = distributeStones(activePlayer, move.getPitIndex()+1, stonesToDistribute, game);
			int nextPlayerId = activePlayer.getNextplayerID();
			while (stonesToDistribute != 0) {
				PlayerDataEntity player = MancalaUtil.getPlayerByID(game,nextPlayerId);
				stonesToDistribute = distributeStones(player, 0, stonesToDistribute, game);
				nextPlayerId = player.getNextplayerID();
			}
		}
	} 
	/**
	 * Method distributes the stones on the players pits and saves last pit location where stone was distributed
	 * @param player
	 * @param startFromThisPitId
	 * @param stonesToDistribute
	 * @param game
	 * @return  remaining stones to distribute
	 */
	public int distributeStones(PlayerDataEntity  player, int startFromThisPitId, int stonesToDistribute,GameDataEntity game) {
		 
		for(int i= startFromThisPitId;i<player.getPitList().size() && stonesToDistribute >0 ;i++,stonesToDistribute--){
			 player.getPitList().get(i).addStone(1);	 
			 game.getLastMove().setPitIndex(i);	
			 game.getLastMove().setPlayerID(player.getId());			 
		} 		 
		if (stonesToDistribute > 0 && game.getActivePlayerID() == player.getId() ) {
			player.getBigPit().addStone(1);
			stonesToDistribute--; 
			game.getLastMove().setPitIndex(player.getBigPit().getId());	
			game.getLastMove().setPlayerID(player.getId());			 			 
		}
		return stonesToDistribute;
	}   
	
	public List<Game> getAllGames() { 
		return gameRepo.findAll().stream().map(gameMapper :: mapEntityToBO).toList();		
	} 
 
	 @Override
	 public GameDataEntity loadGameData(Integer gameId)
	 {
			return gameRepo.findById(gameId)
					.orElseThrow(() -> new GameNotFoundException(String.format(GAME_WITH_ID_S_COULD_NOT_BE_FOUND, gameId)));
     }

	public void validateMove(Move move, GameDataEntity loadedGame) throws IllegalMoveException {
		if (move.getPlayerID() != loadedGame.getActivePlayerID())
			throw new IllegalMoveException(String.format(INVALID_MOVE_MESSAGE , loadedGame.getId(),move.getPlayerID())); 
	}

	@Override
	public void validateGameState(GameDataEntity loadedGame) throws IllegalMoveException {
		if(loadedGame.getGameState().equals(GameState.COMPLETE))
			throw new IllegalMoveException(NO_MOVES_POSSIBLE_ON_COMPLETED_GAME);
	}
    
	@Override
	public Game getGame(Integer gameId) throws GameNotFoundException{   
		return gameMapper.mapEntityToBO(loadGameData(gameId));
	}
 
	/**
	 * Method calculates and populates the nextPlayer id to identify who is next player
	 * @param list
	 * @return
	 */
	public List<PlayerDataEntity> updatePlayersWithNextPlayerID(List<PlayerDataEntity> list) {		
		 for(int i =0; i<list.size()-1;i++){
			 list.get(i).setNextplayerID(list.get(i+1).getId());
		 }
		  list.get(list.size()-1).setNextplayerID(list.get(0).getId());	
		  return list;
	}

 

	 
	
}
