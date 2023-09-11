package com.service.referral.services;

import com.service.referral.builder.TestReferralBuilder;
import com.service.referral.dto.ReferralRequest;
import com.service.referral.entities.Referral;
import com.service.referral.repositories.ReferralRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReferralServiceTest {

    @Mock
    private ReferralRepository referralRepository;
    ReferralService referralService;
    private TestReferralBuilder testReferralBuilder;

    @BeforeEach
    void setup() {
        testReferralBuilder = new TestReferralBuilder();
        referralService = new ReferralService(referralRepository);
    }

    @Test
    void shouldCreateReferral() {
        ReferralRequest referralRequest = new ReferralRequest("a@example.com","1234567898", UUID.randomUUID().toString());
        ArgumentCaptor<Referral> argumentCaptor = ArgumentCaptor.forClass(Referral.class);
        referralService.create(referralRequest);
        verify(referralRepository).save(argumentCaptor.capture());
        Referral actualReferral = argumentCaptor.getValue();
        Assertions.assertEquals(referralRequest.referralCode(), actualReferral.getReferralCode());
    }
}
