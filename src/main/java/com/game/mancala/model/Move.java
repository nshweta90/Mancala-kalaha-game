package com.game.mancala.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Move {

	public Move(int playerID, int pitIndex) {
		super();
		this.playerID = playerID;
		this.pitIndex = pitIndex;
	}
	
	private int playerID;
	private int pitIndex; 
}
