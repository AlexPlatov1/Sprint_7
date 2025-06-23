import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierSteps {

    @Step("Создать курьера с данными: {courier}")
    public Response createCourier(Courier courier) {
        return given()
                .contentType("application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    // Класс CourierSteps.java
    @Step("Удалить курьера по id")
    public Response deleteCourier(int courierId) {
        return given()
                .contentType("application/json")
                .when()
                .delete("/api/v1/courier/" + courierId);
    }

    @Step("Авторизоваться курьером с данными: {credentials}")
    public Response loginCourier(LoginCourier credentials) {
        return given()
                .contentType("application/json")
                .body(credentials)
                .when()
                .post("/api/v1/courier/login");
    }

}