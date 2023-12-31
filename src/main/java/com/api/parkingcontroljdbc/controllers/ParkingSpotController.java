package com.api.parkingcontroljdbc.controllers;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.parkingcontroljdbc.dtos.ParkingSpotDto;
import com.api.parkingcontroljdbc.models.ParkingSpot;
import com.api.parkingcontroljdbc.services.ParkingSpotService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {
	
	final ParkingSpotService parkingSpotService;

	public ParkingSpotController(ParkingSpotService parkingSpotService) {
		this.parkingSpotService = parkingSpotService;
	}
	

	@PostMapping
	public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto) throws IllegalArgumentException, IllegalAccessException{
		
		if(parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Licence Plate Car is already in use!");
		}
		
		if(parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
		}
		
		
		if(parkingSpotService.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict:  Parking Spot is already registered!");
		}		
		
		var parkingSpotModel = new ParkingSpot();
		BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
		parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));		
	}
	
	@PutMapping
	public ResponseEntity<Object> putParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto) throws IllegalArgumentException, IllegalAccessException{
		
		var parkingSpotBanco = parkingSpotService.findById(parkingSpotDto.getId());
		
		if(parkingSpotBanco.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageFormat.format("Not found: id {0} não encontrada.", parkingSpotDto.getId()));
		}
		
		var parkingSpotModel = new ParkingSpot();
		BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);		
		parkingSpotModel.setRegistrationDate(parkingSpotBanco.get().getRegistrationDate());
		parkingSpotModel.setParkingSpotNumber(parkingSpotBanco.get().getParkingSpotNumber());
		parkingSpotModel.setApartment(parkingSpotBanco.get().getApartment());
		parkingSpotModel.setBlock(parkingSpotBanco.get().getBlock());		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));		
	}
	
	@GetMapping
	public ResponseEntity<Page<ParkingSpot>> getAllParkingSpots(@PageableDefault(page = 0, size =10, sort= "id", direction = Sort.Direction.ASC) Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll(pageable));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value="id") UUID id) {
		Optional<ParkingSpot> parkingSpotOptional = parkingSpotService.findById(id);
		
		if(!parkingSpotOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotOptional.get());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value="id") UUID id) {
		Optional<ParkingSpot> parkingSpotOptional = parkingSpotService.findById(id);
		
		if(!parkingSpotOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
		}
		
		parkingSpotService.delete(parkingSpotOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully");
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateParkingSpot(@PathVariable(value="id") UUID id, @RequestBody @Valid ParkingSpotDto parkingSpotDto) throws IllegalArgumentException, IllegalAccessException {
		Optional<ParkingSpot> parkingSpotDoBancoOptional = parkingSpotService.findById(id);
		
		if(!parkingSpotDoBancoOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
		}
		
		var parkingSpotModel = new ParkingSpot();
		BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
		parkingSpotModel.setId(parkingSpotDoBancoOptional.get().getId());
		parkingSpotModel.setRegistrationDate(parkingSpotDoBancoOptional.get().getRegistrationDate());
		
		
		
		parkingSpotService.save(parkingSpotDoBancoOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));
	}

}
