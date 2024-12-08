package com.switchmaker.smpay.dto;

import lombok.Data;

@Data
public class ResponseMessage {
	private String code;
	private String message;
	private Object data;
}
