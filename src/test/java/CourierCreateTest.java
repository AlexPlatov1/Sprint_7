import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static org.hamcrest.Matchers.*;

public class CourierCreateTest extends TestBase {

    private final CourierSteps courierSteps = new CourierSteps();
    private Courier createdCourier;
    private Integer courierId;

    @AfterEach
    void tearDown() {
        if (courierId != null) {
            courierSteps.deleteCourier(courierId).then().statusCode(200);
            courierId = null;
        }
        createdCourier = null;
    }

    @Step
    @Test
    @DisplayName("Успешное создание курьера")
    void createCourierSuccess() {
        Courier courier = Courier.getCourierWithLoginAndPassword();

        Response response = courierSteps.createCourier(courier);
        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));
        Response loginResponse = courierSteps.loginCourier(LoginCourier.from(courier));
        loginResponse.then().statusCode(200).body("id", notNullValue());
        createdCourier = courier;
        courierId = Integer.valueOf(loginResponse.jsonPath().getString("id"));
    }

    @Step
    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    void cannotCreateDuplicateCouriers() {
        Courier courier1 = Courier.getCourierWithLoginAndPassword();
        Response createResponse = courierSteps.createCourier(courier1);
        createResponse.then().statusCode(201);
        Response loginResponse = courierSteps.loginCourier(LoginCourier.from(courier1));
        loginResponse.then().statusCode(200).body("id", notNullValue());
        createdCourier = courier1;
        Response duplicateResponse = courierSteps.createCourier(courier1);
        duplicateResponse.then()
                .statusCode(409);
    }


    @Step
    @Test
    @DisplayName("Создание без обязательных полей возвращает ошибку")
    void createWithoutRequiredFields() {
        Courier courierWithoutLogin = Courier.getCourierWithPasswordOnly();
        Response response1 = courierSteps.createCourier(courierWithoutLogin);
        response1.then()
                .statusCode(400);
        Courier courierWithoutPassword = Courier.getCourierWithLoginOnly();
        Response response2 = courierSteps.createCourier(courierWithoutPassword);
        response2.then()
                .statusCode(400);
        Courier courierWithoutFirstName = Courier.getCourierWithFirstnameOnly();
        Response response3 = courierSteps.createCourier(courierWithoutFirstName);
        response3.then()
                .statusCode(400);
    }
}
