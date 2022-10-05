package com.lshi.mapapi.service;

import com.lshi.mapapi.dto.LocationRequestDto;
import org.locationtech.jts.algorithm.locate.SimplePointInAreaLocator;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GeoService {
    public boolean check(LocationRequestDto locationRequestDto) {
        GeometryFactory gf = new GeometryFactory();
        var coordinate = new Coordinate(locationRequestDto.getPoint().getX(), locationRequestDto.getPoint().getY());
        var pathArray = Stream.of(locationRequestDto.getPath().getPath()).map(p -> new Coordinate(p.getX(), p.getY(), p.getZ())).collect(Collectors.toList()).toArray(new Coordinate[]{});
        Polygon polygon = gf.createPolygon(gf.createLinearRing(pathArray), null);
        return SimplePointInAreaLocator.containsPointInPolygon(coordinate, polygon);
    }
}
