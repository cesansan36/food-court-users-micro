package plazadecomidas.users.domain.secondaryport;

public interface IRestaurantConnectionPort {
    void saveUser(String ownerToken,Long ownerId, Long employeeId, Long restaurantId);
}
