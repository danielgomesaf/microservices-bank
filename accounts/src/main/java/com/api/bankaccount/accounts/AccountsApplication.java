package com.api.bankaccount.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication

/*
 * this annotation indicate to Spring that it needs to expose a endpoint url
 * with the name refresh, that we can invoke to reload the properties setted in the
 * configserver without the need to restart the microservice
 * this do not work for configurations used to access databases or smtp, because they
 * need a server restart to actually be registered by the microservice
 * */
@RefreshScope


@ComponentScans({ @ComponentScan("com.api.bankaccount.accounts.controller") })
@EnableJpaRepositories("com.api.bankaccount.accounts.repository")
@EntityScan("com.api.bankaccount.accounts.model")
@EnableFeignClients
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
