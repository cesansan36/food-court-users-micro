package plazadecomidas.users.adapters.driving.http.rest.dto.request;

import java.time.LocalDate;

public record AddEmployeeUserRequest(
    String name,
    String lastName,
    String documentNumber,
    String cellPhoneNumber,
    LocalDate birthDate,
    String email,
    String password,
    Long roleId
) {}
