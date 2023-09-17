package com.game.mancala.exceptions;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(GameInitializationException.class)
	public final ResponseEntity<Object> handleGameInitializationException(GameInitializationException ex,
			WebRequest request) {
		ErrorDetails err = new ErrorDetails(LocalDate.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(GameNotFoundException.class)
	public final ResponseEntity<Object> handleGameNotFoundException(GameNotFoundException ex, WebRequest request)
			throws GameNotFoundException {
		ErrorDetails error = new ErrorDetails(LocalDate.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
 

	@ExceptionHandler(IllegalMoveException.class)
	protected ResponseEntity<Object> handleIllegalMoveException(IllegalMoveException ex, WebRequest request) {
		ErrorDetails err = new ErrorDetails(LocalDate.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
	} 
}
