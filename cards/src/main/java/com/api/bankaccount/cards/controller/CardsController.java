package com.api.bankaccount.cards.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.bankaccount.cards.config.CardsServiceConfig;
import com.api.bankaccount.cards.model.Cards;
import com.api.bankaccount.cards.model.Customer;
import com.api.bankaccount.cards.model.Properties;
import com.api.bankaccount.cards.repository.CardsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
public class CardsController {

	@Autowired
	private CardsRepository cardsRepository;
	
	@Autowired
	private CardsServiceConfig cardsConfig;
	
	@PostMapping("/my-cards")
	public List<Cards> getCardsDetails(@RequestBody Customer customer) {
		
		List<Cards> cards = cardsRepository.findByCustomerId(customer.getCustomerId());
		
		return cards;
	}
	
	@GetMapping("/card/properties")
	private String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		
		Properties properties = new Properties(cardsConfig.getMsg(), cardsConfig.getBuildVersion(), 
				cardsConfig.getMailDetails(), cardsConfig.getActiveBranches());
	
		return ow.writeValueAsString(properties);
	}
}
