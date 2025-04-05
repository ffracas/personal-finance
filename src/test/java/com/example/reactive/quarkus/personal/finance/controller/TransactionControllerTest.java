package com.example.reactive.quarkus.personal.finance.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
public class TransactionControllerTest {
    @Test
    public void testGetTransactionById() {
        given()
                .pathParam("transactionId", "550e8400-e29b-41d4-a716-446655440000")
                .when()
                .get("/transaction/getTransactionById/{transactionId}")
                .then()
                .statusCode(200)
                .body("transactionId", equalTo("550e8400-e29b-41d4-a716-446655440000"));
    }

    @Test
    public void testGetTransactionByIdNotFound() {
        given()
                .pathParam("transactionId", "550e8400-e29b-41d4-a716-446651440000")
                .when()
                .get("/transaction/getTransactionById/{transactionId}")
                .then()
                .statusCode(404);
    }

    /**
     * Tests that the endpoint to retrieve all users returns a non-empty collection.
     */
    @Test
    public void testGetAllTransaction() {
        given()
                .when()
                .get("/transaction/getAllTransaction")
                .then()
                .statusCode(200)
                // Assumes that at least one user exists in the system.
                .body("size()", greaterThanOrEqualTo(1));
    }

    /**
     * Tests the user creation endpoint. A new user is created with the given request data,
     * and the response is verified to contain the correct name and a generated ID.
     */
    @Test
    public void testCreateTransaction() {
        String requestBody = "{ \"userId\" : \"1658cc09-ef5d-4f5b-8fe8-d9f8abfbfbec\",\"amount\" : \"250.75\", " +
                "\"category\" : \"Utilities\", \"subCategory\" : \"Electricity\", \"type\" : \"Expense\", " +
                "\"transactionDate\" : \"2025-04-03\"}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/transaction/createTransaction")
                .then().log().all()
                .statusCode(201)
                .body("amount", equalTo(250.75F))
                .body("category", equalTo("Utilities"));
    }

    @Test
    public void testCreateTransactionInternalServerError() {
        String requestBody = "{ \"userId\" : \"111\",\"amount\" : \"250.75\", " +
                "\"category\" : \"Utilities\", \"subCategory\" : \"Electricity\", \"type\" : \"Expense\", " +
                "\"transactionDate\" : \"2025-04-03\"}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/transaction/createTransaction")
                .then().log().all()
                .statusCode(500);
    }

    @Test
    public void testUpdateTransaction() {
        // First, create a user to update.

        String requestBody = "{\"userId\" : \"1658cc09-ef5d-4f5b-8fe8-d9f7abfbfbec\", \"amount\" : \"250.75\", \"category\" : \"Utilities\",  \"subCategory\" : \"Electricity\",  \"type\" : \"Expense\",\"transactionDate\" : \"2025-04-03\"}";
        String transactionId = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/transaction/createTransaction")
                .then()
                .statusCode(201)
                .extract().path("transactionId");

        // Now update the user with a new name.
        String updateRequest = "{\"amount\" : \"250.75\",\"category\" : \"Alco\",  \"subCategory\" : \"Beer\",   \"type\" : \"Expense\", \"transactionDate\" : \"2025-04-03\"}";
        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .pathParam("transactionId", transactionId)
                .when()
                .put("/transaction/updateTransaction/{transactionId}")
                .then()
                .statusCode(200)
                .body("category", equalTo("Alco"))
                .body("subCategory", equalTo("Beer"));
    }

    @Test
    public void testDeleteTransactionById() {
        given()
                .pathParam("transactionId", "660e8400-e29b-41d4-a716-556655440112")
                .when()
                .delete("/transaction/deleteTransaction/{transactionId}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testDeleteTransactionByIdNotFound() {
        given()
                .pathParam("transactionId", "660e8400-e29b-41d4-a716-552255440112")
                .when()
                .delete("/transaction/deleteTransaction/{transactionId}")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteTransactionByIdInternalServerError() {
        given()
                .pathParam("transactionId", 11122)
                .when()
                .delete("/transaction/deleteTransaction/{transactionId}")
                .then()
                .statusCode(500);
    }
}
