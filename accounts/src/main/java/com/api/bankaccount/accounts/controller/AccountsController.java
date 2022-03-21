package com.api.bankaccount.accounts.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.api.bankaccount.accounts.config.AccountsServiceConfig;
import com.api.bankaccount.accounts.model.Accounts;
import com.api.bankaccount.accounts.model.Cards;
import com.api.bankaccount.accounts.model.Customer;
import com.api.bankaccount.accounts.model.CustomerDetails;
import com.api.bankaccount.accounts.model.Loans;
import com.api.bankaccount.accounts.model.Properties;
import com.api.bankaccount.accounts.repository.AccountsRepository;
import com.api.bankaccount.accounts.service.client.CardsFeignClient;
import com.api.bankaccount.accounts.service.client.LoansFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.core.annotation.Timed;

@RestController
public class AccountsController {

	private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);
	
	@Autowired
	private AccountsRepository accountsRepository;
	
	@Autowired
	private AccountsServiceConfig accountsConfig;
	
	@Autowired
	private LoansFeignClient loansFeignClient;
	
	@Autowired
	private CardsFeignClient cardsFeignClient;
	
	@PostMapping("/account-details")
	
	// this is a custom metric created by the user using the micrometer library
	@Timed(value = "getAccountDetails.time", description = "Time taken to return Account Details")
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
	
	@PostMapping("/my-customer-details")
	@CircuitBreaker(name = "detailsForCustomerSupportApp", fallbackMethod = "myCustomerDetailsFallBack")
	@Retry(name = "retryForCustomerDetails", fallbackMethod = "myCustomerDetailsFallBack")
	public CustomerDetails myCustomerDetails(@RequestHeader("bank-correlation-id") String correlationId, @RequestBody Customer customer) {
	
		logger.info("myCustomerDetails() method started");
		
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loans = loansFeignClient.getLoanDetails(correlationId, customer);
		List<Cards> cards = cardsFeignClient.getCardDetails(correlationId, customer);
		
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setLoans(loans);
		customerDetails.setCards(cards);
		
		logger.info("myCustomerDetails() method ended");
		
		return customerDetails;
	}
	
	// The fallback method also have to have the same parameters that the method where the
	// annotation of the Circuit Breaker is present.
	// The Throwable parameter is mandatory. It's passed in case the type of exception
	// threw is relevant to the business logic.
	@SuppressWarnings("unused")
	private CustomerDetails myCustomerDetailsFallBack(String correlationId, Customer customer, Throwable t) {
		
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loans = loansFeignClient.getLoanDetails(correlationId, customer);
		
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setLoans(loans);
		
		return customerDetails;
	}
	
	@GetMapping("/sayHello")
	@RateLimiter(name = "sayHello", fallbackMethod = "sayHelloFallback")
	public String sayHello() {
		return "HELLO! TESTING RATE LIMITER PATTERN";
	}
	
	@SuppressWarnings("unused")
	private String sayHelloFallback(Throwable t) {
		return "HELLO! TESTING RATE LIMITER PATTERN FALLBACK METHOD";
	}
}
