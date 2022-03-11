package com.api.bankaccount.accounts.service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.api.bankaccount.accounts.model.Customer;
import com.api.bankaccount.accounts.model.Loans;

@FeignClient("loans")
public interface LoansFeignClient {

	@RequestMapping(method = RequestMethod.POST, value = "my-loans", consumes = "application/json")
	List<Loans> getLoanDetails(@RequestBody Customer customer);
}
