package com.pasha.springboot.cassandraproject.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static org.hamcrest.Matchers.equalTo;


public class ProductControllerRestAssuredTest {


    @BeforeAll
    public static void setup() {
        port = 3001;
    /*    CassandraTestConnector cassandraTestConnector = new CassandraTestConnector();
        cassandraTestConnector("testKeySpace", "SimpleStrategy", 1);*/
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
    public void verifyGetProductById() {

        given().when().get("/api/v0/catalog/products/9eaa0386-1fda-4f82-abe2-79f3857d1af5").then()
                .body("name", equalTo("Pr 1"))
                .body("description", equalTo("Head"))
                .body("price", equalTo(10.0f))
                .statusCode(200);
    }
}
