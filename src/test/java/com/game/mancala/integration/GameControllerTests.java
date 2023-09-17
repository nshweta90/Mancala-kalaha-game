package com.game.mancala.integration;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.mancala.config.GameConfiguration;
import com.game.mancala.datamodel.GameDataEntity;
import com.game.mancala.exceptions.IllegalMoveException;
import com.game.mancala.model.Game;
import com.game.mancala.model.GameState;
import com.game.mancala.model.Player;
import com.game.mancala.repository.GameRepository;
import com.game.mancala.util.Constants;
import com.game.mancala.util.UtilTest;

 
@AutoConfigureDataJpa	
@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTests {
	
	private static final String PLAY_MOVE_REQUEST = """
			{"playerID":%s,"pitIndex":%s}			  
			 """;

	private static final String PLAY_MOVE_WITH_INCORRECT_PLAYER_REQUEST = """
			{"playerID":100,"pitIndex":5}			  
			 """;

	private static final String START_GAME_REQUEST = """
			{"playerID":%s,"pitIndex":5}			  
			 """;

	private static final String START_GAME_REQUEST_WITHOUTPLAYERNAME = """
			[{"playerName":""},{"playerName":""}]
			      """;

	 

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper; 
	
	@Autowired
	GameRepository gameRepo; 
	
	@Autowired
	GameConfiguration config;

	@Test
	void test_whenPlayerNamespassed_newGameInitiated_forthePlayers() throws Exception {
		Game game = createNewGame();
		Assertions.assertEquals(GameState.INIT, game.getGameState()); 
	}

	@Test
	void test_whenPlayerNameNotProvided_newGameInitiatedWithDefaultPlayerNames() throws Exception {		 

		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.post("/startGame").contentType(MediaType.APPLICATION_JSON).content(START_GAME_REQUEST_WITHOUTPLAYERNAME))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		Game game = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
		});
		Assertions.assertEquals(GameState.INIT, game.getGameState());
	 
		game.getPlayerList().stream().forEach(p-> {
			 Assertions.assertEquals("Player"+(game.getPlayerList().indexOf(p)+1), p.getPlayerName()); 
		});		  
	}
    
	@Test
	void test_WhenIncorrectGameIdprovidedToFetchGame_ErrorReturned() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/getGame/99").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
	} 
	
	
	 
	@Test
	void test_whenGameCreated_PlayerPitsInitializedcorrectly()
			throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException, Exception {
	    
		Game game = createNewGame();
		for (Player p : game.getPlayerList()) {
			p.getPitList().stream().forEach(pit -> {
				Assertions.assertEquals(config.getStonesPerPit(),pit.getStoneCount());
			});
		}
	} 
	
	
	@Test
	void test_Exception_when_playGameMove_with_incorrectMove() throws Exception {		 
		Game game = createNewGame(); 		
		String expectedMessage =String.format(Constants.MSG_INVALID_MOVE,game.getId(),100);
		
	     mockMvc.perform(
				MockMvcRequestBuilders.post("/playGameMove/"+game.getId()).contentType(MediaType.APPLICATION_JSON).content(PLAY_MOVE_WITH_INCORRECT_PLAYER_REQUEST))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof IllegalMoveException) )
				.andExpect(result ->Assertions.assertEquals(expectedMessage,result.getResolvedException().getMessage()));		  
	}
	@Test
	void test_Exception_when_playGameMove_with_incorrectStateGame() throws Exception {		
		
		Game game = createNewGame(); 
		 
		GameDataEntity data =  gameRepo.findById(game.getId()).get();
		data.setGameState(GameState.COMPLETE);
		gameRepo.save(data);
		
		String request =String.format(  START_GAME_REQUEST,game.getPlayerList().get(0).getId()); 
		
		
	     mockMvc.perform(
				MockMvcRequestBuilders.post("/playGameMove/"+game.getId()).contentType(MediaType.APPLICATION_JSON).content(request))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof IllegalMoveException) )
				.andExpect(result ->Assertions.assertEquals(Constants.MSG_NO_MOVES_POSSIBLE_ON_COMPLETED_GAME,result.getResolvedException().getMessage()));		  
	}
	
	
	@Test
	void test_whenPlayMove_stonecountUpdatedCorrectly_on_board() throws Exception {		

		Game game = createNewGame();
		String request = String.format(PLAY_MOVE_REQUEST, game.getPlayerList().get(0).getId(),5); 
		
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/playGameMove/" + game.getId())
						.contentType(MediaType.APPLICATION_JSON).content(request))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		Game gameResult = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

		String player1_actual_Pits = UtilTest.createPitListString(gameResult.getPlayerList().get(0).getPitList());
		String player2_actual_Pits = UtilTest.createPitListString(gameResult.getPlayerList().get(1).getPitList());

		Assertions.assertEquals("0-6;1-6;2-6;3-6;4-6;5-0", player1_actual_Pits);
		Assertions.assertEquals("0-7;1-7;2-7;3-7;4-7;5-6", player2_actual_Pits);
				 
	}
	
	@Test
	void test_whenNoGameStored_gameListIsEmpty() throws Exception { 
		gameRepo.deleteAll();
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/getAllGames").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		List<Game> list = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
		});
		Assertions.assertNotNull(list);
		Assertions.assertTrue(list.isEmpty());
	}

	private Game createNewGame()
			throws Exception, JsonProcessingException, JsonMappingException, UnsupportedEncodingException {
		List<Player> playerList = List.of(new Player("Jim"), new Player("Pam"));
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/startGame").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(playerList)))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		Game game = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
		});
		return game;
	}
	
	
	
	

}
