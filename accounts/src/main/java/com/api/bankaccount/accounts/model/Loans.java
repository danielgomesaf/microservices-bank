package com.api.bankaccount.accounts.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Loans {

	private int loanNumber;
	
	private int customerId;
	
	private LocalDate startDt;
	
	private String loanType;
	
	private double totalLoan;
	
	private double amountPaid;
	
	private double outstandingAmount;
	
	private LocalDate createDt;
}
