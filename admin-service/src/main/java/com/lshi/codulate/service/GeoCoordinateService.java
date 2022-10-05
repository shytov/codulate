package com.lshi.codulate.service;

import com.lshi.codulate.model.GeoCoordinate;
import com.lshi.codulate.repository.GeoCoordinateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GeoCoordinateService {
    private final GeoCoordinateRepository geoCoordinateRepository;

    public void save(GeoCoordinate geoCoordinate) {
        geoCoordinateRepository.save(geoCoordinate);
    }
}
