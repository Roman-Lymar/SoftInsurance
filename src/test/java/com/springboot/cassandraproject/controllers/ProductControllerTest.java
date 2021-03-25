package com.springboot.cassandraproject.controllers;

import com.datastax.oss.driver.shaded.guava.common.collect.Lists;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.cassandraproject.domains.Product;
import com.springboot.cassandraproject.exceptions.ErrorMessages;
import com.springboot.cassandraproject.exceptions.ResourceNotFoundException;
import com.springboot.cassandraproject.services.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc

class ProductControllerTest {

    private static final String URL_STRING = "/api/v0/catalog/products";
    private static final String URL_STRING_WITH_ID = "/api/v0/catalog/products/{id}";

    private static final Product product;
    private static final Product product2;

    static {
        product = new Product();
        product.setId(UUID.fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9"));
        product.setName("product1");
        product.setDescription("description1");
        product.setPrice(new BigDecimal(34));

        product2 = new Product();
        product2.setId(UUID.fromString("ccad1469-01ac-44f9-9b05-e01f32243cb7"));
        product2.setName("product2");
        product2.setDescription("description2");
        product2.setPrice(new BigDecimal(34));
    }

    @MockBean
    private ProductServiceImpl service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /products")
    void testGetProducts() throws Exception {

        doReturn(Lists.newArrayList(product, product2)).when(service).getAllProducts();

        mockMvc.perform(get(URL_STRING))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].id", is(UUID
                                            .fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9").toString())))
                .andExpect(jsonPath("$[0].name", is("product1")))
                .andExpect(jsonPath("$[0].description", is("description1")))
                .andExpect(jsonPath("$[0].price", is(34)))

                .andExpect(jsonPath("$[1].id", is(UUID
                                            .fromString("ccad1469-01ac-44f9-9b05-e01f32243cb7").toString())))
                .andExpect(jsonPath("$[1].name", is("product2")))
                .andExpect(jsonPath("$[1].description", is("description2")))
                .andExpect(jsonPath("$[1].price", is(34)));
    }

    @Test
    @DisplayName("GET /products?filter = name success")
    void testGetProductsByName() throws Exception {
        doReturn(Lists.newArrayList(product, product2)).when(service).getProductsBySearchString("pro");

        mockMvc.perform(get(URL_STRING + "?filter=pro"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].id", is(UUID
                        .fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9").toString())))
                .andExpect(jsonPath("$[0].name", is("product1")))
                .andExpect(jsonPath("$[0].description", is("description1")))
                .andExpect(jsonPath("$[0].price", is(34)))

                .andExpect(jsonPath("$[1].id", is(UUID
                        .fromString("ccad1469-01ac-44f9-9b05-e01f32243cb7").toString())))
                .andExpect(jsonPath("$[1].name", is("product2")))
                .andExpect(jsonPath("$[1].description", is("description2")))
                .andExpect(jsonPath("$[1].price", is(34)));
    }

    @Test
    @DisplayName("GET /products?filter=name - Not Found")
    void testGetProductByNameNotFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException(ErrorMessages
                .NO_RESOURCE_FOUND_BY_NAME.getErrorMessage())).when(service).getProductsBySearchString("pro");

