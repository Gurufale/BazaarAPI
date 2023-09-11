package com.service.referral.events;

import com.service.referral.dto.ReferralRequest;
import com.service.referral.services.ReferralService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class UserCreatedEventConsumer {

    private final ReferralService referralService;
    private final KafkaTemplate<String, ReferralRewardEvent> kafkaTemplate;
    @Value("${reward.balance}")
    private double balance;

    public UserCreatedEventConsumer(ReferralService referralService, KafkaTemplate<String, ReferralRewardEvent> kafkaTemplate) {
        this.referralService = referralService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "UserCreatedTopic", groupId = "notificationIdUser")
    public void consume(UserCreatedEvent event) {
        log.info("Received notification " + event);

        if (event.getReferredBy() != null) {
            ReferralRewardEvent referralRewardEvent = new ReferralRewardEvent(event.getEmail(), event.getPhoneNumber(), balance);
            kafkaTemplate.send("ReferralRewardTopic", referralRewardEvent);
            log.info("Referral Reward Event published :" + referralRewardEvent);
        } else {
            UUID referralCode = UUID.randomUUID();
            ReferralRequest referral = new ReferralRequest(event.getEmail(), event.getPhoneNumber(), referralCode.toString());
            referralService.create(referral);
        }
    }
}


