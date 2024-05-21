package plazadecomidas.users.domain.secondaryport;

import plazadecomidas.users.domain.model.Role;

public interface IRolePersistencePort {
    Role findById(Long id);
}
