package com.lshi.codulate.service;

import com.lshi.codulate.dto.CoordinateDto;
import com.lshi.codulate.dto.LocationRequestDto;
import com.lshi.codulate.dto.PathDto;
import com.lshi.codulate.dto.PointDto;
import com.lshi.codulate.messaging.Producer;
import com.lshi.codulate.model.GeoZone;
import com.lshi.codulate.repository.GeoCoordinateRepository;
import com.lshi.codulate.repository.GeoZoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class GeoZoneCheckerService {
    public static Logger LOG = LoggerFactory.getLogger(GeoZoneCheckerService.class);

    private final GeoZoneRepository geoZoneRepository;
    private final GeoCoordinateRepository geoCoordinateRepository;

    private final RestTemplate restTemplate;

    private final ExecutorService ioThreadPool;

    private final Producer producer;

    @Value(value = "${app.map-api.url}")
    private String mapApiUrl;

    public GeoZoneCheckerService(GeoZoneRepository geoZoneRepository, GeoCoordinateRepository geoCoordinateRepository, RestTemplate restTemplate, ExecutorService ioThreadPool, Producer producer) {
        this.geoZoneRepository = geoZoneRepository;
        this.geoCoordinateRepository = geoCoordinateRepository;
        this.restTemplate = restTemplate;
        this.ioThreadPool = ioThreadPool;
        this.producer = producer;
    }

    @Transactional
    public GeoZone check(PointDto point) {
        var allGeoZones = geoZoneRepository.findAll();
        for (GeoZone geoZone : allGeoZones) {
            LOG.info("Zone: {}", geoZone);
            var geoCoordinates = geoCoordinateRepository.findByGeoZoneId(geoZone.getId());

            LOG.info("Get coords: {}", geoCoordinates);

            ioThreadPool.execute(() -> {
                try {
                    LocationRequestDto locationRequestDto = new LocationRequestDto(new CoordinateDto(point.getX(), point.getY(), point.getZ()),
                            new PathDto(geoCoordinates.stream().map(c -> new CoordinateDto(c.getX(), c.getY(), c.getZ())).
                                    collect(Collectors.toList()).toArray(new CoordinateDto[]{})));

                    LOG.info("Before request to {} | {}", mapApiUrl, locationRequestDto);
                    HttpEntity<LocationRequestDto> entity = new HttpEntity<>(locationRequestDto, null);
                    String result = restTemplate.exchange(mapApiUrl + "/contains_location", HttpMethod.POST, entity, String.class).getBody();
                    LOG.info("After request to {} | {}", mapApiUrl, result);
                    if (Boolean.parseBoolean(result)) {
                        producer.send("violations-topic", MessageFormat.format("Zone {0} violated", geoZone.getName()));
                    }
                } catch (Exception e) {
                    LOG.error("Error check location: {}", geoZone, e);
                }
            });

        }
        return null;
    }
}
