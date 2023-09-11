package com.cartService.events;

import com.cartService.dto.CartRequest;
import com.cartService.entities.CartItem;
import com.cartService.services.CartService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserCreatedEventConsumerTest {

    private UserCreatedEventConsumer userCreatedEventConsumer;

    @Mock
    private CartService cartService;

    @BeforeEach
    void setUp()
    {
        userCreatedEventConsumer = new UserCreatedEventConsumer(cartService);
    }

    @Test
    void shouldConsumeUserCreatedEvent() {
        UserCreatedEvent data = new UserCreatedEvent(1L,"email", "a@example.com", "9875645367",null);
        userCreatedEventConsumer.consume(data);
        Assertions.assertEquals("9875645367", data.getPhoneNumber());
    }

    @Test
    void shouldCreateCartWhenUserCreatedEventConsumed()
    {
        List<CartItem> cartItemList = new ArrayList<>();
        CartRequest cartRequest = new CartRequest(1L,cartItemList);
        ArgumentCaptor<CartRequest> cartRequestArgumentCaptor = ArgumentCaptor.forClass(CartRequest.class);

        UserCreatedEvent data = new UserCreatedEvent(1L,"email", "a@example.com", "9875645367",null);
        userCreatedEventConsumer.consume(data);

        verify(cartService).create(cartRequestArgumentCaptor.capture());
        Assertions.assertEquals("9875645367", data.getPhoneNumber());

        CartRequest actualCartRequest = cartRequestArgumentCaptor.getValue();
        Assertions.assertEquals(cartRequest.userId(),actualCartRequest.userId());
    }
}
