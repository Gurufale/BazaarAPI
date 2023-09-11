package com.service.referral.builder;

import com.service.referral.entities.Referral;

public class TestReferralBuilder {

    private Long id;
    private String email;
    private String phoneNumber;
    private String referralCode;

    public TestReferralBuilder withId(Long referralId) {
        this.id = referralId;
        return this;
    }

    public TestReferralBuilder withEmail(String email)
    {
        this.email = email;
        return this;
    }

    public TestReferralBuilder withPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public TestReferralBuilder withReferralCode(String referralCode)
    {
        this.referralCode = referralCode;
        return this;
    }

    public Referral build()
    {
        return new Referral(email,phoneNumber,referralCode);
    }

}
