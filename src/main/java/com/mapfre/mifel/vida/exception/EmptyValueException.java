package com.mapfre.mifel.vida.exception;

import org.springframework.stereotype.Component;

@Component
public class EmptyValueException extends RuntimeException{
    private static final long serialVersionUID=1L;
    private int errorCode;
    private String errorMessage;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static long getSerialVersionUID(){
        return serialVersionUID;
    }
    public EmptyValueException(int errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    public EmptyValueException(){

    }
}
