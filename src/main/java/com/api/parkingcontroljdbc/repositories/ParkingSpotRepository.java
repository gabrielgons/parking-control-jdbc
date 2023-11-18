package com.api.parkingcontroljdbc.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.api.parkingcontroljdbc.models.ParkingSpot;
import com.api.parkingcontroljdbc.repositories.rowmappers.ParkingSpotRowMapper;

@Repository
public class ParkingSpotRepository extends BaseRepository<ParkingSpot> {

	@Autowired
	ParkingSpotRepository() {
		super("parking_spot", new ParkingSpotRowMapper());
	}

	public Boolean existsByApartmentAndBlock(String apartment, String block) {
		final String query = "SELECT * FROM parking_spot WHERE apartment = ? AND block = ?";

		List<ParkingSpot> results = jdbcTemplate.query(query, new ParkingSpotRowMapper(), apartment, block);

		return !results.isEmpty();
	}

	public Boolean existsByParkingSpotNumber(String parkingSpotNumber) {
		final String query = "SELECT * FROM parking_spot WHERE parking_spot_number = ?";

		List<ParkingSpot> results = jdbcTemplate.query(query, new ParkingSpotRowMapper(), parkingSpotNumber);

		return !results.isEmpty();
	}

	public Boolean existsByLicensePlateCar(String licensePlateCar) {
		final String query = "SELECT * FROM parking_spot WHERE license_plate_car = ?";

		List<ParkingSpot> results = jdbcTemplate.query(query, new ParkingSpotRowMapper(), licensePlateCar);

		return !results.isEmpty();
	}

}
