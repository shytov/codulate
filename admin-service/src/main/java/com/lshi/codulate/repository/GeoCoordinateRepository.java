package com.lshi.codulate.repository;

import com.lshi.codulate.model.GeoCoordinate;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GeoCoordinateRepository extends CrudRepository<GeoCoordinate, Long> {
    public List<GeoCoordinate> findByGeoZoneId(Long zoneId);
}
