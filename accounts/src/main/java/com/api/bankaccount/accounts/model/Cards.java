package com.api.bankaccount.accounts.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Cards {

	private int cardId;
	
	private String cardNumber;
	
	private int customerId;
	
	private String cardType;
	
	private double totalLimit;
	
	private double amountUsed;
	
	private double availableAmount;
	
	private LocalDate createDt;
}
