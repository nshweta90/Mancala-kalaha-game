package com.game.mancala.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {	 
	private int id;
	private String playerName;  
	private int nextplayerID;	
	private List<SmallPit> pitList;	
	private Pit bigPit;
	public Player(String playerName) {
		super();
		this.playerName = playerName;
	}
	public Player() {
		super(); 
	}  

}
