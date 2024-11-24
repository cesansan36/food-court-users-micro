package plazadecomidas.users.adapters.driven.jpa.mysql.mapper;

import org.mapstruct.Mapper;
import plazadecomidas.users.adapters.driven.jpa.mysql.entity.RoleEntity;
import plazadecomidas.users.domain.model.Role;

@Mapper(componentModel = "spring")
public interface IRoleEntityMapper {

    public RoleEntity roleToRoleEntity(Role role);

    public Role roleEntityToRole(RoleEntity roleEntity);
}
