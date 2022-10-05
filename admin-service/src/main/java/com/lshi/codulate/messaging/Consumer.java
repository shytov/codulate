package com.lshi.codulate.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lshi.codulate.dto.PointDto;
import com.lshi.codulate.service.GeoZoneCheckerService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.text.MessageFormat;

@Component
@AllArgsConstructor
public class Consumer {
    private static final Logger LOG = LoggerFactory.getLogger(Consumer.class);

    private final ObjectMapper objectMapper;
    private final GeoZoneCheckerService geoZoneCheckerService;

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = "coordinates-topic")
    public void consumeMessage(String message) {
        LOG.info("Message received from activemq topic {}", message);

        PointDto userPoint;
        try{
            userPoint = objectMapper.readValue(message, PointDto.class);
        } catch (Exception e) {
            LOG.error("JMS message is invalid: {}", message, e);
            return;
        }

        var foundZone = geoZoneCheckerService.check(userPoint);
        if(foundZone != null) {
            jmsTemplate.send("violations-topic", new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(MessageFormat.format("Zone {0} violated", foundZone.getName()));
                }
            });
        }
    }
}
