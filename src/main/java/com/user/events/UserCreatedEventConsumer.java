package com.user.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserCreatedEventConsumer {

    private final ObjectMapper objectMapper;

    public UserCreatedEventConsumer() {

        this.objectMapper = new ObjectMapper();
    }

    @KafkaListener(topics = "UserCreatedTopic",groupId = "notificationId")
    public void consume(UserCreatedEvent event)
    {
      log.info("Received notification " + event);
    }

}
