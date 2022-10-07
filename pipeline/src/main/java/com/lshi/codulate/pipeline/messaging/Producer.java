package com.lshi.codulate.pipeline.messaging;

import lombok.AllArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Producer {
    private final JmsTemplate jmsTemplate;

    public void send(String topic, String message) {
        jmsTemplate.send(topic, session -> session.createTextMessage(message));
    }
}
