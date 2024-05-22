package plazadecomidas.users.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import plazadecomidas.users.adapters.driving.http.rest.dto.request.AddClientRequest;
import plazadecomidas.users.domain.model.Role;
import plazadecomidas.users.domain.model.User;

@Mapper(componentModel = "spring")
public interface IClientUserRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "birthDate", constant = "1900-01-01")
    @Mapping(target = "role", source = "roleId" , qualifiedByName = "mapRole")
    User addClientRequestToUser(AddClientRequest request);

    @Named("mapRole")
    default Role mapRole(Long roleId) {return new Role(roleId, "dummy", "dummy");}
}
