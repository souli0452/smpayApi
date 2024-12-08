package com.switchmaker.smpay.entities;

import com.switchmaker.smpay.abstractClass.Identifier;
import lombok.*;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Role extends Identifier {
  private String name;
  private String description;
  private Boolean composite;
  private Boolean clientRole;
  private String containerId;

}
