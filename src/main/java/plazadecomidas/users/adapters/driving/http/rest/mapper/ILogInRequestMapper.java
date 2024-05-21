package plazadecomidas.users.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import plazadecomidas.users.adapters.driving.http.rest.dto.request.LogInRequest;
import plazadecomidas.users.domain.model.User;

@Mapper(componentModel = "spring")
public interface ILogInRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", constant = "Dummy")
    @Mapping(target = "lastName", constant = "Dummy")
    @Mapping(target = "documentNumber", constant = "0000")
    @Mapping(target = "cellPhoneNumber", constant = "0000")
    @Mapping(target = "birthDate", constant = "2000-01-01")
    @Mapping(target = "role", expression = "java(new Role(1L, \"Dummy\", \"Dummy\"))")
    User logInRequestToUser(LogInRequest request);
}
