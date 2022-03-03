package com.api.bankaccount.cards.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
public class Cards {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "card_id")
	private int cardId;
	
	@Column(name = "card_number")
	private String cardNumber;
	
	@Column(name = "customer_id")
	private int customerId;
	
	@Column(name = "card_type")
	private String cardType;
	
	@Column(name = "total_limit")
	private double totalLimit;
	
	@Column(name = "amount_used")
	private double amountUsed;
	
	@Column(name = "available_amount")
	private double availableAmount;
	
	@Column(name = "create_dt")
	private LocalDate createDt;
}
