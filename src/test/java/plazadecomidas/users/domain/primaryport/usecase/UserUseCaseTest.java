package plazadecomidas.users.domain.primaryport.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plazadecomidas.users.TestData.DomainTestData;
import plazadecomidas.users.domain.model.Role;
import plazadecomidas.users.domain.model.Token;
import plazadecomidas.users.domain.model.User;
import plazadecomidas.users.domain.secondaryport.IPasswordEncoderPort;
import plazadecomidas.users.domain.secondaryport.IRestaurantConnectionPort;
import plazadecomidas.users.domain.secondaryport.IRolePersistencePort;
import plazadecomidas.users.domain.secondaryport.IUserAuthentication;
import plazadecomidas.users.domain.secondaryport.IUserPersistencePort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserUseCaseTest {

    private UserUseCase userUseCase;

    private IUserPersistencePort userPersistencePort;
    private IRolePersistencePort rolePersistencePort;
    private IPasswordEncoderPort passwordEncoderPort;
    private IUserAuthentication userAuthentication;
    private IRestaurantConnectionPort restaurantConnectionPort;

    @BeforeEach
    void setUp() {
        userPersistencePort = mock(IUserPersistencePort.class);
        rolePersistencePort = mock(IRolePersistencePort.class);
        passwordEncoderPort = mock(IPasswordEncoderPort.class);
        userAuthentication = mock(IUserAuthentication.class);
        restaurantConnectionPort = mock(IRestaurantConnectionPort.class);
        userUseCase = new UserUseCase(userPersistencePort, rolePersistencePort, passwordEncoderPort, userAuthentication, restaurantConnectionPort);
    }

    @Test
    void saveUser() {
        String encodedPassword = "password";
        User user = DomainTestData.getValidUser(1L);
        Role role = DomainTestData.getValidRole();
        String tokenString = "token";

        when(rolePersistencePort.findById(anyLong())).thenReturn(role);
        when(passwordEncoderPort.encode(anyString())).thenReturn(encodedPassword);
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);
        when(userAuthentication.createToken(any(User.class))).thenReturn(tokenString);

        Token token = userUseCase.saveUser(user);

        verify(passwordEncoderPort, times(1)).encode(anyString());
        verify(userPersistencePort, times(1)).saveUser(any(User.class));
        verify(rolePersistencePort, times(1)).findById(anyLong());
        verify(userAuthentication, times(1)).createToken(any(User.class));
        assertNotNull(token);
    }

    @Test
    void validateRole() {
        User user = DomainTestData.getValidUser(1L);
        when(userPersistencePort.findById(anyLong())).thenReturn(user);
        boolean result = userUseCase.validateRole(user.getId(), user.getRole().getName());

        verify(userPersistencePort, times(1)).findById(anyLong());
        assertTrue(result);
    }

    @Test
    void login() {
        User user = DomainTestData.getValidUser(1L);
        String tokenString = "token";

        when(userPersistencePort.findByEmail(anyString())).thenReturn(user);
        when(userAuthentication.login(any(User.class), anyLong())).thenReturn(tokenString);

        Token token = userUseCase.login(user);

        verify(userAuthentication, times(1)).login(any(User.class), anyLong());
        assertNotNull(token);
        assertEquals(tokenString, token.getValue());
    }

    @Test
    void saveUserInBothServices() {
        String encodedPassword = "password";
        User user = DomainTestData.getValidUser(1L);
        Role role = DomainTestData.getValidRole();
        String ownerToken = "ownerToken";
        String tokenString = "token";

        when(rolePersistencePort.findById(anyLong())).thenReturn(role);
        when(passwordEncoderPort.encode(anyString())).thenReturn(encodedPassword);
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);
        when(userAuthentication.createToken(any(User.class))).thenReturn(tokenString);

        Token token = userUseCase.saveUserInBothServices(user, ownerToken, 1L, 1L);

        verify(passwordEncoderPort, times(1)).encode(anyString());
        verify(userPersistencePort, times(1)).saveUser(any(User.class));
        verify(rolePersistencePort, times(1)).findById(anyLong());
        verify(userAuthentication, times(1)).createToken(any(User.class));
        assertNotNull(token);
    }

    @Test
    void getUserPhone() {
        Long id = 1L;
        String phone = "123456789";

        when(userPersistencePort.getUserPhone(anyLong())).thenReturn(phone);

        String result = userUseCase.getUserPhone(id);

        verify(userPersistencePort, times(1)).getUserPhone(anyLong());
        assertEquals(phone, result);

    }

    @Test
    void getUserEmail() {
        Long id = 1L;
        String email = "email@somemail.com";

        when(userPersistencePort.getUserEmail(anyLong())).thenReturn(email);

        String result = userUseCase.getUserEmail(id);

        verify(userPersistencePort, times(1)).getUserEmail(anyLong());
        assertEquals(email, result);

    }
}