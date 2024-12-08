package com.switchmaker.smpay.dto;

import com.switchmaker.smpay.entities.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Token {
	private String code;
	private String accessToken;
	private String tokenType;
	private int tokenExpired;
  private User user;
	private LocalDateTime generationDate;
}
