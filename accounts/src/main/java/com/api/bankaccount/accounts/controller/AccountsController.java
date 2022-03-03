package com.api.bankaccount.accounts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.bankaccount.accounts.config.AccountsServiceConfig;
import com.api.bankaccount.accounts.model.Accounts;
import com.api.bankaccount.accounts.model.Customer;
import com.api.bankaccount.accounts.model.Properties;
import com.api.bankaccount.accounts.repository.AccountsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
public class AccountsController {

	@Autowired
	private AccountsRepository accountsRepository;
	
	@Autowired
	private AccountsServiceConfig accountsConfig;
	
	@PostMapping("/account-details")
	public Accounts getAccountDetails(@RequestBody Customer customer) {
	
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		
		return accounts;
	}
	
	@GetMapping("/account/properties")
	private String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		
		Properties properties = new Properties(accountsConfig.getMsg(), accountsConfig.getBuildVersion(), 
				accountsConfig.getMailDetails(), accountsConfig.getActiveBranches());
		
		return ow.writeValueAsString(properties);
	}
}
