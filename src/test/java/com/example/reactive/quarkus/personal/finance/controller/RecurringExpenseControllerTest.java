package com.example.reactive.quarkus.personal.finance.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
public class RecurringExpenseControllerTest {
    @Test
    public void testGetRecurringExpenseId() {
        given()
                .pathParam("recurringExpenseId", "5f3b5b3e-4f87-442b-b0cd-37c58a10548e")
                .when()
                .get("/recurring-expense/getRecurringExpenseById/{recurringExpenseId}")
                .then()
                .statusCode(200)
                .body("recurringExpenseId", equalTo("5f3b5b3e-4f87-442b-b0cd-37c58a10548e"));
    }

    @Test
    public void testGetRecurringExpenseIdNotFound() {
        given()
                .pathParam("recurringExpenseId", "1658cc09-ef5d-4f5b-8fe8-d9f71bfbfbec")
                .when()
                .get("/recurring-expense/getRecurringExpenseById/{recurringExpenseId}")
                .then()
                .statusCode(404);
    }


    @Test
    public void testGetAllRecurringExpense() {
        given()
                .when()
                .get("/recurring-expense/getAllRecurringExpense")
                .then()
                .statusCode(200)
                // Assumes that at least one user exists in the system.
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    public void testCreateRecurringExpense() {
        String requestBody = "{\"userId\" : \"1658cc09-ef5d-4f5b-8fe8-d9f8abfbfbec\",\"amount\": \"100.00\",\"category\": \"Utilities\",\"frequency\": \"Monthly\"," +
                "\"startDate\": \"2025-04-01\",\"endDate\": \"2025-12-31\"}";
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/recurring-expense/createRecurringExpense")
                .then().log().all()
                .statusCode(201)
                .body("frequency", equalTo("Monthly"))
                .body("amount", equalTo(100.0F));
    }

    @Test
    public void testCreateRecurringExpenseInternalServerError() {
        String requestBody = "{\"userId\" : \"111\",\"amount\": \"100.00\",\"category\": \"Utilities\",\"frequency\": \"Monthly\"," +
                "\"startDate\": \"2025-04-01\",\"endDate\": \"2025-12-31\"}";
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/recurring-expense/createRecurringExpense")
                .then().log().all()
                .statusCode(500);
    }


    @Test
    public void testUpdateRecurringExpense() {
        // First, create a user to update.
        String requestBody = "{\"userId\" : \"1658cc09-ef5d-4f5b-8fe8-d9f8abfbfbec\",\"amount\": \"100.00\",\"category\": \"Utilities\",\"frequency\": \"Monthly\"," +
                "\"startDate\": \"2025-04-01\",\"endDate\": \"2025-12-31\"}";

        String recurringExpenseId = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/recurring-expense/createRecurringExpense")
                .then()
                .statusCode(201)
                .extract().path("recurringExpenseId");

        // Now update the user with a new name.
        String updateRequest = "{\"userId\" : \"1658cc09-ef5d-4f5b-8fe8-d9f8abfbfbec\",\"amount\": \"1300.00\",\"category\": \"Utilities\",\"frequency\": \"Monthly\"," +
                "\"startDate\": \"2025-04-01\",\"endDate\": \"2027-12-31\"}";
        ;
        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .pathParam("recurringExpenseId", recurringExpenseId)
                .when()
                .put("/recurring-expense/updateRecurringExpense/{recurringExpenseId}")
                .then()
                .statusCode(200)
                .body("userId", equalTo("1658cc09-ef5d-4f5b-8fe8-d9f8abfbfbec"))
                .body("recurringExpenseId", equalTo(recurringExpenseId));
    }

    @Test
    public void testDeleteRecurringExpenseById() {
        given()
                .pathParam("recurringExpenseId", "9c0a7723-8d92-471b-a0f3-d6b0c31ea510")
                .when()
                .delete("/recurring-expense/deleteRecurringExpense/{recurringExpenseId}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testDeleteRecurringExpenseByIdNotFound() {
        given()
                .pathParam("recurringExpenseId", "660e8400-e29b-41d4-a716-552255440112")
                .when()
                .delete("/recurring-expense/deleteRecurringExpense/{recurringExpenseId}")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteRecurringExpenseByIdInternalServerError() {
        given()
                .pathParam("recurringExpenseId", 11122)
                .when()
                .delete("/recurring-expense/deleteRecurringExpense/{recurringExpenseId}")
                .then()
                .statusCode(500);
    }
}
