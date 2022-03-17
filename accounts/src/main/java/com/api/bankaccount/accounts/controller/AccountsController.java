package com.api.bankaccount.accounts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class AccountsController {

	@Autowired
	private AccountsRepository accountsRepository;
	
	@Autowired
	private AccountsServiceConfig accountsConfig;
	
	@Autowired
	private LoansFeignClient loansFeignClient;
	
	@Autowired
	private CardsFeignClient cardsFeignClient;
	
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
	
	@PostMapping("/my-customer-details")
	//@CircuitBreaker(name = "detailsForCustomerSupportApp", fallbackMethod = "myCustomerDetailsFallBack")
	@Retry(name = "retryForCustomerDetails", fallbackMethod = "myCustomerDetailsFallBack")
	public CustomerDetails myCustomerDetails(@RequestBody Customer customer) {
		
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loans = loansFeignClient.getLoanDetails(customer);
		List<Cards> cards = cardsFeignClient.getCardDetails(customer);
		
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setLoans(loans);
		customerDetails.setCards(cards);
		
		return customerDetails;
	}
	
	// The fallback method also have to have the same parameters that the method where the
	// annotation of the Circuit Breaker is present.
	// The Throwable parameter is mandatory. It's passed in case the type of exception
	// threw is relevant to the business logic.
	@SuppressWarnings("unused")
	private CustomerDetails myCustomerDetailsFallBack(Customer customer, Throwable t) {
		
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loans = loansFeignClient.getLoanDetails(customer);
		
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
