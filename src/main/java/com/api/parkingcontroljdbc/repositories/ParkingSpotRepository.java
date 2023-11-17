package com.api.parkingcontroljdbc.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.api.parkingcontroljdbc.models.ParkingSpot;

@Repository
public class ParkingSpotRepository implements BaseRepository<ParkingSpot> {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	//TODO: gerar esses comandos de forma genérica para que possam ser reaproveitados em outros repositories.
	
	@Override
	public ParkingSpot save(ParkingSpot entity) {
		if (entity.getId() == null) {
			final String insertQuery = "INSERT INTO parking_spot (id, apartment, block, brand_car, color_car, license_plate_car, model_car, parking_spot_number, registration_date, responsible_name)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			entity.setId(UUID.randomUUID());

			jdbcTemplate.update(insertQuery, entity.getId(), entity.getApartment(), entity.getBlock(),
					entity.getBrandCar(), entity.getColorCar(), entity.getLicensePlateCar(), entity.getModelCar(),
					entity.getParkingSpotNumber(), entity.getRegistrationDate(), entity.getResponsibleName());

		} else {
			final String updateQuery = "UPDATE parking_spot SET apartment=?, block=?, brand_car=?, color_car=?, license_plate_car=?, model_car=?, parking_spot_number=?, registration_date=?, responsible_name=? WHERE id = ?";
			jdbcTemplate.update(updateQuery, entity.getApartment(), entity.getBlock(), entity.getBrandCar(),
					entity.getColorCar(), entity.getLicensePlateCar(), entity.getModelCar(),
					entity.getParkingSpotNumber(), entity.getRegistrationDate(), entity.getResponsibleName(),
					entity.getId());
		}

		return entity;
	}

	@Override
	public void delete(ParkingSpot entity) {
		final String deleteQuery = "DELETE FROM parking_spot WHERE id = ?";

		jdbcTemplate.update(deleteQuery, entity.getId());

	}

	@Override
	public Optional<ParkingSpot> findById(UUID id) {

		final String query = "SELECT * FROM parking_spot WHERE id = ?";

		List<ParkingSpot> results = jdbcTemplate.query(query, new ParkingSpotRowMapper(), id);

		if (results.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(results.get(0));
		}
	}

	@Override
	public Page<ParkingSpot> findAll(Pageable pageable) {
		final String rowCountQuery = "SELECT COUNT(*) FROM parking_spot";
		final String query = "SELECT * FROM parking_spot LIMIT ? OFFSET ?";

		int total = jdbcTemplate.queryForObject(rowCountQuery, Integer.class);

		int limit = pageable.getPageSize();
		long offset = pageable.getOffset();

		List<ParkingSpot> parkingSpots = jdbcTemplate.query(query, new ParkingSpotRowMapper(),limit, offset);

		return new PageImpl<>(parkingSpots, pageable, total);
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
