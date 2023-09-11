package com.user.repository;

import com.user.builder.TestUserBuilder;
import com.user.entities.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    private TestUserBuilder testUserBuilder;
    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void setUp()
    {
        testUserBuilder = new TestUserBuilder();

        User user1 = testUserBuilder.withEmail("a@example.com").build();
        entityManager.persist(user1);
        entityManager.flush();

        User user2 = testUserBuilder.withEmail("b@example.com").build();
        entityManager.persist(user2);
        entityManager.flush();

        User user3 = testUserBuilder.withEmail("c@example.com").build();
        entityManager.persist(user3);
        entityManager.flush();
    }

    @Test
    public void shouldSaveUser()
    {
       User user = testUserBuilder.withEmail("d@example.com").withNumber("1234567890")
               .withPreference("email")
               .build();

        User actualUser = userRepository.save(user);
        assertEquals(actualUser,user);
    }
}
