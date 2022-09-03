package com.tweetapp.exception;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ErrorModel {

	private UUID uuid;
	private String error;
	private int errorCode;
	private String errorDetails;

    public ErrorModel(String s, int i, String error, String code, String message) {
    }
}
