package com.api.bankaccount.loans.model;

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
public class Loans {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "loan_number")
	private int loanNumber;
	
	@Column(name = "customer_id")
	private int customerId;
	
	@Column(name = "start_dt")
	private LocalDate startDt;
	
	@Column(name = "loan_type")
	private String loanType;
	
	@Column(name = "total_loan")
	private double totalLoan;
	
	@Column(name = "amount_paid")
	private double amountPaid;
	
	@Column(name = "outstanding_amount")
	private double outstandingAmount;
	
	@Column(name = "create_dt")
	private LocalDate createDt;
}
