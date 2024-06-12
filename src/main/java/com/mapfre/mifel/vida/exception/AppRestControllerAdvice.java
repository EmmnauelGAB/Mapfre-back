package com.mapfre.mifel.vida.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.mapfre.mifel.vida.model.response.ErrorResponse;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;;

@RestControllerAdvice
public class AppRestControllerAdvice {
	
	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleBadRequestException(Exception ex) {
		return new ResponseEntity<>(new ErrorResponse(BAD_REQUEST.value(), "Argumento inválido en la petición", now()), HttpStatus.BAD_REQUEST);
	}
}
