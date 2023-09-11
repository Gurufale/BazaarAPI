package com.product.services;

import com.product.builder.TestProductBuilder;
import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;
import com.product.entities.Product;
import com.product.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    ProductService productService;
    private TestProductBuilder testProductBuilder;

    @BeforeEach
    void setup() {
        testProductBuilder = new TestProductBuilder();
        productService = new ProductService(productRepository);
    }

    @Test
    void shouldSaveToRepository() {
        ProductRequest productRequest = new ProductRequest(1L, "Apple", "description", 80, "category", 9);
        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        productService.create(productRequest);
        verify(productRepository).save(argumentCaptor.capture());
        Product actualProduct = argumentCaptor.getValue();
        Assertions.assertEquals(productRequest.name(), actualProduct.getName());
        Assertions.assertEquals(productRequest.description(), actualProduct.getDescription());
    }

    @Test
    void shouldAnotherSaveToRepository() {
        ProductRequest productRequest = new ProductRequest(1L, "Apple", "description", 80, "category", 9);
        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        productService.create(productRequest);
        verify(productRepository).save(argumentCaptor.capture());
        Product actualProduct = argumentCaptor.getValue();
        Assertions.assertEquals(productRequest.name(), actualProduct.getName());
        Assertions.assertEquals(productRequest.description(), actualProduct.getDescription());
    }

    @Test
    void shouldUpdateAppleToOrange() {
        ProductRequest productRequest = new ProductRequest(1L, "Apple", "description", 80, "category", 9);
        Product expectionProduct = testProductBuilder.withName("Orange").withDescription("description").withCategory("category").withId(1L).withPrice(80.0).withQuantity(9).build();
        when(productRepository.findById(productRequest.id())).thenReturn(Optional.ofNullable(expectionProduct));
        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        productService.update(productRequest);
        verify(productRepository).findById(productRequest.id());
        verify(productRepository).save(argumentCaptor.capture());
        Product actualProduct = argumentCaptor.getValue();
        Assertions.assertEquals(productRequest.name(), actualProduct.getName());
    }

    @Test
    void shouldDeleteGivenProduct() {
        ProductRequest productRequest = new ProductRequest(1L, "Apple", "description", 80, "category", 9);
        Product expectionProduct = testProductBuilder.withId(1L).withName("Orange").withDescription("description").withCategory("category").withId(1L).withPrice(80.0).withQuantity(9).build();
        when(productRepository.findById(expectionProduct.getId())).thenReturn(Optional.ofNullable(expectionProduct));
        productService.delete(expectionProduct.getId());
        verify(productRepository).findById(expectionProduct.getId());
    }

    @Test
    void shouldGiveProductforProvidedId() {
        Long id = 1L;
        ProductRequest productRequest = new ProductRequest(id, "Apple", "description", 80, "category", 9);
        Product expectionProduct = testProductBuilder.withName(productRequest.name()).withDescription(productRequest.description()).withCategory(productRequest.category()).withId(productRequest.id()).withPrice(productRequest.price()).withQuantity(productRequest.quantity()).build();
        when(productRepository.findById(productRequest.id())).thenReturn(Optional.ofNullable(expectionProduct));
        ProductResponse productResponse = productService.get(id);
        verify(productRepository).findById(productRequest.id());
        Assertions.assertEquals(expectionProduct.getName(),productResponse.name());
    }

    @Test
    void shouldGiveAllProductsFromAvailableProducts()
    {
        Product expectedProduct1 = testProductBuilder.withName("Orange").withDescription("description").withCategory("category").withId(1L).withPrice(80.0).withQuantity(9).build();
        Product expectedProduct2 = testProductBuilder.withName("Apple").withDescription("description").withCategory("category").withId(2L).withPrice(80.0).withQuantity(9).build();

        List<Product> products = List.of(expectedProduct1,expectedProduct2);
        when(productRepository.findAll()).thenReturn(products);

        List<ProductResponse> actualProducts = productService.getProducts();

        verify(productRepository).findAll();
        Assertions.assertEquals(products.size(),actualProducts.size());

    }

    @Test
    void shouldGiveAllProductsFromAvailableProvidedProductListIds()
    {
        Product expectedProduct1 = testProductBuilder.withName("Orange").withDescription("description").withCategory("category").withId(1L).withPrice(80.0).withQuantity(9).build();
        Product expectedProduct2 = testProductBuilder.withName("Apple").withDescription("description").withCategory("category").withId(2L).withPrice(80.0).withQuantity(9).build();

        List<Long> productListIds = List.of(1L,2L);
        List<Product> products = List.of(expectedProduct1,expectedProduct2);
        when(productRepository.findAllById(productListIds)).thenReturn(products);

        List<ProductResponse> actualProducts = productService.getProductsByIds(productListIds);

        verify(productRepository).findAllById(productListIds);
        Assertions.assertEquals(products.size(),actualProducts.size());

    }
}
