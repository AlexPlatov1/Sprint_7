import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderCreationParamTest extends TestBase {

    private final OrderSteps orderSteps = new OrderSteps();

    static Stream<Object[]> provideColors() {
        return Stream.of(
                new Object[]{new String[]{"BLACK"}},
                new Object[]{new String[]{"GREY"}},
                new Object[]{new String[]{"BLACK", "GREY"}}
        );
    }

    @Step
    @ParameterizedTest(name = "Создание заказа с цветом(ами): {arguments}")
    @MethodSource("provideColors")
    public void testCreateOrderWithDifferentColors(String[] colors) {
        Order order = Order.getRandomOrder().setColor(colors);

        Response response = orderSteps.createOrder(order);
        response.then()
                .statusCode(201)
                .body("track", notNullValue());

        String track = response.jsonPath().getString("track");
        assertNotNull(track);
    }

    @Step
    @Test
    public void testCreateOrderWithoutColor() {
        Order order = Order.getRandomOrder();
        order.setColor(null); // Заказ без цвета

        Response response = orderSteps.createOrder(order);
        response.then()
                .statusCode(201)
                .body("track", notNullValue());

        String track = response.jsonPath().getString("track");
        assertNotNull(track);
    }
}