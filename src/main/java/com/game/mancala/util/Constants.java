package com.game.mancala.util;

public class Constants {	 

	private Constants() {}

	/**
	 * Indicates last element from the list of Pits as the Big Pit on Mancala/kalaha game board.
	 */
	public static final int  BIG_PIT_IDX = 6;	
	
	/**
	 * Indicates the index of First element in list
	 */
	public static final int ZERO = 0;		
	
     
	/**
	 * Error message
	 */
	public static final String MSG_INCORRECT_GAME_PLAYER_CONFIGURATION = "Game not configured to be played with %s players";

	/**
	 * Error message
	 */
	public static final String MSG_GAME_COULD_NOT_BE_FOUND = "Game With Id %s could not be found!";

	/**
	 * Error message
	 */
	public static final String MSG_INVALID_MOVE = "This move is invalid for game %s and player %s";

	/**
	 * Error message
	 */
	public static final String MSG_NO_MOVES_POSSIBLE_ON_COMPLETED_GAME = "No moves possible on completed game!";

}
