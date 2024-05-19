package plazadecomidas.users.domain.secondaryport;

import plazadecomidas.users.domain.model.User;

public interface IUserPersistencePort {

    User saveUser(User user);
}
