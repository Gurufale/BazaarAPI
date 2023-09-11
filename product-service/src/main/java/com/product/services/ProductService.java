package com.product.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;
import com.product.entities.Product;
import com.product.exception.ProductNotFound;
import com.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void create(ProductRequest productRequest) {
        Product product = new Product(productRequest.id(), productRequest.name(), productRequest.description(), productRequest.price(), productRequest.category(), productRequest.quantity());
        productRepository.save(product);
    }

    public void update(ProductRequest productRequest) {
        productRepository.findById(productRequest.id()).orElseThrow(() -> new ProductNotFound());
        Product productFromRequest = new Product(productRequest.id(), productRequest.name(), productRequest.description(), productRequest.price(), productRequest.category(), productRequest.quantity());
        productRepository.save(productFromRequest);
    }

    public void delete(Long id) {
        productRepository.findById(id).orElseThrow(() -> new ProductNotFound());
        productRepository.deleteById(id);
    }

    public ProductResponse get(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFound());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(product, ProductResponse.class);
    }

    public List<ProductResponse> getProducts() {
        List<Product> productList = productRepository.findAll();
        return productList.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse convertToProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getQuantity()
        );
    }

    public List<ProductResponse> getProductsByIds(List<Long> productIdList) {
        List<Product> productList = productRepository.findAllById(productIdList);
        return productList.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
    }
}
