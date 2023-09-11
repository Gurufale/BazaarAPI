package com.service.referral.events;

import com.service.referral.dto.ReferralRequest;
import com.service.referral.services.ReferralService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserCreatedEventConsumerTest {

    UserCreatedEventConsumer userCreatedEventConsumer;

    @Mock
    ReferralService referralService;

    @Mock
    KafkaTemplate kafkaTemplateMock;

    @BeforeEach
    void setUp() {
        userCreatedEventConsumer = new UserCreatedEventConsumer(referralService, kafkaTemplateMock);
    }

    @Test
    void shouldConsumeUserCreatedEvent() {

        String phoneNumber = "9875645367";
        String email = "a@example.com";
        String preference = "email";
        Long id = 1L;

        ArgumentCaptor<ReferralRequest> referralRequestArgumentCaptor = ArgumentCaptor.forClass(ReferralRequest.class);
        ReferralRequest referralRequest = new ReferralRequest(email, phoneNumber, UUID.randomUUID().toString());

        UserCreatedEvent data = new UserCreatedEvent(id,preference, email, phoneNumber,null);
        userCreatedEventConsumer.consume(data);
        verify(referralService).create(referralRequestArgumentCaptor.capture());
        Assertions.assertEquals(phoneNumber, data.getPhoneNumber());
    }

    @Test
    void shouldPublishReferralRewardEventWhenUserRegisteredWithReferralID() throws Exception {
        UserCreatedEvent userCreatedEvent = new UserCreatedEvent(1L,"email", "a@example.com", "8978987678","Referred-1");

        ReferralRewardEvent referralRewardEvent = new ReferralRewardEvent(userCreatedEvent.getEmail(), userCreatedEvent.getPhoneNumber(), 30.00);
        ArgumentCaptor<ReferralRewardEvent> rewardEventArgumentCaptor = ArgumentCaptor.forClass(ReferralRewardEvent.class);

        String topic = "ReferralRewardTopic";
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        userCreatedEventConsumer.consume(userCreatedEvent);

        verify(kafkaTemplateMock).send(argumentCaptor.capture(),rewardEventArgumentCaptor.capture());

        Assertions.assertEquals("ReferralRewardTopic", argumentCaptor.getValue());
    }
}
