package plazadecomidas.users.domain.primaryport;

import plazadecomidas.users.domain.model.Token;
import plazadecomidas.users.domain.model.User;

public interface IUserServicePort {

    Token saveUser (User user);
}
