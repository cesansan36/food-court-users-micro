package plazadecomidas.users.adapters.driven.jpa.mysql.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import plazadecomidas.users.adapters.driven.jpa.mysql.entity.UserEntity;
import plazadecomidas.users.domain.model.User;

@Mapper(componentModel = "spring")
public interface IUserEntityMapper {

    @Mapping(target = "roleEntity", source = "role")
    public UserEntity userToUserEntity(User user);

    @Mapping(target = "role", source = "roleEntity")
    public User userEntityToUser(UserEntity userEntity);
}
