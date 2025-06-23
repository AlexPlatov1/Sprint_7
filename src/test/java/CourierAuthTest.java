import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;

public class CourierAuthTest extends TestBase {

    private final CourierSteps courierSteps = new CourierSteps();
    private Integer courierId;

    @AfterEach
    void tearDown() {
        if (courierId != null) {
            courierSteps.deleteCourier(courierId).then().statusCode(200);
            courierId = null;
        }
    }
    private void createCourierAndStoreId(Courier courier) {
        Response response = courierSteps.createCourier(courier);
        response.then().statusCode(201);
        Response loginResponse = courierSteps.loginCourier(LoginCourier.from(courier));
        loginResponse.then().statusCode(200).body("id", notNullValue());
        courierId = Integer.valueOf(loginResponse.jsonPath().getString("id"));
    }

    @Step
    @Test
    @DisplayName("Курьер может авторизоваться с правильными данными")
    void courierCanLogin() {
        Courier courier = Courier.getCourierWithLoginAndPassword();
        createCourierAndStoreId(courier);

        Response response = courierSteps.loginCourier(LoginCourier.from(courier));
        response.then()
                .statusCode(200)
                .body("id", notNullValue());
        System.out.println(response.body().asString());
    }

    @Step
    @Test
    @DisplayName("Авторизация с неправильным логином или паролем возвращает ошибку")
    void loginWithInvalidCredentials() {
        Courier courier = Courier.getCourierWithLoginAndPassword();
        createCourierAndStoreId(courier);

        LoginCourier invalidLogin = LoginCourier.getCourierCredentialsWithInvalidLogin(courier);
        courierSteps.loginCourier(invalidLogin).then().statusCode(anyOf(is(404), is(401)));

        LoginCourier invalidPassword = LoginCourier.getCourierCredentialsWithInvalidPassword(courier);
        courierSteps.loginCourier(invalidPassword).then().statusCode(anyOf(is(404), is(401)));
    }

    @Step
    @Test
    @DisplayName("Авторизация без обязательных полей возвращает ошибку")
    void loginWithoutRequiredFields() {
        Courier courier = Courier.getCourierWithLoginAndPassword();
        createCourierAndStoreId(courier);

        LoginCourier withoutLogin = LoginCourier.getCourierCredentialsWithoutLogin(courier);
        courierSteps.loginCourier(withoutLogin).then().statusCode(400);

        LoginCourier withoutPassword = LoginCourier.getCourierCredentialsWithoutPassword(courier);
        courierSteps.loginCourier(withoutPassword).then().statusCode(400);
    }

    @Step
    @Test
    @DisplayName("Авторизация под несуществующим пользователем возвращает ошибку")
    void loginNonExistentUser() {
        String fakeLogin = Courier.getRandom().login;
        String fakePassword = Courier.getRandom().password;

        LoginCourier nonExistentUser = new LoginCourier(fakeLogin, fakePassword);

        courierSteps.loginCourier(nonExistentUser).then().statusCode(anyOf(is(404), is(401)));
    }

    @Step
    @Test
    @DisplayName("Успешная авторизация возвращает id")
    void successfulLoginReturnsId() {
        Courier courier = Courier.getCourierWithLoginAndPassword();
        createCourierAndStoreId(courier);

        Response response = courierSteps.loginCourier(LoginCourier.from(courier));

        response.then()
                .statusCode(200)
                .body("id", notNullValue());

        System.out.println(response.body().asString());
    }
}