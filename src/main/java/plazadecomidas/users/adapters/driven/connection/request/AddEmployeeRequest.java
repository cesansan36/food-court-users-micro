package plazadecomidas.users.adapters.driven.connection.request;

public record AddEmployeeRequest(
        Long idOwner,
        Long idEmployee,
        Long idRestaurant
) {
}
