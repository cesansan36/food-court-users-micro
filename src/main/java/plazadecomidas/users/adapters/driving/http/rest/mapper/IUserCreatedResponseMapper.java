package plazadecomidas.users.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import plazadecomidas.users.adapters.driving.http.rest.dto.response.UserCreatedResponse;
import plazadecomidas.users.domain.model.Token;

@Mapper(componentModel = "spring")
public interface IUserCreatedResponseMapper {

    @Mapping(source = "value", target = "token")
    UserCreatedResponse toUserCreatedResponse(Token token);
}
