package plazadecomidas.users.adapters.driven.jpa.mysql.adapter;

import lombok.RequiredArgsConstructor;
import plazadecomidas.users.adapters.driven.jpa.mysql.entity.RoleEntity;
import plazadecomidas.users.adapters.driven.jpa.mysql.exception.RegistryNotFoundException;
import plazadecomidas.users.adapters.driven.jpa.mysql.mapper.IRoleEntityMapper;
import plazadecomidas.users.adapters.driven.jpa.mysql.repository.IRoleRepository;
import plazadecomidas.users.adapters.driven.jpa.mysql.util.PersistenceConstants;
import plazadecomidas.users.domain.model.Role;
import plazadecomidas.users.domain.secondaryport.IRolePersistencePort;

import java.util.Optional;

@RequiredArgsConstructor
public class RoleAdapter implements IRolePersistencePort {

    private final IRoleEntityMapper roleEntityMapper;
    private final IRoleRepository roleRepository;

    @Override
    public Role findById(Long id) {
        Optional<RoleEntity> role = roleRepository.findById(id);

        if (role.isEmpty()) {
            throw new RegistryNotFoundException(PersistenceConstants.ROLE_NOT_FOUND_MESSAGE);
        }

        return roleEntityMapper.roleEntityToRole(role.get());
    }
}
