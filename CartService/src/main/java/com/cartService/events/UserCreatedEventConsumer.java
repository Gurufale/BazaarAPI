package com.cartService.events;

import com.cartService.dto.CartRequest;
import com.cartService.services.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
public class UserCreatedEventConsumer {

    private final CartService cartService;

    public UserCreatedEventConsumer(CartService cartService) {
        this.cartService = cartService;
    }

    @KafkaListener(topics = "UserCreatedTopic", groupId = "notificationIdCart")
    public void consume(UserCreatedEvent event) {
        log.info("Received notification " + event);
        CartRequest cartRequest = new CartRequest(event.getId(), new ArrayList<>());
        cartService.create(cartRequest);
    }
}


