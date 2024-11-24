package plazadecomidas.users.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import plazadecomidas.users.adapters.driving.http.rest.dto.response.UserEmailResponse;

@Mapper(componentModel = "spring")
public interface IUserEmailResponseMapper {

    @Mapping(target = "email", source = "userEmail")
    UserEmailResponse toUserEmailResponse(String userEmail);
}
