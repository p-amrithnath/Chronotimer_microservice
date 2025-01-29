package com.cts.teams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TeamsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamsApplication.class, args);
	}

}
