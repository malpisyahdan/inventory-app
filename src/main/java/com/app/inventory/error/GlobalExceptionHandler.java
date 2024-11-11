package com.app.inventory.error;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.app.inventory.model.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse<String>> handleException(Exception ex, HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ErrorResponse<String>> handleException(ResponseStatusException ex,
			HttpServletRequest request) {
		String errorMessage = ex.getMessage();
		if (errorMessage.contains("\"")) {
			errorMessage = errorMessage.split("\"")[1];
		}
		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, request.getRequestURI());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse<String>> handleValidationException(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		return buildErrorResponse(HttpStatus.BAD_REQUEST, errorMessage, request.getRequestURI());
	}

	private ResponseEntity<ErrorResponse<String>> buildErrorResponse(HttpStatus status, String message, String path) {
		ErrorResponse<String> errorResponse = ErrorResponse.<String>builder().timestamp(LocalDateTime.now())
				.code(status.value()).error(message).path(path).build();
		return new ResponseEntity<>(errorResponse, status);
	}

}
