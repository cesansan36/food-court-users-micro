package plazadecomidas.users.domain.primaryport;

import plazadecomidas.users.domain.model.Token;
import plazadecomidas.users.domain.model.User;

public interface IUserServicePort {


    boolean validateRole (Long id, String role);

    Token login(User user);

    Token saveUser(User user);

    Token saveUserInBothServices(User user, String ownerToken, Long ownerId, Long restaurantId);
}
