package com.user.events;

import com.user.entities.User;
import com.user.entities.Wallet;
import com.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class ReferralRewardEventConsumer {

    @Autowired
    private final UserRepository userRepository;

    public ReferralRewardEventConsumer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = "ReferralRewardTopic", groupId = "ReferralRewardNotification")
    public void consume(ReferralRewardEvent referralRewardEvent) {
        log.info("Received Reward Notification :" + referralRewardEvent);
        Optional<User> userOptional = userRepository.findByEmail(referralRewardEvent.getEmail());
        if (userOptional.isPresent()) {

            User user = userOptional.get();
            Wallet wallet = user.getWallet();

            int currentBalance = wallet.getBalance();
            int newBalance = (int) (currentBalance + referralRewardEvent.getBalance());

            wallet.setBalance(newBalance);
            wallet.setNote("Referral Reward");
            userRepository.save(user);
        }
    }
}
