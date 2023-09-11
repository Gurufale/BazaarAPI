package com.service.referral.repositories;

import com.service.referral.entities.Referral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralRepository extends JpaRepository<Referral,Long> {
}
