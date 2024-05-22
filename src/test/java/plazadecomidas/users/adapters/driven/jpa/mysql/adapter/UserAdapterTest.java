package plazadecomidas.users.adapters.driven.jpa.mysql.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import plazadecomidas.users.TestData.DomainTestData;
import plazadecomidas.users.TestData.PersistenceTestData;
import plazadecomidas.users.adapters.driven.jpa.mysql.entity.UserEntity;
import plazadecomidas.users.adapters.driven.jpa.mysql.exception.RegistryAlreadyExistsException;
import plazadecomidas.users.adapters.driven.jpa.mysql.exception.RegistryNotFoundException;
import plazadecomidas.users.adapters.driven.jpa.mysql.mapper.IUserEntityMapper;
import plazadecomidas.users.adapters.driven.jpa.mysql.repository.IUserRepository;
import plazadecomidas.users.domain.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserAdapterTest {

    private UserAdapter userAdapter;

    private IUserRepository userRepository;
    private IUserEntityMapper userEntityMapper;

    @BeforeEach
    void setUp() {
        userRepository = mock(IUserRepository.class);
        userEntityMapper = mock(IUserEntityMapper.class);
        userAdapter = new UserAdapter(userRepository, userEntityMapper);
    }

    @Test
    @DisplayName("Should save user successfully")
    void shouldSaveUserSuccessfully() {

        UserEntity userEntity = PersistenceTestData.getValidUserEntity(1L);
        User user = DomainTestData.getValidUser(1L);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userEntityMapper.userToUserEntity(any(User.class))).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userEntityMapper.userEntityToUser(any(UserEntity.class))).thenReturn(user);

        User savedUser = userAdapter.saveUser(user);

        assertAll(
                () -> assertNotNull(savedUser),
                () -> assertEquals(userEntity.getId(), savedUser.getId()),
                () -> assertEquals(userEntity.getName(), savedUser.getName()),
                () -> assertEquals(userEntity.getLastName(), savedUser.getLastName()),
                () -> assertEquals(userEntity.getDocumentNumber(), savedUser.getDocumentNumber()),
                () -> assertEquals(userEntity.getCellPhoneNumber(), savedUser.getCellPhoneNumber()),
                () -> assertEquals(userEntity.getBirthDate(), savedUser.getBirthDate()),
                () -> assertEquals(userEntity.getEmail(), savedUser.getEmail()),
                () -> assertEquals(userEntity.getPassword(), savedUser.getPassword()),
                () -> assertEquals(userEntity.getRoleEntity().getId(), savedUser.getRole().getId()),
                () -> assertEquals(userEntity.getRoleEntity().getName(), savedUser.getRole().getName()),
                () -> assertEquals(userEntity.getRoleEntity().getDescription(), savedUser.getRole().getDescription()),
                () -> verify(userRepository, times(1)).findByEmail(anyString()),
                () -> verify(userEntityMapper, times(1)).userToUserEntity(any(User.class)),
                () -> verify(userRepository, times(1)).save(any(UserEntity.class)),
                () -> verify(userEntityMapper, times(1)).userEntityToUser(any(UserEntity.class))
        );
    }

    @Test
    @DisplayName("Should throw exception when user already exists")
    void shouldThrowExceptionWhenUserAlreadyExists() {

        UserEntity userEntity = PersistenceTestData.getValidUserEntity(1L);
        User user = DomainTestData.getValidUser(1L);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));

        assertThrows(RegistryAlreadyExistsException.class, () -> userAdapter.saveUser(user));
    }

    @Test
    @DisplayName("Should find user by id")
    void shouldFindUserById() {
        UserEntity userEntity = PersistenceTestData.getValidUserEntity(1L);
        User user = DomainTestData.getValidUser(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(userEntityMapper.userEntityToUser(any(UserEntity.class))).thenReturn(user);

        User foundUser = userAdapter.findById(1L);

        assertAll(
                () -> assertNotNull(foundUser),
                () -> assertEquals(userEntity.getId(), foundUser.getId()),
                () -> assertEquals(userEntity.getName(), foundUser.getName()),
                () -> assertEquals(userEntity.getLastName(), foundUser.getLastName()),
                () -> assertEquals(userEntity.getDocumentNumber(), foundUser.getDocumentNumber()),
                () -> assertEquals(userEntity.getCellPhoneNumber(), foundUser.getCellPhoneNumber()),
                () -> assertEquals(userEntity.getBirthDate(), foundUser.getBirthDate()),
                () -> assertEquals(userEntity.getEmail(), foundUser.getEmail()),
                () -> assertEquals(userEntity.getPassword(), foundUser.getPassword()),
                () -> assertEquals(userEntity.getRoleEntity().getId(), foundUser.getRole().getId()),
                () -> assertEquals(userEntity.getRoleEntity().getName(), foundUser.getRole().getName()),
                () -> assertEquals(userEntity.getRoleEntity().getDescription(), foundUser.getRole().getDescription()),
                () -> verify(userRepository, times(1)).findById(anyLong()),
                () -> verify(userEntityMapper, times(1)).userEntityToUser(any(UserEntity.class))
        );
    }

    @Test
    @DisplayName("Should fail on not user found by id")
    void shouldFailOnNotFoundId() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RegistryNotFoundException.class, () -> userAdapter.findById(1L));
    }

    @Test
    @DisplayName("Should find user by email")
    void shouldFindUserByEmail() {
        UserEntity userEntity = PersistenceTestData.getValidUserEntity(1L);
        User user = DomainTestData.getValidUser(1L);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));
        when(userEntityMapper.userEntityToUser(any(UserEntity.class))).thenReturn(user);

        User foundUser = userAdapter.findByEmail("xXn8z@example.com");

        assertAll(
                () -> assertNotNull(foundUser),
                () -> assertEquals(userEntity.getId(), foundUser.getId()),
                () -> assertEquals(userEntity.getName(), foundUser.getName()),
                () -> assertEquals(userEntity.getLastName(), foundUser.getLastName()),
                () -> assertEquals(userEntity.getDocumentNumber(), foundUser.getDocumentNumber()),
                () -> assertEquals(userEntity.getCellPhoneNumber(), foundUser.getCellPhoneNumber()),
                () -> assertEquals(userEntity.getBirthDate(), foundUser.getBirthDate()),
                () -> assertEquals(userEntity.getEmail(), foundUser.getEmail()),
                () -> assertEquals(userEntity.getPassword(), foundUser.getPassword()),
                () -> assertEquals(userEntity.getRoleEntity().getId(), foundUser.getRole().getId()),
                () -> assertEquals(userEntity.getRoleEntity().getName(), foundUser.getRole().getName()),
                () -> assertEquals(userEntity.getRoleEntity().getDescription(), foundUser.getRole().getDescription()),
                () -> verify(userRepository, times(1)).findByEmail(anyString()),
                () -> verify(userEntityMapper, times(1)).userEntityToUser(any(UserEntity.class))
        );
    }

    @Test
    @DisplayName("Should fail on not user found by email")
    void shouldFailOnNotFoundEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(RegistryNotFoundException.class, () -> userAdapter.findByEmail("xXn8z@example.com"));
    }
}