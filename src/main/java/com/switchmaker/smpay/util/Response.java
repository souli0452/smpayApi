package com.switchmaker.smpay.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response {
  private String statusCode;
  private String message;
  private Object data; // Peut être une List, un objet unique ou null

  // Constructeur, getters et setters
}
