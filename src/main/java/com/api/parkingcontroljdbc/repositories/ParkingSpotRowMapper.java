package com.api.parkingcontroljdbc.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import com.api.parkingcontroljdbc.models.ParkingSpot;

public class ParkingSpotRowMapper implements RowMapper<ParkingSpot> {

    @Override
    public ParkingSpot mapRow(ResultSet rs, int rowNum) throws SQLException {
    	ParkingSpot parkingSpot = new ParkingSpot();

        parkingSpot.setId((UUID) rs.getObject("id"));
        parkingSpot.setParkingSpotNumber(rs.getString("parking_spot_number"));
        parkingSpot.setLicensePlateCar(rs.getString("license_plate_car"));
        parkingSpot.setBrandCar(rs.getString("brand_car"));
        parkingSpot.setModelCar(rs.getString("model_car"));
        parkingSpot.setColorCar(rs.getString("color_car"));
        parkingSpot.setRegistrationDate(rs.getTimestamp("registration_date").toLocalDateTime());
        parkingSpot.setResponsibleName(rs.getString("responsible_name"));
        parkingSpot.setApartment(rs.getString("apartment"));
        parkingSpot.setBlock(rs.getString("block"));

        return parkingSpot;
    }
}
