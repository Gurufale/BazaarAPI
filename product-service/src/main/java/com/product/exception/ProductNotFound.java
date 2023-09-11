package com.product.exception;

public class ProductNotFound extends RuntimeException {

    public ProductNotFound() {
        super();
    }

    public ProductNotFound(String message) {
        super(message);
    }
}
