package plazadecomidas.users.TestData;

import plazadecomidas.users.adapters.driven.jpa.mysql.entity.RoleEntity;
import plazadecomidas.users.adapters.driven.jpa.mysql.entity.UserEntity;

import java.time.LocalDate;

import static plazadecomidas.users.TestData.DomainTestData.CELL_PHONE_FIELD;
import static plazadecomidas.users.TestData.DomainTestData.DOCUMENT_NUMBER_FIELD;
import static plazadecomidas.users.TestData.DomainTestData.EMAIL_FIELD;
import static plazadecomidas.users.TestData.DomainTestData.LAST_NAME_FIELD;
import static plazadecomidas.users.TestData.DomainTestData.NAME_FIELD;
import static plazadecomidas.users.TestData.DomainTestData.PASSWORD_FIELD;

public class PersistenceTestData {

    private PersistenceTestData() { throw new IllegalStateException("Utility class"); }

    public static RoleEntity getValidRoleEntity() {
        return new RoleEntity(1L, "ROLE_ADMIN", "The user with admin access");
    }

    public static UserEntity getValidUserEntity(Long id) {
        return new UserEntity(
                id,
                NAME_FIELD.formatted(id),
                LAST_NAME_FIELD.formatted(id),
                DOCUMENT_NUMBER_FIELD.formatted(id),
                CELL_PHONE_FIELD.formatted(id),
                LocalDate.of(1994, 11, 15),
                EMAIL_FIELD.formatted(id),
                PASSWORD_FIELD.formatted(id),
                getValidRoleEntity()
                );
    }
}
