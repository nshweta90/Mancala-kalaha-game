package com.game.mancala.model;

public class SmallPit extends Pit {  
	
	public int grabStones() {
        int stones = getStoneCount();
        setStoneCount(0);
		return stones;
	}
	
	
	public SmallPit(int id,int stoneCount) {
		super(id,stoneCount); 
	}
	
}
