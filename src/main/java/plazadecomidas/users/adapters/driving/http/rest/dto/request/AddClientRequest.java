package plazadecomidas.users.adapters.driving.http.rest.dto.request;

public record AddClientRequest(
        String name,
        String lastName,
        String documentNumber,
        String cellPhoneNumber,
        String email,
        String password,
        Long roleId
) {
}
