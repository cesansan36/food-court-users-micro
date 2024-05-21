package plazadecomidas.users.domain.secondaryport;

import plazadecomidas.users.domain.model.User;

public interface IUserAuthentication {
    String login(User user);

    String createToken(User savedUser);
}
