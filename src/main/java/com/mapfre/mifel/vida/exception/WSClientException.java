package com.mapfre.mifel.vida.exception;

public class WSClientException extends RuntimeException {

	public WSClientException(final String mensaje, final Throwable cause) {
		super(mensaje, cause);
	}
	
	public WSClientException(final String mensaje) {
		super(mensaje);
	}
	
	public WSClientException() {
		super();
	}

}
