package com.fabio.financialtransfer.integrationTest;

import com.fabio.financialtransfer.domain.util.Message;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Testing integration between services")
public class TransferControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void shouldTransferFundsSuccessfully() {
        String requestBody = "{ \"debitAccountId\": 1, \"creditAccountId\": 2, \"amount\": 100.00 }";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/transfer")
                .then()
                .statusCode(200)
                .body("status", equalTo("Success"))
                .body("message", equalTo(Message.TRANSFER_SUCCESS.getCode()));
    }

    @Test
    public void shouldFailDueToUnsupportedCurrency() {
        // Account 4 with currency XYZ
        String requestBody = "{ \"debitAccountId\": 4, \"creditAccountId\": 2, \"amount\": 50.00 }";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/transfer")
                .then()
                .statusCode(400)
                .body("status", equalTo("Failed"))
                .body("message", equalTo(Message.ERROR_UNSUPPORTED_CURRENCY.getCode()));
    }

    @Test
    public void shouldFailDueToInsufficientBalance() {
        // Exceeding balance
        String requestBody = "{ \"debitAccountId\": 1, \"creditAccountId\": 2, \"amount\": 1500.00 }";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/transfer")
                .then()
                .statusCode(400)
                .body("status", equalTo("Failed"))
                .body("message", equalTo(Message.ERROR_INSUFFICIENT_BALANCE.getCode()));
    }

    @Test
    public void shouldFailDueToNullOrNegativeAccountId() {
        String requestBodyNullId = "{ \"debitAccountId\": null, \"creditAccountId\": 2, \"amount\": 100.00 }";
        String requestBodyNegativeId = "{ \"debitAccountId\": -1, \"creditAccountId\": 2, \"amount\": 100.00 }";

        given()
                .contentType(ContentType.JSON)
                .body(requestBodyNullId)
                .when()
                .post("/api/v1/transfer")
                .then()
                .statusCode(400)
                .body("status", equalTo("Failed"))
                .body("message", equalTo("Debit account ID cannot be null"));

        given()
                .contentType(ContentType.JSON)
                .body(requestBodyNegativeId)
                .when()
                .post("/api/v1/transfer")
                .then()
                .statusCode(400)
                .body("status", equalTo("Failed"))
                .body("message", equalTo("Debit account ID must be positive"));
    }

    @Test
    public void shouldFailDueToNegativeOrNullAmount() {
        String requestBodyNullAmount = "{ \"debitAccountId\": 1, \"creditAccountId\": 2, \"amount\": null }";
        String requestBodyNegativeAmount = "{ \"debitAccountId\": 1, \"creditAccountId\": 2, \"amount\": -100.00 }";

        given()
                .contentType(ContentType.JSON)
                .body(requestBodyNullAmount)
                .when()
                .post("/api/v1/transfer")
                .then()
                .statusCode(400)
                .body("status", equalTo("Failed"))
                .body("message", equalTo("Amount cannot be null"));

        given()
                .contentType(ContentType.JSON)
                .body(requestBodyNegativeAmount)
                .when()
                .post("/api/v1/transfer")
                .then()
                .statusCode(400)
                .body("status", equalTo("Failed"))
                .body("message", equalTo("Amount must be positive"));
    }

    @Test
    public void shouldFailDueToSameDebitAndCreditAccount() {
        String requestBody = "{ \"debitAccountId\": 1, \"creditAccountId\": 1, \"amount\": 100.00 }";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/v1/transfer")
                .then()
                .statusCode(400)
                .body("status", equalTo("Failed"))
                .body("message", equalTo("Transfer between the same account is not allowed"));
    }
}
