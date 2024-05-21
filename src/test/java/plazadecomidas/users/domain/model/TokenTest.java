package plazadecomidas.users.domain.model;

import org.junit.jupiter.api.Test;
import plazadecomidas.users.domain.exception.EmptyFieldException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenTest {

    @Test
    void createTokenCorrectly() {
        String value = "test";
        Token token = new Token(value);
        assertEquals(value, token.getValue());
    }

    @Test
    void createTokenWithEmptyValue() {
        String value = "";
        assertThrows(EmptyFieldException.class, () -> new Token(value));
    }

    @Test
    void createTokenWithNullValue() {
        String value = null;
        assertThrows(EmptyFieldException.class, () -> new Token(value));
    }
}