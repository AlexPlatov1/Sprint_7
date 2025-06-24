import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;

public class CourierAuthTest extends TestBase {

    private final CourierSteps courierSteps = new CourierSteps();
    private Integer courierId;
    private Courier newCourier;


    private void createCourierAndStoreId(Courier courier) {
        Response response = courierSteps.createCourier(courier);
        response.then().statusCode(SC_CREATED);
        Response loginResponse = courierSteps.loginCourier(LoginCourier.from(courier));
        loginResponse.then().statusCode(SC_OK).body("id", notNullValue());
        courierId = Integer.valueOf(loginResponse.jsonPath().getString("id"));
    }

    @BeforeEach
    void setUp() {
        newCourier = Courier.getCourierWithLoginAndPassword();
        createCourierAndStoreId(newCourier);
    }

    @AfterEach
    void tearDown() {
        if (courierId != null) {
            courierSteps.deleteCourier(courierId).then().statusCode(SC_OK);
            courierId = null;
        }
        newCourier = null;
    }

    @Step
    @Test
    @DisplayName("Курьер может авторизоваться с правильными данными")
    void courierCanLogin() {
        Response response = courierSteps.loginCourier(LoginCourier.from(newCourier));
        response.then()
                .statusCode(SC_OK)
                .body("id", notNullValue());
        System.out.println(response.body().asString());
    }

    @Step
    @Test
    @DisplayName("Авторизация с неправильным паролем возвращает ошибку")
    void loginWithInvalidPassword() {
        LoginCourier invalidPassword = LoginCourier.getCourierCredentialsWithInvalidPassword(newCourier);
        courierSteps.loginCourier(invalidPassword)
                .then()
                .statusCode(anyOf(is(SC_NOT_FOUND), is(SC_UNAUTHORIZED)));
    }

    @Step
    @Test
    @DisplayName("Авторизация с неправильным логином возвращает ошибку")
    void loginWithInvalidLogin() {
        LoginCourier invalidLogin = LoginCourier.getCourierCredentialsWithInvalidLogin(newCourier);
        courierSteps.loginCourier(invalidLogin)
                .then()
                .statusCode(anyOf(is(SC_NOT_FOUND), is(SC_UNAUTHORIZED)));
    }

    @Step
    @Test
    @DisplayName("Авторизация без логина возвращает ошибку")
    void loginWithoutLogin() {
        LoginCourier withoutLogin = LoginCourier.getCourierCredentialsWithoutLogin(newCourier);
        courierSteps.loginCourier(withoutLogin).then().statusCode(SC_BAD_REQUEST);
    }

    @Step
    @Test
    @DisplayName("Авторизация без пароля возвращает ошибку")
    void loginWithoutPassword() {
        LoginCourier withoutPassword = LoginCourier.getCourierCredentialsWithoutPassword(newCourier);
        courierSteps.loginCourier(withoutPassword).then().statusCode(SC_BAD_REQUEST);
    }

    @Step
    @Test
    @DisplayName("Авторизация с несуществующим логином возвращает ошибку")
    void loginWithNonExistentLogin() {
        String fakeLogin = Courier.getRandom().login; // случайный логин, который не зарегистрирован
        String validPassword = newCourier.getPassword(); // используем правильный пароль для другого пользователя или случайный, если допустимо
        LoginCourier nonExistentLogin = new LoginCourier(fakeLogin, validPassword);
        courierSteps.loginCourier(nonExistentLogin).then().statusCode(anyOf(is(SC_NOT_FOUND), is(SC_UNAUTHORIZED)));
    }

    @Step
    @Test
    @DisplayName("Авторизация с несуществующим паролем возвращает ошибку")
    void loginWithNonExistentPassword() {
        String validLogin = newCourier.getLogin(); // используем правильный логин зарегистрированного курьера
        String fakePassword = Courier.getRandom().password; // случайный пароль, который не подходит
        LoginCourier nonExistentPassword = new LoginCourier(validLogin, fakePassword);
        courierSteps.loginCourier(nonExistentPassword).then().statusCode(anyOf(is(SC_NOT_FOUND), is(SC_UNAUTHORIZED)));
    }

    @Step
    @Test
    @DisplayName("Успешная авторизация возвращает id")
    void successfulLoginReturnsId() {
        Response response = courierSteps.loginCourier(LoginCourier.from(newCourier));
        response.then()
                .statusCode(SC_OK)
                .body("id", notNullValue());
        System.out.println(response.body().asString());
    }
}