package com.lshi.codulate.service;

import com.lshi.codulate.dto.PointDto;
import com.lshi.codulate.model.GeoZone;

public interface GeoZoneService {
    public GeoZone saveFromDto(String name, PointDto[] path);
}
