package com.lshi.mapapi.controller;

import com.lshi.mapapi.dto.LocationRequestDto;
import com.lshi.mapapi.service.GeoService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MapApiController {
    public static Logger LOG = LoggerFactory.getLogger(MapApiController.class);

    private final GeoService geoService;

    @PostMapping("/contains_location")
    public Boolean containsLocation(@RequestBody LocationRequestDto locationRequestDto) {
        return geoService.check(locationRequestDto);
    }
}
