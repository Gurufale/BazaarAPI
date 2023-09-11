package com.user.events;

import com.user.entities.User;
import com.user.entities.Wallet;
import com.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReferralRewardEventConsumerTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ReferralRewardEventConsumer referralRewardEventConsumer;

    @Test
    void shouldConsumeReferralRewardEventWhenRewardAdded()
    {
        ReferralRewardEvent referralRewardEvent = new ReferralRewardEvent("a@example.com", "12345678", 10.00);
        User user = new User(referralRewardEvent.getEmail(), referralRewardEvent.getPhoneNumber());

        Wallet wallet = Wallet.builder().balance(100).build();
        user.setWallet(wallet);

        when(userRepository.findByEmail(referralRewardEvent.getEmail())).thenReturn(Optional.ofNullable(user));

        referralRewardEventConsumer.consume(referralRewardEvent);

        Assertions.assertEquals(110,user.getWallet().getBalance());
    }
}
