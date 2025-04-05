package com.example.reactive.quarkus.personal.finance.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
public class InvestmentControllerTest {
    @Test
    public void testGetInvestmentId() {
        given()
                .pathParam("investmentId", "b2c3d4e5-f678-9012-abcd-2345678901bc")
                .when()
                .get("/investment/getInvestmentById/{investmentId}")
                .then()
                .statusCode(200)
                .body("investmentId", equalTo("b2c3d4e5-f678-9012-abcd-2345678901bc"));
    }

    @Test
    public void testGetInvestmentIdNotFound() {
        given()
                .pathParam("investmentId", "1658cc09-ef5d-4f5b-8fe8-d9f71bfbfbec")
                .when()
                .get("/investment/getInvestmentById/{investmentId}")
                .then()
                .statusCode(404);
    }


    @Test
    public void testGetAllInvestment() {
        given()
                .when()
                .get("/investment/getAllInvestment")
                .then()
                .statusCode(200)
                // Assumes that at least one user exists in the system.
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    public void testCreateInvestment() {
        String requestBody = "{\"userId\": \"1658cc09-ef5d-4f5b-8fe8-d9f8abfbfbec\",\"name\": \"Apple Inc.\", " +
                "\"code\": \"AAPL\",  \"sharedOwned\": \"50.0000\", \"unitPrice\": \"150.2500\", " +
                "\"investmentDate\": \"2024-01-15\", \"currentValue\": \"7512.50\"}";
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/investment/createInvestment")
                .then().log().all()
                .statusCode(201)
                .body("name", equalTo("Apple Inc."))
                .body("sharedOwned", equalTo(50.0F));
    }

    @Test
    public void testCreateInvestmentInternalServerError() {
        String requestBody = "{\"userId\" : \"111\",\"amount\": \"100.00\",\"category\": \"Utilities\",\"frequency\": \"Monthly\"," +
                "\"startDate\": \"2025-04-01\",\"endDate\": \"2025-12-31\"}";
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/investment/createInvestment")
                .then().log().all()
                .statusCode(500);
    }


    @Test
    public void testUpdateInvestment() {
        // First, create a user to update.
        String requestBody = "{\"userId\": \"1658cc09-ef5d-4f5b-8fe8-d9f7abfbfbec\",\"name\": \"Amazon.com\", " +
                "\"code\": \"AMZN\",  \"sharedOwned\": \"150.0000\", \"unitPrice\": \"10.2500\", " +
                "\"investmentDate\": \"2024-01-15\", \"currentValue\": \"7512.50\"}";

        String investmentId = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/investment/createInvestment")
                .then()
                .statusCode(201)
                .extract().path("investmentId");

        // Now update the user with a new name.
        String updateRequest = "{\"userId\": \"1658cc09-ef5d-4f5b-8fe8-d9f7abfbfbec\",\"name\": \"Amazon.com\", " +
                "\"code\": \"AMZN\",  \"sharedOwned\": \"1150.0000\", \"unitPrice\": \"1022.2500\", " +
                "\"investmentDate\": \"2024-01-15\", \"currentValue\": \"71512.50\"}";
        ;
        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .pathParam("investmentId", investmentId)
                .when()
                .put("/investment/updateInvestment/{investmentId}")
                .then()
                .statusCode(200)
                .body("userId", equalTo("1658cc09-ef5d-4f5b-8fe8-d9f7abfbfbec"))
                .body("investmentId", equalTo(investmentId));
    }

    @Test
    public void testDeleteInvestmentById() {
        given()
                .pathParam("investmentId", "c3d4e5f6-7890-1234-abcd-3456789012cd")
                .when()
                .delete("/investment/deleteInvestment/{investmentId}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testDeleteInvestmentByIdNotFound() {
        given()
                .pathParam("investmentId", "660e8400-e29b-41d4-a716-552255440112")
                .when()
                .delete("/investment/deleteInvestmentById/{investmentId}")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteInvestmentByIdInternalServerError() {
        given()
                .pathParam("investmentId", 11122)
                .when()
                .delete("/investment/deleteInvestment/{investmentId}")
                .then()
                .statusCode(500);
    }
}
