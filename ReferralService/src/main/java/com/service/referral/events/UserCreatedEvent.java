package com.service.referral.events;

import lombok.*;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class UserCreatedEvent {

    private Long userId;
    private String preference;
    private String email;
    private String phoneNumber;
    private String referredBy;
}
