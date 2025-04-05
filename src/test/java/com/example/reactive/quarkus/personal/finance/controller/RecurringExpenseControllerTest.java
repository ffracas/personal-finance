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
                .pathParam("recurringExpenseId", 1)
                .when()
                .get("/recurring-expense/getRecurringExpenseById/{recurringExpenseId}")
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
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
        String requestBody = """
                {
                  "amount": 100.00,
                  "category": "Utilities",
                  "frequency": "Monthly",
                  "startDate": "2025-04-01",
                  "endDate": "2025-12-31"
                }
                """;
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/recurring-expense/createRecurringExpense")
                .then().log().all()
                .statusCode(201)
                .body("name", equalTo("New User"))
                .body("email", equalTo("thebestemail@bigmail.com"));
    }


    @Test
    public void testUpdateRecurringExpense() {
        // First, create a user to update.
        String requestBody = """
                {
                  "amount": 100.00,
                  "category": "Utilities",
                  "frequency": "Monthly",
                  "startDate": "2025-04-01",
                  "endDate": "2025-12-31"
                }
                """;

        int userId = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/recurring-expense/createRecurringExpense")
                .then()
                .statusCode(201)
                .extract().path("id");

        // Now update the user with a new name.
        String updateRequest = """
                {
                  "amount": 100.00,
                  "category": "Utilities",
                  "frequency": "Monthly",
                  "startDate": "2025-04-01",
                  "endDate": "2025-12-31"
                }
                """;
        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put("/recurring-expense/updateRecurringExpense")
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated User"))
                .body("id", equalTo(userId));
    }
}
