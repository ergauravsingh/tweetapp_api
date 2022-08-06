package com.tweetapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.kafka.Producer;

import io.swagger.annotations.ApiOperation;

@RequestMapping("/kafka")
@RestController
public class KafkaController {
	@Autowired
	Producer producer;
	
	@ApiOperation(value = "sending message through Kafka", response = ResponseEntity.class)
	@GetMapping(value="/sendMessage")
	public String sendMessage(@RequestParam(name="message") String message) {
		producer.sendMessage(message);
		return "message sent";
		
	}
	

}
