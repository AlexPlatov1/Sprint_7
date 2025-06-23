import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class OrderSteps {

    @Step("Создать заказ: {order}")
    public Response createOrder(Order order) {
        return RestAssured.given()
                .contentType("application/json")
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

}