package plazadecomidas.users.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import plazadecomidas.users.adapters.driving.http.rest.dto.response.UserPhoneResponse;

@Mapper(componentModel = "spring")
public interface IUserPhoneResponseMapper {

    @Mapping(target = "phoneNumber", source = "userPhone")
    UserPhoneResponse toUserPhoneResponse(String userPhone);
}
