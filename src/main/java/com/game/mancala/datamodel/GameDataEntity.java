package com.game.mancala.datamodel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.game.mancala.model.GameState;
import com.game.mancala.model.Move;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="game_details")
public class GameDataEntity {
	
	@Id
	@GeneratedValue
	private int id; 	
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "game_id", referencedColumnName = "id") 
	private List<PlayerDataEntity> playerList = new ArrayList<>();
	
	
	private LocalDateTime startTime;
	
	private LocalDateTime endtartTime;
	
	private int activePlayerID; 	
	
	@Transient
	private int winner;
	 
	@Transient
	private Move lastMove ;	
	 
	private GameState gameState; 
	

	
 
	

}
