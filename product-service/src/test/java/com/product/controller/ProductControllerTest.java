package com.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;
import com.product.exception.ProductNotFound;
import com.product.services.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @MockBean
    ProductService productServiceMock;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Captor
    ArgumentCaptor<ProductRequest> argumentCaptor;

    @Test
    void shouldCreateAProduct() throws Exception {
        ProductRequest expectedProductRequest = new ProductRequest(1L, "Apple", "description", 80, "category", 9);
        String jsonProductRequest = objectMapper.writeValueAsString(expectedProductRequest);
        mockMvc.perform(post("/product/add").contentType(MediaType.APPLICATION_JSON).content(jsonProductRequest)).andExpect(status().isCreated());
        verify(productServiceMock).create(argumentCaptor.capture());
        ProductRequest actualProductRequest = argumentCaptor.getValue();
        assertEquals(expectedProductRequest.name(), actualProductRequest.name());
    }

    @Test
    void shouldNotCreateAnotherProduct() throws Exception {
        ProductRequest expectedProductRequest = new ProductRequest(1L, " ", "description", 80, "category", 9);
        String jsonProductRequest = objectMapper.writeValueAsString(expectedProductRequest);
        mockMvc.perform(post("/product/add").contentType(MediaType.APPLICATION_JSON).content(jsonProductRequest)).andExpect(status().isBadRequest());
        verify(productServiceMock, never()).create(any(ProductRequest.class));
    }

    @Test
    void shouldUpdateAppleToOrange() throws Exception {
        ProductRequest expectedProductRequest = new ProductRequest(1L, "Apple", "description", 80, "category", 9);
        String jsonProductRequest = objectMapper.writeValueAsString(expectedProductRequest);
        mockMvc.perform(put("/product/update").contentType(MediaType.APPLICATION_JSON).content(jsonProductRequest)).andExpect(status().isOk());
        verify(productServiceMock).update(argumentCaptor.capture());
        ProductRequest actualProductRequest = argumentCaptor.getValue();
        assertEquals(expectedProductRequest.name(), actualProductRequest.name());
    }

    @Test
    void shouldNotUpdateAppleToOrange() throws Exception {
        ProductRequest updatedProductRequest = new ProductRequest(1L, "Apple", "description", 80, "category", 9);
        String jsonProductRequest = objectMapper.writeValueAsString(updatedProductRequest);
        doThrow(new ProductNotFound()).when(productServiceMock).update(updatedProductRequest);
        mockMvc.perform(put("/product/update").contentType(MediaType.APPLICATION_JSON).content(jsonProductRequest)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteOrange() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/product/delete/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(productServiceMock).delete(id);
    }

    @Test
    void shouldNotDeleteOrange() throws Exception {
        ProductRequest productDeleteRequest = new ProductRequest(1L, "Apple", "description", 80, "category", 9);
        doThrow(new ProductNotFound()).when(productServiceMock)
                .delete(productDeleteRequest.id());
        mockMvc.perform(delete("/product/delete/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                .isBadRequest());
    }

    @Test
    void shouldGetAProductById() throws Exception {
        Long id = 1L;
        mockMvc.perform(get("/product/get/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(productServiceMock).get(id);

    }

    @Test
    void shouldThrowProductNotFoundException() throws Exception {
        Long id = 1L;
        doThrow(new ProductNotFound()).when(productServiceMock).get(id);
        mockMvc.perform(get("/product/get/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(productServiceMock).get(id);
    }

    @Test
    void shouldGiveAllProductsFromAvailableProducts() throws Exception {

        List<ProductResponse> productList = new ArrayList<>();
        productList.add(new ProductResponse(1L,"Product 1", "Description 1", 100,"category1",2));
        productList.add(new ProductResponse(2L,"Product 2", "Description 2", 200,"category2",4));

        when(productServiceMock.getProducts()).thenReturn(productList);

        mockMvc.perform(get("/product/getProducts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[1].name").value("Product 2"));

        verify(productServiceMock).getProducts();
    }

    @Test
    void shouldGiveProductsWhenProductIdsProvided() throws Exception {
        List<ProductResponse> productList = new ArrayList<>();
        productList.add(new ProductResponse(1L,"Product 1", "Description 1", 100,"category1",2));
        productList.add(new ProductResponse(2L,"Product 2", "Description 2", 200,"category2",4));

        List<Long> productIdsList = List.of(1L,2L);
        String productIdsString = productIdsList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        when(productServiceMock.getProductsByIds(productIdsList)).thenReturn(productList);

        mockMvc.perform(get("/product/getProducts/{productIdList}",productIdsString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[1].name").value("Product 2"));
    }
}
