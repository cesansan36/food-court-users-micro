package plazadecomidas.users.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import plazadecomidas.users.adapters.driving.http.rest.dto.request.AddOwnerUserRequest;
import plazadecomidas.users.domain.model.Role;
import plazadecomidas.users.domain.model.User;


@Mapper(componentModel = "spring")
public interface IOwnerUserRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "roleId", target = "role", qualifiedByName = "mapRole")
    User addOwnerRequestToUser(AddOwnerUserRequest request, Long roleId);

    @Named("mapRole")
    default Role mapRole(Long roleId) {return new Role(roleId, "dummy", "dummy");}
}
