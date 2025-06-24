import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetOrdersTest extends TestBase {

    @Test
    public void shouldReturnListOfOrders() {
        Response response = RestAssured
                .given()
                .when()
                .get("/api/v1/orders");
        response.then().statusCode(SC_OK);
        response.then().body("orders", is(notNullValue()));
        var ordersList = response.jsonPath().getList("orders");
        assertThat("Количество заказов должно быть больше нуля", ordersList.size(), greaterThan(0));
        System.out.println("Количество заказов: " + ordersList.size());
    }
}