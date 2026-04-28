package com.dt;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync 
public class IL_CampaignManagement {

	public static void main(String[] args) {
		SpringApplication.run(IL_CampaignManagement.class, args);
	}
//	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
