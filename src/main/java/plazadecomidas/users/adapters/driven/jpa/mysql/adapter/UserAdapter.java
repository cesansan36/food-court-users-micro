package plazadecomidas.users.adapters.driven.jpa.mysql.adapter;

import lombok.RequiredArgsConstructor;
import plazadecomidas.users.adapters.driven.jpa.mysql.entity.UserEntity;
import plazadecomidas.users.adapters.driven.jpa.mysql.exception.RegistryAlreadyExistsException;
import plazadecomidas.users.adapters.driven.jpa.mysql.exception.RegistryNotFoundException;
import plazadecomidas.users.adapters.driven.jpa.mysql.mapper.IUserEntityMapper;
import plazadecomidas.users.adapters.driven.jpa.mysql.repository.IUserRepository;
import plazadecomidas.users.adapters.driven.jpa.mysql.util.PersistenceConstants;
import plazadecomidas.users.domain.model.User;
import plazadecomidas.users.domain.secondaryport.IUserPersistencePort;

import java.util.Optional;

@RequiredArgsConstructor
public class UserAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    @Override
    public User saveUser(User user) {

        Optional<UserEntity> previousUserEntity = userRepository.findByEmail(user.getEmail());

        if (previousUserEntity.isPresent()) {
            throw new RegistryAlreadyExistsException(PersistenceConstants.USER_ALREADY_EXISTS_MESSAGE);
        }

        UserEntity userEntity = userRepository.save(userEntityMapper.userToUserEntity(user));

        return userEntityMapper.userEntityToUser(userEntity);
    }

    @Override
    public User findById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);

        if (userEntity.isEmpty()) {
            throw new RegistryNotFoundException(PersistenceConstants.USER_NOT_FOUND_MESSAGE);
        }

        return userEntityMapper.userEntityToUser(userEntity.get());
    }

    @Override
    public User findByEmail(String email) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);

        if (userEntity.isEmpty()) {
            throw new RegistryNotFoundException(PersistenceConstants.USER_NOT_FOUND_MESSAGE);
        }

        return userEntityMapper.userEntityToUser(userEntity.get());
    }

    @Override
    public String getUserPhone(Long id) {

        Optional<String> phoneNumber = userRepository.findCellPhoneNumberById(id);

        if (phoneNumber.isEmpty()) {
            throw new RegistryNotFoundException(PersistenceConstants.USER_NOT_FOUND_MESSAGE);
        }

        return phoneNumber.get();
    }

    @Override
    public String getUserEmail(Long id) {
        Optional<String> email = userRepository.findEmailById(id);

        if (email.isEmpty()) {
            throw new RegistryNotFoundException(PersistenceConstants.USER_NOT_FOUND_MESSAGE);
        }

        return email.get();
    }
}
