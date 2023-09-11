package com.service.referral.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "referrals")
public class Referral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String phoneNumber;
    private String referralCode;

    public Referral(String email, String phoneNumber, String referralCode) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.referralCode = referralCode;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getReferralCode() {
        return referralCode;
    }
}
