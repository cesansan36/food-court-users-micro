package plazadecomidas.users.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import plazadecomidas.users.adapters.driving.http.rest.dto.response.LogInResponse;
import plazadecomidas.users.domain.model.Token;

@Mapper(componentModel = "spring")
public class ILogInResponseMapper {

    public LogInResponse toLogInResponse(Token token) {
        return new LogInResponse(token.getValue());
    }
}
