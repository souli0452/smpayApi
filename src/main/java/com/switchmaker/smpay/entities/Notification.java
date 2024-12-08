package com.switchmaker.smpay.entities;

import com.switchmaker.smpay.abstractClass.Identifier;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification")
public class Notification extends Identifier{
	@Column(name="message", columnDefinition="text")
	private String message;
	private boolean status = false;
	private LocalDateTime date;
	@OneToOne
	private Customer costomer;
}
