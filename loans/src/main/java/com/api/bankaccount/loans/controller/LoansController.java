package com.api.bankaccount.loans.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.api.bankaccount.loans.config.LoansServiceConfig;
import com.api.bankaccount.loans.model.Customer;
import com.api.bankaccount.loans.model.Loans;
import com.api.bankaccount.loans.model.Properties;
import com.api.bankaccount.loans.repository.LoansRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
public class LoansController {

	@Autowired
	private LoansRepository loansRepository;
	
	@Autowired
	private LoansServiceConfig loansConfig;
	
	@PostMapping("/my-loans")
	public List<Loans> getLoansDetails(@RequestHeader("bank-correlation-id") String correlationId, @RequestBody Customer customer) {
		
		System.out.println("INVOKING LOANS MICROSERVICE. TESTING RETRY PATTERN");
		
		List<Loans> loans = loansRepository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());
		
		return loans;
	}
	
	@GetMapping("/loan/properties")
	public String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		
		Properties properties = new Properties(loansConfig.getMsg(), loansConfig.getBuildVersion(), 
				loansConfig.getMailDetails(), loansConfig.getActiveBranches());
		
		return ow.writeValueAsString(properties);
	}
	
}
