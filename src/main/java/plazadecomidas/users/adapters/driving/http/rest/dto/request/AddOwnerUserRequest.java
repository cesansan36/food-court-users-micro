package plazadecomidas.users.adapters.driving.http.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class AddOwnerUserRequest {

    private String name;
    private String lastName;
    private String documentNumber;
    private String cellPhoneNumber;
    private LocalDate birthDate;
    private String email;
    private String password;
}
