package plazadecomidas.users.domain.model;

import plazadecomidas.users.domain.exception.EmptyFieldException;
import plazadecomidas.users.domain.util.DomainConstants;

public class Token {

    private final String value;

    public Token(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.TOKEN_FIELD));
        }
    }

    public String getValue() {
        return value;
    }
}
