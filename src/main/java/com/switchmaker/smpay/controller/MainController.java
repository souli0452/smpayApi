package com.switchmaker.smpay.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class MainController {
	@RequestMapping(path = "/")
	private ResponseEntity<?> mainUrl()
	{
		return new ResponseEntity<String>("error (type=Not Found, status=404)",HttpStatus.OK);
	}
	
	
	@RequestMapping(path = "/smpay/api/v1/test")
	private ResponseEntity<?> testUrl()
	{
		return new ResponseEntity<String>("<h1>bonjour</h1>",HttpStatus.OK);
	}
}
