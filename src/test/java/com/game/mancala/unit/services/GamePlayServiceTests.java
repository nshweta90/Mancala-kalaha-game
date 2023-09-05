package com.game.mancala.unit.services;
 

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.game.mancala.config.GameConfiguration;
import com.game.mancala.datamodel.PlayerDataEntity;
import com.game.mancala.exceptions.GameInitializationException;
import com.game.mancala.model.Player;
import com.game.mancala.services.GamePlayService;

@ExtendWith(MockitoExtension.class)
class GamePlayServiceTests {
 
	@InjectMocks
	GamePlayService service;

	@Mock
	GameConfiguration config;

 	
	@Test
	 void testStartGame_for_correctPlayerCount() {
		 
		List<Player> list =List.of(new Player("Player-One"),new Player("Player-Two"),new Player("Player-Three"));
		 
		Mockito.when(config.getNumberOfPlayers()).thenReturn(Integer.valueOf(2));
		Assertions.assertThrows(GameInitializationException.class,()-> service.startGame(list));
	}
	
	@Test
	 void testStartGame_with_correctPlayerdetails() {
		PlayerDataEntity player1 =new PlayerDataEntity();
		player1.setId(2);		
		PlayerDataEntity player2 =new PlayerDataEntity();
		player2.setId(3);
				
		List<PlayerDataEntity> list =List.of(player1,player2);
		List<PlayerDataEntity> PlayersWithNextPlayerID = service.updatePlayersWithNextPlayerID(list);
		
		assertEquals( player2.getId(),PlayersWithNextPlayerID.get(0).getNextplayerID());
		assertEquals( player1.getId(),PlayersWithNextPlayerID.get(1).getNextplayerID());
	}
	
	
	 

}
