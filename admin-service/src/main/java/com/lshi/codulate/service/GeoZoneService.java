package com.lshi.codulate.service;

import com.lshi.codulate.dto.PointDto;
import com.lshi.codulate.model.GeoCoordinate;
import com.lshi.codulate.model.GeoZone;
import com.lshi.codulate.repository.GeoZoneRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class GeoZoneService {
    private final GeoZoneRepository geoZoneRepository;
    private final GeoCoordinateService geoCoordinateService;

    @Transactional
    public GeoZone saveFromDto(String name, PointDto[] path) {

        GeoZone geoZone = new GeoZone();
        geoZone.setName(name);
        geoZoneRepository.save(geoZone);

        for (PointDto pointDto : path) {
            GeoCoordinate geoCoordinate = new GeoCoordinate(geoZone, pointDto.getX(), pointDto.getY(), pointDto.getZ());
            geoCoordinateService.save(geoCoordinate);
        }

        return geoZone;
    }
}
