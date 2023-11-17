package com.api.parkingcontroljdbc.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;
import lombok.Setter;


@Table(name="parking_spot")
@Getter
@Setter
public class ParkingSpot implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	private UUID id;
	
	private String parkingSpotNumber;
	
	private String licensePlateCar;
	
	private String brandCar;
	
	
	private String modelCar;
	
	private String colorCar;	
	
	
	private LocalDateTime registrationDate;
	
	
	private String responsibleName;	
	

	private String apartment;	
	
	private String block;	
}
