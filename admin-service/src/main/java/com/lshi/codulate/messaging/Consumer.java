package com.lshi.codulate.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lshi.codulate.dto.PointDto;
import com.lshi.codulate.service.GeoZoneCheckerService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

import static com.lshi.codulate.Constants.COORDINATES_TOPIC;

/**
 * Listen for user check location request
 */
@Component
@AllArgsConstructor
public class Consumer {
    private static final Logger LOG = LoggerFactory.getLogger(Consumer.class);

    private final ObjectMapper objectMapper;
    private final GeoZoneCheckerService geoZoneCheckerService;
    private final ExecutorService dbThreadPool;

    @JmsListener(destination = COORDINATES_TOPIC)
    public void consumeMessage(String message) {
        LOG.info("Message received from activemq topic {}", message);

        PointDto userPoint;
        try {
            userPoint = objectMapper.readValue(message, PointDto.class);
        } catch (Exception e) {
            LOG.error("JMS message is invalid: {}", message, e);
            return;
        }

        handleUserPoint(userPoint);
    }

    private void handleUserPoint(PointDto userPoint) {
        dbThreadPool.execute(() -> {
            try {
                var requestData = geoZoneCheckerService.getRequestData(userPoint);
                geoZoneCheckerService.checkAsync(requestData);
            } catch (Exception e) {
                LOG.error("Get zones data error: {}", userPoint, e);
            }
        });
    }
}
