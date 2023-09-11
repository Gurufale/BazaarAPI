package com.product.controller;

import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;
import com.product.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    void create(@RequestBody @Validated ProductRequest productRequest) {
        productService.create(productRequest);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    void update(@RequestBody @Validated ProductRequest productRequest) {
        productService.update(productRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @GetMapping("/get/{id}")
    ResponseEntity<ProductResponse> get(@PathVariable Long id) {
        ProductResponse productResponse = productService.get(id);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/getProducts")
    public ResponseEntity<List<ProductResponse>> getProducts()
    {
        List<ProductResponse> products = productService.getProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("getProducts/{productIdList}")
    public ResponseEntity<List<ProductResponse>> getProductByIds(@PathVariable List<Long> productIdList)
    {
        List<ProductResponse> productsByIds = productService.getProductsByIds(productIdList);
        return ResponseEntity.ok(productsByIds);
    }

}
