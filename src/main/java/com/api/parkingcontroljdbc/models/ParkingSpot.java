package com.api.parkingcontroljdbc.models;

import java.time.LocalDateTime;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "parking_spot")
@Getter
@Setter
public class ParkingSpot extends Entity {

	private static final long serialVersionUID = 1L;

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
