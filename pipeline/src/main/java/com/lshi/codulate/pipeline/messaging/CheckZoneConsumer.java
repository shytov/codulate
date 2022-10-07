package com.lshi.codulate.pipeline.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lshi.codulate.pipeline.dto.LocationRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static com.lshi.codulate.pipeline.Constants.PIPELINE_QUEUE;
import static com.lshi.codulate.pipeline.Constants.VIOLATIONS_TOPIC;

@Component
public class CheckZoneConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(CheckZoneConsumer.class);

    private final ObjectMapper objectMapper;
    private final ExecutorService ioThreadPool;
    private final ExecutorService jmsThreadPool;
    private final RestTemplate restTemplate;
    private final Producer producer;

    @Value(value = "${app.map-api.url}")
    private String mapApiUrl;

    public CheckZoneConsumer(ObjectMapper objectMapper,
                             ExecutorService ioThreadPool,
                             ExecutorService jmsThreadPool,
                             RestTemplate restTemplate,
                             Producer producer) {
        this.objectMapper = objectMapper;
        this.ioThreadPool = ioThreadPool;
        this.jmsThreadPool = jmsThreadPool;
        this.restTemplate = restTemplate;
        this.producer = producer;
    }

    @JmsListener(destination = PIPELINE_QUEUE, concurrency = "1-3")
    public void consumeMessage(String message) {
        LOG.info("Message received from activemq topic {}", message);

        LocationRequestDto locationRequestDto;
        try {
            locationRequestDto = objectMapper.readValue(message, LocationRequestDto.class);

            CompletableFuture
                    .supplyAsync(() -> makeRequest(locationRequestDto), ioThreadPool)
                    .thenAcceptAsync((result) -> {
                        if (result != null && result) {
                            producer.send(VIOLATIONS_TOPIC, MessageFormat.format("Zone {0} violated", locationRequestDto.getZoneName()));
                        }
                    }, jmsThreadPool)
                    .handle((r, e) -> {
                        if(e != null) {
                            LOG.error("Error in check zone {}", message, e);
                        }
                        return false;
                    })
                    .get(1, TimeUnit.MINUTES);
        } catch (Exception e) {
            LOG.error("JMS message is invalid: {}", message, e);
            return;
        }
    }

    private Boolean makeRequest(LocationRequestDto locationRequestDto) {
        LOG.info("Before request to {} | {}", mapApiUrl, locationRequestDto);
        HttpEntity<LocationRequestDto> entity = new HttpEntity<>(locationRequestDto, null);
        String result = restTemplate.exchange(mapApiUrl + "/contains_location", HttpMethod.POST, entity, String.class).getBody();
        LOG.info("After request to {} | {}", mapApiUrl, result);
        return Boolean.parseBoolean(result);
    }
}
