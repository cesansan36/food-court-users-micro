package plazadecomidas.users.adapters.driven.jpa.mysql.exception;

public class RegistryNotFoundException extends RuntimeException{

    public RegistryNotFoundException(String message) {
        super(message);
    }
}
