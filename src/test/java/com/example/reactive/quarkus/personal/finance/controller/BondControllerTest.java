package com.example.reactive.quarkus.personal.finance.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
class BondControllerTest {

    @Test
    void testGetBondId() {
        given()
                .pathParam("bondId", "cc6df3fc-03c4-4f08-91b9-0b92d1db3344")
                .when()
                .get("/bond/getBondById/{bondId}")
                .then()
                .statusCode(200)
                .body("maturityDate", equalTo("2027-03-01"));
    }

    @Test
    void testGetBondIdNotFound() {
        given()
                .pathParam("bondId", "1658cc09-ef5d-4f5b-8fe8-d9f71bfbfbec")
                .when()
                .get("/bond/getBondById/{bondId}")
                .then()
                .statusCode(404);
    }


    @Test
    void testGetAllBond() {
        given()
                .when()
                .get("/bond/getAllBond")
                .then()
                .statusCode(200)
                // Assumes that at least one user exists in the system.
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    void testCreateBond() {
        String requestBody = "{\"userId\":\"1658cc09-ef5d-4f5b-8fe8-d9f8abfbfbec\",\"name\": \"Italian Government Bond 2040\",  " +
                "\"code\": \"ITB2030\", \"investedAmount\": 10000.00,\"annualRate\": 2.50,\"maturityDate\": \"2035-12-31\", " +
                "\"couponType\": \"Fixed\",\"currentValue\": 10250.75 }";
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/bond/createBond")
                .then().log().all()
                .statusCode(201)
                .body("name", equalTo("Italian Government Bond 2040"))
                .body("currentValue", equalTo(10250.75F));
    }

    @Test
    void testCreateBondInternalServerError() {
        String requestBody = "{\"userId\":\"11111\",\"name\": \"Italian Government Bond 2040\",  " +
                "\"code\": \"ITB2030\", \"investedAmount\": 10000.00,\"annualRate\": 2.50,\"maturityDate\": \"2035-12-31\", " +
                "\"couponType\": \"Fixed\",\"currentValue\": 10250.75 }";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/bond/createBond")
                .then().log().all()
                .statusCode(500);
    }


    @Test
    void testUpdateBond() {
        // First, create a user to update.
        String requestBody = "{\"userId\":\"1658cc09-ef5d-4f5b-8fe8-d9f7abfbfbec\",\"name\": \"Italian Government Bond 2060\",  " +
                "\"code\": \"ITB2030\", \"investedAmount\": 10000.00,\"annualRate\": 2.50,\"maturityDate\": \"2035-12-31\", " +
                "\"couponType\": \"Fixed\",\"currentValue\": 10250.75 }";

        String bondId = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/bond/createBond")
                .then()
                .statusCode(201)
                .extract().path("bondId");

        // Now update the user with a new name.
        String updateRequest = "{\"userId\":\"1658cc09-ef5d-4f5b-8fe8-d9f7abfbfbec\",\"name\": \"Italian Government Bond 2030\",  " +
                "\"code\": \"ITB2030\", \"investedAmount\": 102200.00,\"annualRate\": 2.50,\"maturityDate\": \"2035-12-31\", " +
                "\"couponType\": \"Fixed\",\"currentValue\": 10250.75 }";

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .pathParam("bondId", bondId)
                .when()
                .put("/bond/updateBond/{bondId}")
                .then()
                .statusCode(200)
                .body("userId", equalTo("1658cc09-ef5d-4f5b-8fe8-d9f7abfbfbec"))
                .body("bondId", equalTo(bondId));
    }

    @Test
    void testDeleteBondById() {
        given()
                .pathParam("bondId", "b45e690e-56c2-4bb1-9922-6fbe6c7a789a")
                .when()
                .delete("/bond/deleteBond/{bondId}")
                .then()
                .statusCode(204);
    }

    @Test
    void testDeleteBondByIdNotFound() {
        given()
                .pathParam("bondId", "660e8400-e29b-41d4-a716-552255440112")
                .when()
                .delete("/bond/deleteBond/{bondId}")
                .then()
                .statusCode(404);
    }

    @Test
    void testDeleteBondByIdInternalServerError() {
        given()
                .pathParam("bondId", 11122)
                .when()
                .delete("/bond/deleteBond/{bondId}")
                .then()
                .statusCode(500);
    }
}
