package com.game.mancala.exceptions;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorDetails { 

	private LocalDate timeStamp;
	private String message;
	private String details;
	

	public ErrorDetails(LocalDate timeStamp, String message, String details) {
		super();
		this.timeStamp = timeStamp;
		this.message = message;
		this.details = details;
	}
	
 
}
