package com.bankaccount.gatewayserver.filters;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Order(1)
@Component(value = "customTraceFilter")
public class TraceFilter implements GlobalFilter {

	private static final Logger logger = LoggerFactory.getLogger(TraceFilter.class);
	
	@Autowired
	private FilterUtility filterUtility;
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
		
		if (isCorrelationIdPresent(requestHeaders)) {
			logger.debug("Bank-correlation-id found in tracing filter: {}. ", 
					filterUtility.getCorrelationId(requestHeaders));
		} else {
			String correlationID = generateCorrelationId();
			
			exchange = filterUtility.setCorrelationId(exchange, correlationID);
			
			logger.debug("Bank-correlation-id generated in tracing filter: {}.", correlationID);
		}
		
		return chain.filter(exchange);
	}
	
	private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
		return filterUtility.getCorrelationId(requestHeaders) != null;
	}
	
	private String generateCorrelationId() {
		return UUID.randomUUID().toString();
	}

}
