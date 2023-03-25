package com.jeremias.dev.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jeremias.dev.utils.apiError.ApiError;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	ResponseEntity<?> resourceNotFoundHandler(Exception ex, WebRequest request) {
		super.logger.warn("Resource not found.", ex);
		return ResponseEntity.notFound().build();
	}
	/*
	@ExceptionHandler(ImportFailedException.class) 
	ResponseEntity<Boolean> importFailedHandler(Exception ex, WebRequest request) {
		super.logger.warn("Import failed.", ex);
		return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.NOT_ACCEPTABLE);
	}
	*/
	@ExceptionHandler(MailedException.class)
	ResponseEntity<Object> mailExceptionHandler(MailedException ex, WebRequest request) {
		super.logger.warn("Mail exception.", ex);
		   ApiError apiError = new ApiError(BAD_REQUEST);
	       apiError.setMessage("Error enviando el correo ");
	       apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}
	@ExceptionHandler(AccessForbiddenException.class)
	ResponseEntity<Object> accessForbiddenHandler(Exception ex, WebRequest request) {
		super.logger.warn("Access forbidden.", ex);
		   ApiError apiError = new ApiError(HttpStatus.FORBIDDEN);
	       apiError.setMessage("Error al ingresar no tiene acceso ");
	     //  apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler({AccessUnauthorizedException.class, JwtTokenValidationException.class}) 
	ResponseEntity<Boolean> accessUnauthorizedHandler(Exception ex, WebRequest request) {
		super.logger.warn("Access unauthorized.", ex);
		return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.UNAUTHORIZED);
	}	
	
	
	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
