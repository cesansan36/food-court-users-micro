package plazadecomidas.users.domain.util;

import plazadecomidas.users.domain.model.Role;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Validator {

    private Validator() {throw new IllegalStateException("Utility class");}

    private static final String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_NUMBER_PATTERN = "^(\\+)?\\d{1,13}$";
    private static final String NUMBERS_ONLY_PATTERN = "\\d+";

    private static final Pattern EMAIL_REGEX = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
    private static final Pattern PHONE_NUMBER_REGEX = Pattern.compile(PHONE_NUMBER_PATTERN);
    private static final Pattern NUMBERS_ONLY_REGEX = Pattern.compile(NUMBERS_ONLY_PATTERN);


    public static boolean isValidEmail(String email) {
        return EMAIL_REGEX.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return PHONE_NUMBER_REGEX.matcher(phoneNumber).matches();
    }

    public static boolean isFieldEmpty(String field) {
        return field == null || field.trim().isEmpty();
    }

    public static boolean isValidDocumentNumber(String documentNumber) {
        return NUMBERS_ONLY_REGEX.matcher(documentNumber).matches();
    }

    public static boolean isValidAge(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        int age = now.getYear() - birthDate.getYear();
        return age >= DomainConstants.AGE_MINIMUM;
    }

    public static boolean isValidRole(Role role) {
        return DomainConstants.Roles.appearsInList(role.getId());
    }
}
