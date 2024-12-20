package plazadecomidas.users.domain.model;

import plazadecomidas.users.domain.exception.EmptyFieldException;
import plazadecomidas.users.domain.exception.FieldRuleInvalidException;
import plazadecomidas.users.domain.util.DomainConstants;
import plazadecomidas.users.domain.util.Validator;

import java.time.LocalDate;

public class User {

    private final Long id;
    private final String name;
    private final String lastName;
    private final String documentNumber;
    private final String cellPhoneNumber;
    private final LocalDate birthDate;
    private final String email;
    private final String password;
    private final Role role;

    public User(Long id, String name, String lastName, String documentNumber, String cellPhoneNumber, LocalDate birthDate, String email, String password, Role role) {

        validateData(name, lastName, documentNumber, cellPhoneNumber, birthDate, email, password, role);

        name = name.trim();
        lastName = lastName.trim();
        documentNumber = documentNumber.trim();
        cellPhoneNumber = cellPhoneNumber.trim();
        email = email.trim();
        password = password.trim();

        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.documentNumber = documentNumber;
        this.cellPhoneNumber = cellPhoneNumber;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    private void validateData(String name, String lastName, String documentNumber, String cellPhoneNumber, LocalDate birthDate, String email, String password, Role role) {
        if (Validator.isFieldEmpty(name)) {
            throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.Fields.NAME.name()));
        }

        if (Validator.isFieldEmpty(lastName)) {
            throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.Fields.LAST_NAME.name()));
        }

        if (Validator.isFieldEmpty(password)) {
            throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.Fields.PASSWORD.name()));
        }
        if (Validator.isFieldEmpty(documentNumber)) {
            throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.Fields.DOCUMENT_NUMBER.name()));
        }

        if (Validator.isFieldEmpty(cellPhoneNumber)) {
            throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.Fields.CELL_PHONE_NUMBER.name()));
        }

        if (Validator.isFieldEmpty(email)) {
            throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.Fields.EMAIL.name()));
        }

        if (birthDate == null) {
            throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.Fields.BIRTH_DATE.name()));
        }

        if (role == null) {
            throw new EmptyFieldException(DomainConstants.EMPTY_FIELD_MESSAGE.formatted(DomainConstants.Fields.ROLE_ID.name()));
        }

        if (!Validator.isValidRole(role)) {
            throw new FieldRuleInvalidException(DomainConstants.ROLE_ID_NOT_VALID);
        }

        if (!Validator.isValidDocumentNumber(documentNumber)) {
            throw new FieldRuleInvalidException(DomainConstants.DOCUMENT_NUMBER_SHOULD_CONTAIN_ONLY_NUMBERS);
        }

        if (!Validator.isValidPhoneNumber(cellPhoneNumber)) {
            throw new FieldRuleInvalidException(DomainConstants.PHONE_NUMBER_NOT_VALID_MESSAGE.formatted(DomainConstants.PHONE_NUMBER_MAX_LENGTH));
        }

        if (!Validator.isValidEmail(email)) {
            throw new FieldRuleInvalidException(DomainConstants.EMAIL_STRUCTURE_NOT_VALID);
        }

        if (!Validator.isValidAge(birthDate)) {
            throw new FieldRuleInvalidException(DomainConstants.AGE_BELOW_MINIMUM.formatted(DomainConstants.AGE_MINIMUM));
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
