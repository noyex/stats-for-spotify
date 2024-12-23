package com.jts.stats_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = {"com.jts.stats_client", "com.jts.stats_data", "com.jts.stats_service", "com.jts.stats_api"})
@EnableJpaRepositories(basePackages = "com.jts.stats_data")
@EntityScan(basePackages = "com.jts.stats_data")
public class StatsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatsApiApplication.class, args);
	}

}
