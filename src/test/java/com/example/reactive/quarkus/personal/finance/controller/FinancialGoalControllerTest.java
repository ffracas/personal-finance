package com.example.reactive.quarkus.personal.finance.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class FinancialGoalControllerTest {
    @Test
    void dummyTest() {
        given().get("/financial-goal").then().statusCode(200);
    }
}
