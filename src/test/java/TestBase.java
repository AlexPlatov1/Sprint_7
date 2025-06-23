import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public abstract class TestBase {

    protected static final String BASE_URI = "https://qa-scooter.praktikum-services.ru"; // Адрес API

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = BASE_URI;
    }
}