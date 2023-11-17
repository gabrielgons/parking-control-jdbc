package com.api.parkingcontroljdbc.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.parkingcontroljdbc.models.ParkingSpot;

public interface BaseRepository<T> {	
 ParkingSpot save(T entity);
 void delete(T entity);
 Optional<ParkingSpot> findById(UUID id);
 Page<T> findAll(Pageable pageable);
}
