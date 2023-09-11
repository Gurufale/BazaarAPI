package com.user.events;

import lombok.*;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class UserCreatedEvent {

    private long id;
    private String preference;
    private String email;
    private String phoneNumber;
    private String referredBy;
}
