package com.service.referral.events;

import lombok.*;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class ReferralRewardEvent {

    String email;
    String phoneNumber;
    double balance;

}
