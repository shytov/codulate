package com.lshi.codulate.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lshi.codulate.dto.CoordinateDto;
import com.lshi.codulate.dto.LocationRequestDto;
import com.lshi.codulate.dto.PathDto;
import com.lshi.codulate.dto.PointDto;
import com.lshi.codulate.messaging.Producer;
import com.lshi.codulate.model.GeoZone;
import com.lshi.codulate.repository.GeoCoordinateRepository;
import com.lshi.codulate.repository.GeoZoneRepository;
import com.lshi.codulate.service.GeoZoneCheckerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static com.lshi.codulate.Constants.PIPELINE_QUEUE;

@Service
public class GeoZoneCheckerServiceImpl implements GeoZoneCheckerService {
    public static Logger LOG = LoggerFactory.getLogger(GeoZoneCheckerServiceImpl.class);

    private final GeoZoneRepository geoZoneRepository;
    private final GeoCoordinateRepository geoCoordinateRepository;
    private final ExecutorService jmsThreadPool;
    private final Producer producer;
    private final ObjectMapper objectMapper;

    @Value(value = "${app.map-api.url}")
    private String mapApiUrl;

    public GeoZoneCheckerServiceImpl(GeoZoneRepository geoZoneRepository,
                                     GeoCoordinateRepository geoCoordinateRepository,
                                     ExecutorService jmsThreadPool,
                                     Producer producer,
                                     ObjectMapper objectMapper) {
        this.geoZoneRepository = geoZoneRepository;
        this.geoCoordinateRepository = geoCoordinateRepository;
        this.jmsThreadPool = jmsThreadPool;
        this.producer = producer;
        this.objectMapper = objectMapper;
    }

    /**
     * Get zones and their path coordinates and build request DTO
     *
     * @param point user point location
     * @return
     */
    @Override
    @Transactional
    public List<LocationRequestDto> getRequestData(PointDto point) {
        List<LocationRequestDto> requestData = new ArrayList<>();
        var allGeoZones = geoZoneRepository.findAll();
        for (GeoZone geoZone : allGeoZones) {
            LOG.info("Zone: {}", geoZone);
            var geoCoordinates = geoCoordinateRepository.findByGeoZoneId(geoZone.getId());
            LOG.info("Get coords: {}", geoCoordinates);
            LocationRequestDto locationRequestDto = new LocationRequestDto(geoZone.getName(), new CoordinateDto(point.getX(), point.getY(), point.getZ()),
                    new PathDto(geoCoordinates.stream().map(c -> new CoordinateDto(c.getX(), c.getY(), c.getZ())).
                            collect(Collectors.toList()).toArray(new CoordinateDto[]{})));
            requestData.add(locationRequestDto);
        }
        return requestData;
    }

    /**
     * Make check requests to external service, that can take a lot of time.
     *
     * @param requestData
     */
    @Override
    public void checkAsync(List<LocationRequestDto> requestData) {
        for (LocationRequestDto locationRequestDto : requestData) {
            sendToJms(locationRequestDto);
        }
    }

    private void sendToJms(LocationRequestDto locationRequestDto) {
        jmsThreadPool.execute(() -> {
            try {
                producer.send(PIPELINE_QUEUE, objectMapper.writeValueAsString(locationRequestDto));
            } catch (Exception e) {
                LOG.error("Error send JMS result: {}", locationRequestDto, e);
            }
        });
    }
}
