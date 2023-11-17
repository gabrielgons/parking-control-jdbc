package com.api.parkingcontroljdbc.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.parkingcontroljdbc.repositories.ParkingSpotRepository;
import com.api.parkingcontroljdbc.models.ParkingSpot;

@Service
public class ParkingSpotService {
	
	final ParkingSpotRepository parkingSpotRepository;
	
	public ParkingSpotService(ParkingSpotRepository parkingSpotRepository) {
		
		this.parkingSpotRepository = parkingSpotRepository;
	}

	@Transactional
	public ParkingSpot save(ParkingSpot parkingSpotModel) {
		return parkingSpotRepository.save(parkingSpotModel);
	}

	public boolean existsByLicensePlateCar(String licensePlateCar) {
		return parkingSpotRepository.existsByLicensePlateCar(licensePlateCar);		
	}
	
	public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
		return parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
	}
	
	public boolean existsByApartmentAndBlock(String apartment, String block) {
		return parkingSpotRepository.existsByApartmentAndBlock(apartment, block);
	}

	public Page<ParkingSpot> findAll(Pageable pageable) {
		return parkingSpotRepository.findAll(pageable);
	}

	public Optional<ParkingSpot> findById(UUID id) {
		return parkingSpotRepository.findById(id);
	}

	@Transactional
	public void delete(ParkingSpot parkingSpotModel) {
		parkingSpotRepository.delete(parkingSpotModel);		
	}	
}
