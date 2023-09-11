package com.service.referral.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.referral.dto.ReferralRequest;
import com.service.referral.services.ReferralService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReferralController.class)
public class ReferralControllerTest {
    @MockBean
    ReferralService referralServiceMock;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Captor
    ArgumentCaptor<ReferralRequest> argumentCaptor;

    @Test
    void shouldCreateReferralForGivenDetails() throws Exception {
        ReferralRequest expectedReferraLRequest = new ReferralRequest("a@example.com","1234567898", UUID.randomUUID().toString());
        String jsonReferralRequest = objectMapper.writeValueAsString(expectedReferraLRequest);
        mockMvc.perform(post("/referral/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReferralRequest))
                .andExpect(status().isCreated());
        verify(referralServiceMock).create(argumentCaptor.capture());
        ReferralRequest actualReferralRequest = argumentCaptor.getValue();
        assertEquals(expectedReferraLRequest.referralCode(), actualReferralRequest.referralCode());
    }
}
