package com.user.events;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserCreatedEventConsumerTest {

    UserCreatedEventConsumer userCreatedEventConsumer;

    @BeforeEach
    void setUp() {
        userCreatedEventConsumer = new UserCreatedEventConsumer();
    }

    @Test
    void shouldConsumeUserCreatedEvent() {
        Long id = 1L;
        String phoneNumber = "9875645367";
        String email = "a@example.com";
        String preference = "email";
        String referredBy = "Refer-1";

        UserCreatedEvent data = new UserCreatedEvent(id,preference, email, phoneNumber,referredBy);
        userCreatedEventConsumer.consume(data);
        Assertions.assertEquals(phoneNumber, data.getPhoneNumber());
    }
}
