package com.pasha.springboot.cassandraproject.controllers;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;


public class PackageControllerRestAssuredTest {


    @LocalServerPort
    private int port;

    @Before
    public void setUp() throws Exception {
        RestAssured.port = Integer.valueOf(3001);
    }

    @Test
    public void getAllProductsOrFilterByMatch_test() {
        given().when().get("/api/v0/catalog/products/").then().statusCode(200);
    }

    @Test
    public void findProductById_test() {
        given().when().get("/api/v0/catalog/products/9eaa0386-1fda-4f82-abe2-79f3857d1af5").then().statusCode(200);
    }

    @Test
    public void findProductByWrongId_test() {
        given().when().get("/api/v0/catalog/products/9eaa0386-1fda-4f82-abe2-79f3857d1aff").then().statusCode(404);
    }

    @Test
    public void verifyGetPackageById() {
        given().when().get("/api/v0/catalog/packages/base/90f69f9b-7b1a-42b8-9dd7-6838a3e7c6f3").then().
                body("name", equalTo("Base"))
                .body("description", equalTo("Basic - you can choose better package"))
                .body("price", equalTo(25.0f))
                .body("createdTime", equalTo("2021-03-19T19:48:08.036"))
                .body("productIds", hasItems("9eaa0386-1fda-4f82-abe2-79f3857d1af5",
                        "e2a8bd64-365f-4583-b94d-d98697e8a923"))
                .statusCode(200);
    }
}