        mockMvc.perform(get(URL_STRING + "?filter=pro"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /products/id")
    void testGetProductById() throws Exception {
        doReturn(Optional.of(product)).when(service).getProductById(UUID.fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9"));

        mockMvc.perform(get(URL_STRING_WITH_ID , UUID.fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(UUID
                                            .fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9").toString())))
                .andExpect(jsonPath("$.name", is("product1")))
                .andExpect(jsonPath("$.description", is("description1")))
                .andExpect(jsonPath("$.price", is(34)));
    }

    @Test
    @DisplayName("GET /products/id - Not Found")
    void testGetProductByIdNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();
        doThrow(new ResourceNotFoundException(ErrorMessages.NO_RESOURCE_FOUND.getErrorMessage())).when(service).getProductById(uuid);

        mockMvc.perform(get(URL_STRING_WITH_ID, uuid))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST /products")
    void testCreateProduct() throws Exception {
        Product productToPost = new Product();
        productToPost.setName("name");
        productToPost.setDescription("description");
        productToPost.setPrice(new BigDecimal(12));

        Product productToReturn = new Product();
        productToReturn.setId(productToPost.getId());
        productToReturn.setName("name");
        productToReturn.setDescription("description");
        productToReturn.setPrice(new BigDecimal(12));

        doReturn(productToPost).when(service).saveProduct(any());

        mockMvc.perform(post(URL_STRING)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productToReturn)))

                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(productToPost.getId().toString())))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.description", is("description")))
                .andExpect(jsonPath("$.price", is(12)));
    }

    @Test
    @DisplayName("PUT /products/id")
    void testUpdateProduct() throws Exception {
        Product productToPut = new Product();
        productToPut.setId(null);
        productToPut.setName("name");
        productToPut.setDescription("description");
        productToPut.setPrice(new BigDecimal(13));

        Product productToReturnFindBy = new Product();
        productToReturnFindBy.setId(UUID.fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9"));
        productToReturnFindBy.setName("name222");
        productToReturnFindBy.setDescription("description222");
        productToReturnFindBy.setPrice(new BigDecimal(12));

        Product productToReturnSave = new Product();
        productToReturnSave.setId(UUID.fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9"));
        productToReturnSave.setName("name");
        productToReturnSave.setDescription("description");
        productToReturnSave.setPrice(new BigDecimal(13));

        doReturn(Optional.of(productToReturnFindBy)).when(service)
                            .getProductById(UUID.fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9"));
        doReturn(productToReturnSave).when(service).saveProduct(any());

        mockMvc.perform(put(URL_STRING_WITH_ID, UUID.fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productToPut)))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id", is(UUID
                                            .fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9").toString())))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.description", is("description")))
                .andExpect(jsonPath("$.price", is(13)));
    }

    @Test
    @DisplayName("PUT /products/id - Not Found")
    void testUpdateProductNotFound() throws Exception {
        Product productToPut = new Product();
        productToPut.setId(null);
        productToPut.setName("name");
        productToPut.setDescription("description");
        productToPut.setPrice(new BigDecimal(13));

        doThrow(new ResourceNotFoundException(ErrorMessages.NO_RESOURCE_FOUND.getErrorMessage())).when(service)
                .getProductById(UUID.fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9"));

        mockMvc.perform(put(URL_STRING_WITH_ID, UUID.fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productToPut)))

                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH /products/id")
    void testPartUpdateProduct() throws Exception {
        Product productToPatch = new Product();
        productToPatch.setId(null);
        productToPatch.setName("name");
        productToPatch.setDescription("description");

        Product productToReturnFindBy = new Product();
        productToReturnFindBy.setId(UUID.fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9"));
        productToReturnFindBy.setName("name222");
        productToReturnFindBy.setDescription("description222");
        productToReturnFindBy.setPrice(new BigDecimal(13));

        Product productToReturnSave = new Product();
        productToReturnSave.setId(UUID.fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9"));
        productToReturnSave.setName("name");
        productToReturnSave.setDescription("description");
        productToReturnSave.setPrice(new BigDecimal(13));

        doReturn(Optional.of(productToReturnFindBy)).when(service)
                .getProductById(UUID.fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9"));
        doReturn(productToReturnSave).when(service).saveProduct(any());

        mockMvc.perform(patch(URL_STRING_WITH_ID, UUID.fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productToPatch)))

                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PATCH /products/id - Not Found")
    void testPartUpdateProductNotFound() throws Exception {
        Product productToPatch = new Product();
        productToPatch.setId(null);
        productToPatch.setName("name");
        productToPatch.setDescription("description");

        doThrow(new ResourceNotFoundException(ErrorMessages.NO_RESOURCE_FOUND.getErrorMessage())).when(service)
                .getProductById(UUID.fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9"));

        mockMvc.perform(patch(URL_STRING_WITH_ID, UUID.fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productToPatch)))

                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /products/id success")
    void testDeleteProduct() throws Exception {

        doReturn(Optional.of(product)).when(service).getProductById(UUID.fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9"));
        mockMvc.perform(delete(URL_STRING_WITH_ID, UUID.fromString("ccad1469-01ac-44f9-9b05-e01f32243cb9")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /products/cost")
    void testPriceOfProducts() throws Exception {
        Set<UUID> uuids = new HashSet<>();
        uuids.add(product.getId());
        uuids.add(product2.getId());

        BigDecimal sum = BigDecimal.ZERO;
        sum = sum.add(product2.getPrice());
        sum = sum.add(product.getPrice());

        doReturn(sum).when(service).getProductsCostByIds(uuids);

        mockMvc.perform(get(URL_STRING + "/cost?ids=ccad1469-01ac-44f9-9b05-e01f32243cb9," +
                                                    "ccad1469-01ac-44f9-9b05-e01f32243cb7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(68)));
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
