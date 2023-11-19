package com.api.parkingcontroljdbc.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingSpotDto {

	private UUID id;
	
	@NotBlank
	private String parkingSpotNumber;

	@NotBlank
	@Size(max = 7)
	private String licensePlateCar;

	@NotBlank
	private String brandCar;

	@NotBlank
	private String modelCar;

	@NotBlank
	private String colorCar;

	@NotBlank
	private String responsibleName;

	@NotBlank
	private String apartment;

	@NotBlank
	private String block;

}
