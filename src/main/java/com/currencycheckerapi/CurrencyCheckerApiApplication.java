package com.currencycheckerapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.integration.spring.SpringLiquibase;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.net.http.HttpClient;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class CurrencyCheckerApiApplication {

	private static String zoneId;

	private DataSource dataSource;

	@Autowired
	public CurrencyCheckerApiApplication(DataSource dataSource, @Value("${zoneid:+0}") String zoneId) {
		CurrencyCheckerApiApplication.zoneId = zoneId;
		this.dataSource = dataSource;
	}

	public static void main(String[] args) {
		SpringApplication.run(CurrencyCheckerApiApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return objectMapper;
	}

	@Bean
	public HttpClient httpClient() {
		return HttpClient.newHttpClient();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public SpringLiquibase liquibase() {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setChangeLog("classpath:liquibase-changeLog.xml");
		liquibase.setDataSource(dataSource);
		return liquibase;
	}

	public static String getZoneId() {
		return zoneId;
	}
}
