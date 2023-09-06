package com.game.mancala.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Game { 
    
	
	private int id;
	private List<Player> playerList;	
	
	private LocalDateTime startTime;
	
	private LocalDateTime endtartTime;
	
	private int activePlayerID; 
	
	private int winner;
	
	@JsonIgnore
	private Move lastMove ;	 
	
	
	private GameState gameState; 
	 
    
	
}
