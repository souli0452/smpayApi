package com.switchmaker.smpay.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.switchmaker.smpay.abstractClass.Identifier;
import com.switchmaker.smpay.enumeration.UserAccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Keycloak_user")
public class User extends Identifier {
  @JsonIgnore
  private UUID codeIDKeycloak;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String developerAccountSecret;
  private UserAccountType accountType;
  private  Boolean accountActivated;
  private Boolean firstConnexion;
  private LocalDateTime creationDate;
  @ManyToMany
  List<Platform> platforms= new ArrayList<>();
  @ManyToOne
  Customer customer;

}
