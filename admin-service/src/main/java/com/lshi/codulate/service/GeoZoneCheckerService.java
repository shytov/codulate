package com.lshi.codulate.service;

import com.lshi.codulate.dto.LocationRequestDto;
import com.lshi.codulate.dto.PointDto;

import javax.transaction.Transactional;
import java.util.List;

public interface GeoZoneCheckerService {
    @Transactional
    List<LocationRequestDto> getRequestData(PointDto point);

    void checkAsync(List<LocationRequestDto> requestData);
}
