# Microservices
Sample Microservice based application

version1 -- simple microservice application
================================================================================================================================================================================================================================================
version2 -- added circuitbreaker pattern to vaccinationCenter service
steps involved:

step1: add hystrix dependency to the pom.xml of vaccinationcenter
---------------------------------------------------------------------
 <dependency>
			 <groupId>org.springframework.cloud</groupId>
			 <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
			 <version>2.2.8.RELEASE</version>
		 </dependency>

-----------------------------------------------------------------------------
**step2 : add @EnableCircuitBreaker in main class of VaccinationCenter Service**

package com.codedecode.microservices.VaccinationCenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCircuitBreaker
public class VaccinationCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaccinationCenterApplication.class, args);
	}
	
	
	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate()
	{
		return new RestTemplate();
	}

}

------------------------------------------------------------------------------
**Step3:  add @HystrixCommand to methods that needs breaking of circuit and configure it.**

@RequestMapping(path = "/id/{id}")
	@HystrixCommand(fallbackMethod = "handleCitizenDownTime")
	public ResponseEntity<RequiredResponse> getAllDataBasedOnCenterId(@PathVariable Integer id) {
		
		RequiredResponse requiredResponse = new RequiredResponse();

		// 1st get vaccinationcenter details
		VaccinationCenter center = centerRepo.findById(id).get();
        requiredResponse.setCenter(center);

		// then get all citizens refistered to vaccination center
		java.util.List<Citizen> listOfCitizens = restTemplate.getForObject("http://CITIZEN-SERVICE/citizen/id/" + id,List.class);

		// java.util.List<Citizen>
		// listOfCitizens=restTemplate.getForObject("http://localhost:8081/citizen/id/"+id,
		// List.class);
		requiredResponse.setCitizens(listOfCitizens);

		return new ResponseEntity<RequiredResponse>(requiredResponse, HttpStatus.OK);
	}
	
      public ResponseEntity<RequiredResponse> handleCitizenDownTime(@PathVariable Integer id) {
		
		RequiredResponse requiredResponse = new RequiredResponse();
		VaccinationCenter center = centerRepo.findById(id).get();
        requiredResponse.setCenter(center);
		return new ResponseEntity<RequiredResponse>(requiredResponse, HttpStatus.OK);
	}



================================================================================================================================================================================================================================================
