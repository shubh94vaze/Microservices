package com.codedecode.microservices.VaccinationCenter.Repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codedecode.microservices.VaccinationCenter.Entity.VaccinationCenter;

public interface CenterRepo extends JpaRepository<VaccinationCenter,Integer> {
	
	

}
