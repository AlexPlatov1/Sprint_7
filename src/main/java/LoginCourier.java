import org.apache.commons.lang3.RandomStringUtils;

public class LoginCourier {
    public final String login;
    public final String password;

    public LoginCourier(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static LoginCourier from(Courier courier) {
        return new LoginCourier(courier.login, courier.password);
    }

    public static LoginCourier getCourierCredentialsWithInvalidLogin(Courier courier){
        courier.setLogin(RandomStringUtils.randomAlphabetic(10));
        return new LoginCourier(courier.login, courier.password);
    }
    public static LoginCourier getCourierCredentialsWithInvalidPassword(Courier courier){
        courier.setPassword(RandomStringUtils.randomAlphabetic(10));
        return new LoginCourier(courier.login, courier.password);
    }
    public static LoginCourier getCourierCredentialsWithoutPassword(Courier courier){
        return new LoginCourier(courier.login, "");
    }

    public static LoginCourier getCourierCredentialsWithoutLogin(Courier courier){
        return new LoginCourier("", courier.password);
    }
}