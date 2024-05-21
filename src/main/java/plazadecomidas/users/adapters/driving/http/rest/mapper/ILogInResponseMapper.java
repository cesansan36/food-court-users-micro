package plazadecomidas.users.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import plazadecomidas.users.adapters.driving.http.rest.dto.response.LogInResponse;
import plazadecomidas.users.domain.model.Token;

@Mapper(componentModel = "spring")
public interface ILogInResponseMapper {

    @Mapping(target = "token", source = "value")
    public LogInResponse toLogInResponse(Token token);
}
