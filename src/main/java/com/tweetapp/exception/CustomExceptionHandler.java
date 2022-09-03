package com.tweetapp.exception;

import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class CustomExceptionHandler {
	Log logger = LogFactory.getLog(CustomExceptionHandler.class.getName());

	private ErrorModel logError(Exception ex, WebRequest request, String errorMsg, HttpStatus httpStatus) {
		// Creating UUID which will be sent back to the UI so that we can use it to
		// search the exact issue in the logs
		UUID exceptionUUID = UUID.randomUUID();
		ErrorModel error = new ErrorModel(exceptionUUID, errorMsg, httpStatus.value(), httpStatus.toString());

		logger.error("[" + exceptionUUID + "] Request failed for : "
				+ ((ServletWebRequest) request).getRequest().getRequestURI() + " | Http Method : "
				+ ((ServletWebRequest) request).getHttpMethod() + " | " + error.getError());
		logger.error("[" + exceptionUUID + "] Exception Stacktrace : [" + ex.getClass().getName() + "] | "
				+ Arrays.toString(ex.getStackTrace()));

		String userID = request.getHeader("email");
		logger.error("[" + exceptionUUID + "] User:" + userID + " | Exception Details: " + error.getError() + " | ["
				+ ex.getClass().getName() + "]");
		return error;
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ErrorModel> handleAllExceptions(Exception ex, WebRequest request) {

		ErrorModel error = logError(ex, request, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(error, HttpStatus.OK);

	}

}
