package com.switchmaker.smpay.entities;

import com.switchmaker.smpay.abstractClass.Identifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class BalanceEvolution extends Identifier {
  private double balance;
  private LocalDateTime operationDate;
  private String operationType;
  @ManyToOne
  Platform platform;
}
