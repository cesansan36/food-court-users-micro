package plazadecomidas.users.adapters.driven.connection.util;

public class ConnectionConstants {

    private ConnectionConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String FAILED_EMPLOYEE_RESTAURANT_LINK = "Could not link the registered employee in the restaurant microservice - Message received from the microservice: %s";
}
