package com.lshi.codulate.service.impl;

import com.lshi.codulate.model.GeoCoordinate;
import com.lshi.codulate.repository.GeoCoordinateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GeoCoordinateServiceImpl implements com.lshi.codulate.service.GeoCoordinateService {
    private final GeoCoordinateRepository geoCoordinateRepository;

    public void save(GeoCoordinate geoCoordinate) {
        geoCoordinateRepository.save(geoCoordinate);
    }
}
