package com.mapfre.mifel.vida.model.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {
	int code;
	String message;
	LocalDateTime timestamp;
}
