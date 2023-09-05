package com.game.mancala.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pit {
	
	private int id;
	private int stoneCount;	  	 
	
	@JsonIgnore
	public boolean isPitEmpty() {
		return this.stoneCount == 0;
	}
     
	public void addStone(int count) {
		this.stoneCount+=count;
	}
 
	public Pit(int id ,int stoneCount) {
		super();
		this.id = id;
		this.stoneCount = stoneCount; 
	} 
	
}
