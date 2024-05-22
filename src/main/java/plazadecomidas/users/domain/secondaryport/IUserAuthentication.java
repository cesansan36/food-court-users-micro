package plazadecomidas.users.domain.secondaryport;

import plazadecomidas.users.domain.model.User;

public interface IUserAuthentication {
    String login(User user, Long idUser);

    String createToken(User savedUser);
}
