package plazadecomidas.users.adapters.driving.http.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class AddOwnerUserRequest {
    String name;
    String lastName;
    String documentNumber;
    String cellPhoneNumber;
    LocalDate birthDate;
    String email;
    String password;
}
