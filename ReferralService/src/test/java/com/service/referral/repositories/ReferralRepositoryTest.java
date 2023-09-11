package com.service.referral.repositories;

import com.service.referral.builder.TestReferralBuilder;
import com.service.referral.entities.Referral;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ReferralRepositoryTest {

    @Autowired
    private ReferralRepository repository;

    private TestReferralBuilder testReferralBuilder;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        testReferralBuilder = new TestReferralBuilder();

        Referral referral1 = testReferralBuilder.withEmail("a@example.com").build();
        entityManager.persist(referral1);
        entityManager.flush();
        Referral referral2 = testReferralBuilder.withEmail("b@example.com").build();
        entityManager.persist(referral2);
        entityManager.flush();
        Referral referral3 = testReferralBuilder.withEmail("c@example.com").build();
        entityManager.persist(referral3);
        entityManager.flush();
    }

    @Test
    void ShouldCreateReferralforGivenDetails() {
        Referral referral = testReferralBuilder.withEmail("a@example.com").withId(0L).withPhoneNumber("23456").withReferralCode(UUID.randomUUID().toString()).build();
        Referral actualReferral = repository.save(referral);
        Assertions.assertEquals(referral, actualReferral);
    }
}
