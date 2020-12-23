package com.example.library.advice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.example.library.exception.CustomizeException;

@ControllerAdvice
public class CustomizeExceptionHandler {

	@ExceptionHandler(CustomizeException.class)
	public ResponseEntity<Object> handleCityNotFoundException(CustomizeException ex, WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("code", ex.getCode());

		return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
	}
	
	//待修正
//	@ExceptionHandler(RuntimeException.class)
//	public ResponseEntity<Object> handleRuntimeException(CustomizeException ex, WebRequest request) {
//
//		Map<String, Object> body = new LinkedHashMap<>();
//		body.put("timestamp", LocalDateTime.now());
//		body.put("message", ex.getMessage());
//		body.put("code", ex.getCode());
//
//		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//	}

//	 @ExceptionHandler(Exception.class)
//	    public ResponseEntity<Object> handleCityNotFoundException(
//	    		Exception ex, WebRequest request) {
//
//	        Map<String, Object> body = new LinkedHashMap<>();
//	        body.put("timestamp", LocalDateTime.now());
//	        body.put("message", CustomizeErrorCode.SYS_ERROR);
//	        body.put("code", CustomizeErrorCode.SYS_ERROR.getCode());
//
//	        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//	    }

//	@ExceptionHandler(Exception.class)
//	ModelAndView handleControllerException(HttpServletRequest request, Throwable ex) {
//		HttpStatus status = getStatus(request);
//		return new ModelAndView("error");
//	}
//
//	private HttpStatus getStatus(HttpServletRequest request) {
//		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
//		if (statusCode == null) {
//			return HttpStatus.INTERNAL_SERVER_ERROR;
//		}
//		return HttpStatus.valueOf(statusCode);
//	}
}
