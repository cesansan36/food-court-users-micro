package plazadecomidas.users.adapters.driving.http.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LogInRequest {

    private String email;
    private String password;
}
