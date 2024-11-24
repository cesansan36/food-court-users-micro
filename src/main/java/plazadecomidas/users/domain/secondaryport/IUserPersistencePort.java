package plazadecomidas.users.domain.secondaryport;

import plazadecomidas.users.domain.model.User;

public interface IUserPersistencePort {

    User saveUser(User user);

    User findById(Long id);

    User findByEmail(String email);

    String getUserPhone(Long id);

    String getUserEmail(Long id);
}
