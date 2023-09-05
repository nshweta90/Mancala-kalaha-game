package com.game.mancala.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class GameNotFoundException extends  RuntimeException {
	
	public GameNotFoundException (String messsage) {
		super(messsage);
	}
	

}
