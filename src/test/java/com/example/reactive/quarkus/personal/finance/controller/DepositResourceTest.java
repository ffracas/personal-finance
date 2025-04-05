package com.example.reactive.quarkus.personal.finance.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
public class DepositResourceTest {

    @Test
    void testCreateNewDeposit() {
        String requestBody = "{ \"id\": \"a1b2c3d4-e5f6-7890-abcd-1234567890ef\", "
                + "\"user_id\": \"f1234567-89ab-cdef-0123-456789abcdef\", \"invested_amount\": 10000.00, "
                + "\"annual_rate\": 3.50, \"start_date\": \"2024-01-01\", \"end_date\": \"2025-01-01\" }";
        given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/deposit/createDeposit")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void testCreateNewDepositWithoutUser() {
        String requestBody = "{ \"id\": \"a1b2c3d4-e5f6-7890-abcd-1234567890ef\", "
                + "\"user_id\": \"n0tv4l1d-u53r-nnot-0123-456789abcdef\", \"invested_amount\": 10000.00, "
                + "\"annual_rate\": 3.50, \"start_date\": \"2024-01-01\", \"end_date\": \"2025-01-01\" }";
        given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/deposit/createDeposit")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    void testReadAllDeposits() {
        given()
                .when()
                .get("/deposit/getAllDeposits")
                .then()
                .statusCode(200)
                // Assumes that at least one user exists in the system.
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    void testReadByDepositId() {
        given()
                .pathParam("depositId", "b2c3d4e5-f678-9012-abcd-2345678901bc")
                .when()
                .get("/deposit/getDepositById/{depositId}")
                .then()
                .statusCode(200)
                .body("depositId", equalTo("b2c3d4e5-f678-9012-abcd-2345678901bc"));
    }

    @Test
    void testReadByNotExitingDepositId() {
        given()
                .pathParam("depositId", "1658cc09-ef5d-4f5b-8fe8-d9f71bfbfbec")
                .when()
                .get("/deposit/getDepositById/{depositId}")
                .then()
                .statusCode(404);
    }

    @Test
    void testUpdateDeposit() {
        String requestBody = "{ \"id\": \"a1b2c3d4-e5f6-7890-abcd-1234567890ef\", "
                + "\"user_id\": \"n0tv4l1d-u53r-nnot-0123-456789abcdef\", \"invested_amount\": 10000.00, "
                + "\"annual_rate\": 3.50, \"start_date\": \"2024-01-01\", \"end_date\": \"2025-01-01\" }";

        String depositId = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/deposit/createDeposit")
                .then()
                .statusCode(201)
                .extract().path("depositId");

        String updateRequest = "{ \"id\": \"a1b2c3d4-e5f6-7890-abcd-1234567890ef\", "
                + "\"user_id\": \"n0tv4l1d-u53r-nnot-0123-456789abcdef\", \"invested_amount\": 20000.00, "
                + "\"annual_rate\": 5.00, \"start_date\": \"2024-01-01\", \"end_date\": \"2025-01-01\" }";

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .pathParam("depositId", depositId)
                .when()
                .put("/deposit/updateDeposit/{depositId}")
                .then()
                .statusCode(200)
                .body("userId", equalTo("1658cc09-ef5d-4f5b-8fe8-d9f7abfbfbec"))
                .body("depositId", equalTo(depositId))
                .body("investedAmount", equalTo(20000.00))
                .body("annualRate", equalTo(5.00));
    }

    @Test
    void testUpdateWithWrongUser() {
        String requestBody = "{ \"id\": \"a1b2c3d4-e5f6-7890-abcd-1234567890ef\", "
                + "\"user_id\": \"n0tv4l1d-u53r-nnot-0123-456789abcdef\", \"invested_amount\": 10000.00, "
                + "\"annual_rate\": 3.50, \"start_date\": \"2024-01-01\", \"end_date\": \"2025-01-01\" }";

        String depositId = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/deposit/createDeposit")
                .then()
                .statusCode(201)
                .extract().path("depositId");

        String updateRequest = "{ \"id\": \"a1b2c3d4-e5f6-7890-abcd-1234567890ef\", "
                + "\"user_id\": \"n0tv4l1d-u53r-nnot-0123-456789abcdef\", \"invested_amount\": 20000.00, "
                + "\"annual_rate\": 5.00, \"start_date\": \"2024-01-01\", \"end_date\": \"2025-01-01\" }";

        given().contentType(ContentType.JSON)
                .body(updateRequest)
                .pathParam("depositId", depositId)
                .when()
                .put("/deposit/updateDeposit/{depositId}")
                .then()
                .log().all()
                .statusCode(409);
    }

    @Test
    void testDeleteDeposit() {
        given()
                .pathParam("depositId", "c3d4e5f6-7890-1234-abcd-3456789012cd")
                .when()
                .delete("/deposit/deleteDeposit/{depositId}")
                .then()
                .statusCode(204);
    }

    @Test
    void testDeleteNotExistingDeposit() {
        given()
                .pathParam("depositId", "660e8400-e29b-41d4-a716-552255440112")
                .when()
                .delete("/deposit/deleteDepositById/{depositId}")
                .then()
                .statusCode(404);
    }
}
