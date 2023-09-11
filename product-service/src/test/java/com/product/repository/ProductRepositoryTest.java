package com.product.repository;

import com.product.builder.TestProductBuilder;
import com.product.entities.Product;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    private TestProductBuilder testProductBuilder;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        testProductBuilder = new TestProductBuilder();

        Product apple = testProductBuilder.withName("Apple").build();
        entityManager.persist(apple);
        entityManager.flush();
        Product orange = testProductBuilder.withName("Orange").build();
        entityManager.persist(orange);
        entityManager.flush();
        Product mango = testProductBuilder.withName("Mango").build();
        entityManager.persist(mango);
        entityManager.flush();
    }

    @Test
    void ShouldSaveProduct() {
        Product expectedProduct = testProductBuilder.withName("Apple").withQuantity(9).withPrice(80.0).withCategory("category").withDescription("description").build();
        Product actualProduct = repository.save(expectedProduct);
        Assertions.assertEquals(expectedProduct, actualProduct);
    }
}
