package plazadecomidas.users.adapters.driven.jpa.mysql.exception;

public class RegistryAlreadyExistsException extends RuntimeException {

    public RegistryAlreadyExistsException(String message) {
        super(message);
    }
}
