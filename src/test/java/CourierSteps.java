import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierSteps {

    private static final String BASE_URL = "/api/v1/courier";

    @Step("Создать курьера с данными: {courier}")
    public Response createCourier(Courier courier) {
        return given()
                .contentType("application/json")
                .body(courier)
                .when()
                .post(BASE_URL);
    }

    @Step("Удалить курьера по id")
    public Response deleteCourier(int courierId) {
        return given()
                .contentType("application/json")
                .when()
                .delete(BASE_URL + "/" + courierId);
    }

    @Step("Авторизоваться курьером с данными: {credentials}")
    public Response loginCourier(LoginCourier credentials) {
        return given()
                .contentType("application/json")
                .body(credentials)
                .when()
                .post(BASE_URL + "/login");
    }
}