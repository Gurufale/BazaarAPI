package com.service.referral.services;

import com.service.referral.dto.ReferralRequest;
import com.service.referral.entities.Referral;
import com.service.referral.repositories.ReferralRepository;
import org.springframework.stereotype.Service;

@Service
public class ReferralService {
    private final ReferralRepository referralRepository;

    public ReferralService(ReferralRepository referralRepository) {
        this.referralRepository = referralRepository;
    }

    public void create(ReferralRequest referralRequest) {

        Referral referral = new Referral(referralRequest.email(), referralRequest.phoneNumber(), referralRequest.referralCode());
        referralRepository.save(referral);
    }
}
