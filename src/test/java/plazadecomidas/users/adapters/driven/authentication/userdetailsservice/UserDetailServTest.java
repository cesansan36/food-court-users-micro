package plazadecomidas.users.adapters.driven.authentication.userdetailsservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import plazadecomidas.users.TestData.DomainTestData;
import plazadecomidas.users.TestData.PersistenceTestData;
import plazadecomidas.users.adapters.driven.jpa.mysql.entity.UserEntity;
import plazadecomidas.users.adapters.driven.jpa.mysql.repository.IUserRepository;
import plazadecomidas.users.domain.model.User;
import plazadecomidas.users.util.ITokenUtils;

import java.util.Optional;

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

class UserDetailServTest {

    UserDetailServ userDetailServ;

    private IUserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ITokenUtils tokenUtils;

    @BeforeEach
    void setUp() {
        userRepository = mock(IUserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        tokenUtils = mock(ITokenUtils.class);
        userDetailServ = new UserDetailServ(userRepository, passwordEncoder, tokenUtils);
    }

    @Test
    void loadUserByUsername() {

        UserEntity userEntity = PersistenceTestData.getValidUserEntity(1L);

        when(userRepository.findByEmail("user1")).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = userDetailServ.loadUserByUsername("user1");

        assertNotNull(userDetails);

        assertEquals(userEntity.getEmail(), userDetails.getUsername());
        assertEquals(userEntity.getPassword(), userDetails.getPassword());
    }

    @Test
    void loadUserByUsernameNotFound() {

        when(userRepository.findByEmail("user1")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailServ.loadUserByUsername("user1"));

        verify(userRepository, times(1)).findByEmail("user1");

    }

    @Test
    void authenticate() {
        UserEntity userEntity = PersistenceTestData.getValidUserEntity(1L);

        when(userRepository.findByEmail("user1")).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("12341", userEntity.getPassword())).thenReturn(true);

        Authentication authentication = userDetailServ.authenticate("user1", "12341");

        assertNotNull(authentication);

        verify(userRepository, times(1)).findByEmail("user1");
        verify(passwordEncoder, times(1)).matches("12341", userEntity.getPassword());
    }
    @Test
    void authenticateFail() {
        UserEntity userEntity = PersistenceTestData.getValidUserEntity(1L);

        when(userRepository.findByEmail("user1")).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("12341", userEntity.getPassword())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> userDetailServ.authenticate("user1", "12341"));

        verify(userRepository, times(1)).findByEmail("user1");
        verify(passwordEncoder, times(1)).matches("12341", userEntity.getPassword());
    }

    @Test
    void logInUser() {
        UserEntity userEntity = PersistenceTestData.getValidUserEntity(1L);
        String token = "token";

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenUtils.createToken(any(Authentication.class), anyLong())).thenReturn(token);

        String result = userDetailServ.logInUser("user1", "12341", 1L);

        assertNotNull(result);
        assertEquals(token, result);

        verify(userRepository, times(1)).findByEmail("user1");
        verify(passwordEncoder, times(1)).matches("12341", userEntity.getPassword());
        verify(tokenUtils, times(1)).createToken(any(Authentication.class), anyLong());
    }

    @Test
    void createUser() {
        String token = "token";

        when(tokenUtils.createToken(any(Authentication.class), anyLong())).thenReturn(token);

        String result = userDetailServ.createUser("user1", "12341", "ROLE_ADMIN", 1L);

        assertNotNull(result);
        assertEquals(token, result);

        verify(tokenUtils, times(1)).createToken(any(Authentication.class), anyLong());
    }

    @Test
    void login() {
        String token = "token";
        User user = DomainTestData.getValidUser(1L);
        UserEntity userEntity = PersistenceTestData.getValidUserEntity(1L);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenUtils.createToken(any(Authentication.class), anyLong())).thenReturn(token);

        String result = userDetailServ.login(user, 1L);

        assertNotNull(result);
        assertEquals(token, result);

        verify(userRepository, times(1)).findByEmail("email1@example.com");
        verify(passwordEncoder, times(1)).matches("12341", userEntity.getPassword());
        verify(tokenUtils, times(1)).createToken(any(Authentication.class), anyLong());
    }

    @Test
    void createToken() {
        String token = "token";

        when(tokenUtils.createToken(any(Authentication.class), anyLong())).thenReturn(token);

        String result = userDetailServ.createToken(DomainTestData.getValidUser(1L));

        assertNotNull(result);
        assertEquals(token, result);

        verify(tokenUtils, times(1)).createToken(any(Authentication.class), anyLong());
    }
}