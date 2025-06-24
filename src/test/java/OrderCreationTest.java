import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderCreationTest extends TestBase {

    private final OrderSteps orderSteps = new OrderSteps();

    @Step
    @Test
    public void createOrderWithOneColorBlack() {
        Order order = Order.getRandomOrder().setColor(new String[]{"BLACK"});

        Response response = orderSteps.createOrder(order);
        response.then()
                .statusCode(SC_CREATED)
                .body("track", notNullValue());

        String track = response.jsonPath().getString("track");
        assertNotNull(track);
        System.out.println();
    }

    @Step
    @Test
    public void createOrderWithOneColorGrey() {
        Order order = Order.getRandomOrder().setColor(new String[]{"GREY"});

        Response response = orderSteps.createOrder(order);
        response.then()
                .statusCode(SC_CREATED)
                .body("track", notNullValue());

        String track = response.jsonPath().getString("track");
        assertNotNull(track);
    }

    @Step
    @Test
    public void createOrderWithBothColors() {
        Order order = Order.getRandomOrder().setColor(new String[]{"BLACK", "GREY"});

        Response response = orderSteps.createOrder(order);
        response.then()
                .statusCode(SC_CREATED)
                .body("track", notNullValue());

        String track = response.jsonPath().getString("track");
        assertNotNull(track);
    }

    @Step
    @Test
    public void createOrderWithoutColor() {
        Order order = Order.getRandomOrder();
        order.setColor(null); // или не указывать вообще

        Response response = orderSteps.createOrder(order);
        response.then()
                .statusCode(SC_CREATED)
                .body("track", notNullValue());

        String track = response.jsonPath().getString("track");
        assertNotNull(track);
    }
}