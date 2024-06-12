package com.mapfre.mifel.vida.exception;

public class NoContentException extends RuntimeException {
	
	public NoContentException(final String mensaje) {
		super(mensaje);
	}
	
	public NoContentException() {
		super();
	}

}
