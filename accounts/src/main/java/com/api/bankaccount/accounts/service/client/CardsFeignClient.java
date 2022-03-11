package com.api.bankaccount.accounts.service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.api.bankaccount.accounts.model.Cards;
import com.api.bankaccount.accounts.model.Customer;

@FeignClient("cards")
public interface CardsFeignClient {

	@RequestMapping(method = RequestMethod.POST, value = "my-cards", consumes = "application/json")
	List<Cards> getCardDetails(@RequestBody Customer customer);
}