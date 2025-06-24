import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;

public class CourierCreateTest extends TestBase {

    private final CourierSteps courierSteps = new CourierSteps();
    private Courier createdCourier;
    private Integer courierId;

    @AfterEach
    void tearDown() {
        if (courierId != null) {
            courierSteps.deleteCourier(courierId).then().statusCode(SC_OK);
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
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
        Response loginResponse = courierSteps.loginCourier(LoginCourier.from(courier));
        loginResponse.then().statusCode(SC_OK).body("id", notNullValue());
        createdCourier = courier;
        courierId = Integer.valueOf(loginResponse.jsonPath().getString("id"));
    }

    @Step
    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    void cannotCreateDuplicateCouriers() {
        Courier courier1 = Courier.getCourierWithLoginAndPassword();
        Response createResponse = courierSteps.createCourier(courier1);
        createResponse.then().statusCode(SC_CREATED);
        Response loginResponse = courierSteps.loginCourier(LoginCourier.from(courier1));
        loginResponse.then().statusCode(SC_OK).body("id", notNullValue());
        createdCourier = courier1;
        Response duplicateResponse = courierSteps.createCourier(courier1);
        duplicateResponse.then()
                .statusCode(SC_CONFLICT);
    }


    @Step
    @Test
    @DisplayName("Создание курьера без логина возвращает ошибку")
    void createCourierWithoutLogin() {
        Courier courierWithoutLogin = Courier.getCourierWithPasswordOnly();
        Response response = courierSteps.createCourier(courierWithoutLogin);
        response.then()
                .statusCode(SC_BAD_REQUEST);
    }

    @Step
    @Test
    @DisplayName("Создание курьера без пароля возвращает ошибку")
    void createCourierWithoutPassword() {
        Courier courierWithoutPassword = Courier.getCourierWithLoginOnly();
        Response response = courierSteps.createCourier(courierWithoutPassword);
        response.then()
                .statusCode(SC_BAD_REQUEST);
    }

    @Step
    @Test
    @DisplayName("Создание курьера без имени возвращает ошибку")
    void createCourierWithoutFirstName() {
        Courier courierWithoutFirstName = Courier.getCourierWithFirstnameOnly();
        Response response = courierSteps.createCourier(courierWithoutFirstName);
        response.then()
                .statusCode(SC_BAD_REQUEST);
    }
}
