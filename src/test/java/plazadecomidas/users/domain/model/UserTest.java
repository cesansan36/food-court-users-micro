package plazadecomidas.users.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import plazadecomidas.users.TestData.DomainTestData;
import plazadecomidas.users.domain.exception.EmptyFieldException;
import plazadecomidas.users.domain.exception.FieldRuleInvalidException;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Create User with correct data")
    void createUserWithCorrectData() {

        Role role = DomainTestData.getValidRole();
        User user = new User(
                1L,
                "Roberto",
                "Hongo",
                "123456789",
                "+123456789",
                LocalDate.of(1994, 11, 15),
                "somemail@example.com",
                "123456",
                role
                );

        assertAll(
                () -> assertEquals(1L, user.getId()),
                () -> assertEquals("Roberto", user.getName()),
                () -> assertEquals("Hongo", user.getLastName()),
                () -> assertEquals("123456789", user.getDocumentNumber()),
                () -> assertEquals("+123456789", user.getCellPhoneNumber()),
                () -> assertEquals(LocalDate.of(1994, 11, 15), user.getBirthDate()),
                () -> assertEquals("somemail@example.com", user.getEmail()),
                () -> assertEquals("123456", user.getPassword()),
                () -> assertEquals(role, user.getRole())
        );
    }

    @ParameterizedTest
    @DisplayName("Should pass with correct data")
    @MethodSource("correctData")
    void shouldPassWithCorrectData(String name, String lastName, String documentNumber, String cellPhoneNumber, LocalDate birthDate, String email, String password, Role role) {

        String nameTrim = name.trim();
        String lastNameTrim = lastName.trim();
        String documentNumberTrim = documentNumber.trim();
        String cellPhoneNumberTrim = cellPhoneNumber.trim();
        String emailTrim = email.trim();
        String passwordTrim = password.trim();

        User user = new User(
                1L,
                name,
                lastName,
                documentNumber,
                cellPhoneNumber,
                birthDate,
                email,
                password,
                role
                );

        assertAll(
                () -> assertEquals(1L, user.getId()),
                () -> assertEquals(nameTrim, user.getName()),
                () -> assertEquals(lastNameTrim, user.getLastName()),
                () -> assertEquals(documentNumberTrim, user.getDocumentNumber()),
                () -> assertEquals(cellPhoneNumberTrim, user.getCellPhoneNumber()),
                () -> assertEquals(birthDate, user.getBirthDate()),
                () -> assertEquals(emailTrim, user.getEmail()),
                () -> assertEquals(passwordTrim, user.getPassword()),
                () -> assertEquals(role, user.getRole())
        );
    }

    @ParameterizedTest
    @DisplayName("Should fail with empty fields")
    @MethodSource("dataWithNullOrEmptyFields")
    void shouldFailWithEmptyFields(String name, String lastName, String documentNumber, String cellPhoneNumber, LocalDate birthDate, String email, String password, Role role) {
        assertThrows(EmptyFieldException.class, () -> new User(
                1L,
                name,
                lastName,
                documentNumber,
                cellPhoneNumber,
                birthDate,
                email,
                password,
                role
                ));
    }

    @ParameterizedTest
    @DisplayName("Should fail with broken rule fields")
    @MethodSource("dataWithBrokenRules")
    void shouldFailWithBrokenRules(String name, String lastName, String documentNumber, String cellPhoneNumber, LocalDate birthDate, String email, String password, Role role) {
        assertThrows(FieldRuleInvalidException.class, () -> new User(
                1L,
                name,
                lastName,
                documentNumber,
                cellPhoneNumber,
                birthDate,
                email,
                password,
                role
                ));
    }

    static Stream<Arguments> correctData () {
        return Stream.of(
                Arguments.of("Roberto", "Hongo", "123456789", "+123456789", LocalDate.of(1994, 11, 15), "somemail@example.com", "123456", DomainTestData.getValidRole()),
                Arguments.of("   Roberto   ", " Hongo ", "123456789", "123456789", LocalDate.of(1937, 3, 9), "somemail@example.com.co", "pFJ31*%123=456", new Role(1L, "Dummy", "Dummy")),
                Arguments.of("Roberto", "Hongo", "123456789", "+123456789", LocalDate.of(1994, 11, 15), "some.mail@example.com", "123456", new Role(3L, "Dummy", "Dummy"))
        );
    }

    static Stream<Arguments> dataWithNullOrEmptyFields () {
        return Stream.of(
                Arguments.of("", "Hongo", "123456789", "+123456789", LocalDate.of(1994, 11, 15), "somemail@example.com", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "", "123456789", "+123456789", LocalDate.of(1994, 11, 15), "somemail@example.com", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "    ", "+123456789", LocalDate.of(1994, 11, 15), "somemail@example.com", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "123456789", "  ", LocalDate.of(1994, 11, 15), "somemail@example.com", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "123456789", "123456789", LocalDate.of(1994, 11, 15), "", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "123456789", "+123456789", LocalDate.of(1994, 11, 15), "somemail@example.com", "", DomainTestData.getValidRole()),
                Arguments.of(null, "Hongo", "123456789", "+123456789", LocalDate.of(1994, 11, 15), "somemail@example.com", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", null, "123456789", "+123456789", LocalDate.of(1994, 11, 15), "somemail@example.com", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", null, "+123456789", LocalDate.of(1994, 11, 15), "somemail@example.com", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "123456789", null, LocalDate.of(1994, 11, 15), "somemail@example.com", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "123456789", "123456789", LocalDate.of(1994, 11, 15), null, "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "123456789", "+123456789", LocalDate.of(1994, 11, 15), "somemail@example.com", null, DomainTestData.getValidRole())
        );
    }

    static Stream<Arguments> dataWithBrokenRules() {
        return Stream.of(
                Arguments.of("Roberto", "Hongo", "123456789abcde", "123456789", LocalDate.of(1994, 11, 15), "somemail@example.com", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "123456789", "-123456789", LocalDate.of(1994, 11, 15), "somemail@example.com", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "123456789", "123+456789", LocalDate.of(1994, 11, 15), "somemail@example.com", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "123456789", "123456789123456789", LocalDate.of(1994, 11, 15), "somemail@example.com", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "123456789", "+123456789", LocalDate.now().plusYears(5), "somemail@example.com", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "123456789", "+123456789", LocalDate.of(1994, 11, 15), "@example.com", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "123456789", "+123456789", LocalDate.of(1994, 11, 15), "somemail@.com", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "123456789", "+123456789", LocalDate.of(1994, 11, 15), "somemail@example..com", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "123456789", "+123456789", LocalDate.of(1994, 11, 15), "somemail@example.com.", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "123456789", "+123456789", LocalDate.of(1994, 11, 15), ".somemail@example.com", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "123456789", "+123456789", LocalDate.of(1994, 11, 15), "somemail@example", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "123456789", "+123456789", LocalDate.of(1994, 11, 15), "some(mail)@example.com", "123456", DomainTestData.getValidRole()),
                Arguments.of("Roberto", "Hongo", "123456789", "+123456789", LocalDate.of(1994, 11, 15), "somemail@example.com", "123456", new Role(-1L, "Dummy", "Dummy"))
        );
    }
}