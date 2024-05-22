package plazadecomidas.users.domain.util;

public class DomainConstants {

    private DomainConstants() {throw new IllegalStateException("Utility class");}

    public enum Roles {
        ADMIN(1L),
        OWNER(2L),
        EMPLOYEE(3L),
        CLIENT(4L);

        private final Long id;

        Roles(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public static boolean appearsInList(Long value) {
            for (Roles role : Roles.values()) {
                if (role.getId().equals(value)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static final Integer AGE_MINIMUM = 18;
    public static final Integer PHONE_NUMBER_MAX_LENGTH = 13;

    public enum Fields {
        NAME,
        LAST_NAME,
        DOCUMENT_NUMBER,
        CELL_PHONE_NUMBER,
        EMAIL,
        BIRTH_DATE,
        PASSWORD,
        ROLE_ID
    }

    public static final String TOKEN_FIELD = "token";

    public static final String EMPTY_FIELD_MESSAGE = "The field %s cannot be empty";
    public static final String EMAIL_STRUCTURE_NOT_VALID = "The email field is not valid";
    public static final String PHONE_NUMBER_CHAR_AMOUNT_EXCEEDED = "The cell phone number can not have more than %s characters";
    public static final String DOCUMENT_NUMBER_SHOULD_CONTAIN_ONLY_NUMBERS = "The document number should contain only numbers";
    public static final String AGE_BELOW_MINIMUM = "The user age should be at least %s";
    public static final String ROLE_ID_NOT_VALID = "The role id is not valid";

}
