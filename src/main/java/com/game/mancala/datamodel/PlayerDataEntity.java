package com.game.mancala.datamodel;

import java.util.List;

import com.game.mancala.datamapper.BigPitDetailsConverter;
import com.game.mancala.datamapper.SmallPitDetalsConverter;
import com.game.mancala.model.Pit;
import com.game.mancala.model.SmallPit;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="player_details")
public class PlayerDataEntity {
        
	@Id
	@GeneratedValue
	private int id; 
	
	@Size(min=2, message ="Name should have atleast two characters")
	private String playerName;	 
	
	@ManyToOne
	@JoinColumn(name = "game_id")
	private GameDataEntity game;
	
	@Column(name ="small_pit_values")
	@Convert(converter = SmallPitDetalsConverter.class)
	private List<SmallPit> pitList;
	 
	private int nextplayerID; 
	 
	@Column(name ="score")
	@Convert(converter = BigPitDetailsConverter.class)
	private Pit bigPit;  
}
