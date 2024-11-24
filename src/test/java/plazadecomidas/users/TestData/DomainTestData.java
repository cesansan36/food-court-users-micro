package plazadecomidas.users.TestData;

import plazadecomidas.users.domain.model.Role;
import plazadecomidas.users.domain.model.User;

import java.time.LocalDate;

public class DomainTestData {

    public static final String NAME_FIELD = "name %s";
    public static final String LAST_NAME_FIELD = "lastName %s";
    public static final String DOCUMENT_NUMBER_FIELD = "12345678%s";
    public static final String CELL_PHONE_FIELD = "+12345678%s";
    public static final String EMAIL_FIELD = "email%s@example.com";
    public static final String PASSWORD_FIELD = "1234%s";

    public static Role getValidRole() {
        return new Role(1L, "ROLE_ADMIN", "The user with admin access");
    }

    public static User getValidUser(Long id) {
        return new User(id,
                NAME_FIELD.formatted(id),
                LAST_NAME_FIELD.formatted(id),
                DOCUMENT_NUMBER_FIELD.formatted(id),
                CELL_PHONE_FIELD.formatted(id),
                LocalDate.of(1994, 11, 15),
                EMAIL_FIELD.formatted(id),
                PASSWORD_FIELD.formatted(id),
                getValidRole());
    }
}
