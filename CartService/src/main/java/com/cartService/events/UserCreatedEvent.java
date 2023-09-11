package com.cartService.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class UserCreatedEvent {

    private Long id;
    private String preference;
    private String email;
    private String phoneNumber;
    private String referredBy;
}
