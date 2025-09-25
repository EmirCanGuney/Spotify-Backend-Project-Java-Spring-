
package com.internship.spotify.handler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.internship.spotify.exception.BaseException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = { BaseException.class })
	public ResponseEntity<ApiError> handleBaseException(BaseException exception, WebRequest request) {

		return ResponseEntity.badRequest().body(creatApiError(exception.getMessage(), request));

		// return ResponseEntity.badRequest().body(exception.getMessage());
	}

	private String getHostName() {
		try {
			InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			System.out.println("Hata oluştu getHostName methodunda." + e.getMessage());
		}
		return null;
	}

	public <E> ApiError<E> creatApiError(E message, WebRequest request) {

		ApiError<E> apiError = new ApiError<>();
		apiError.setStatus(HttpStatus.BAD_REQUEST.value());

		Exception<E> exception = new Exception<>();
		exception.setCreateTime(new Date(1));
		exception.setHostName(getHostName());
		exception.setPath(request.getDescription(false));
		exception.setMessage(message);

		apiError.setException(exception);

		return apiError;
	}

}
