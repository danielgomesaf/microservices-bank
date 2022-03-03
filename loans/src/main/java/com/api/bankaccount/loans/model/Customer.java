package com.api.bankaccount.loans.model;

import javax.persistence.Column;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Customer {

	@Column(name = "customer_id")
	private int customerId;
}
