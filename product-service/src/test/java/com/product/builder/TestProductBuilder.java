package com.product.builder;

import com.product.entities.Product;
import jakarta.validation.constraints.NotBlank;

public class TestProductBuilder {

    private Long id;
    @NotBlank
    private String name;
    private String description;
    private double price;
    private String category;
    private int quantity;

    public TestProductBuilder withId(Long productId) {
        this.id = productId;
        return this;
    }

    public TestProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TestProductBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TestProductBuilder withCategory(String category) {
        this.category = category;
        return this;
    }

    public TestProductBuilder withPrice(double price) {
        this.price = price;
        return this;
    }

    public TestProductBuilder withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Product build() {
        return new Product(id, name, description, price, category, quantity);
    }

}
