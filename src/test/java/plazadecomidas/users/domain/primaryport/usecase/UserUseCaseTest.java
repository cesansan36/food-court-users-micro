package plazadecomidas.users.domain.primaryport.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import plazadecomidas.users.TestData.DomainTestData;
import plazadecomidas.users.domain.model.Token;
import plazadecomidas.users.domain.model.User;
import plazadecomidas.users.domain.secondaryport.IUserPersistencePort;

import static org.junit.jupiter.api.Assertions.*;
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
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userPersistencePort = mock(IUserPersistencePort.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userUseCase = new UserUseCase(userPersistencePort, passwordEncoder);
    }

    @Test
    void saveUser() {
        String encodedPassword = "password";
        User user = DomainTestData.getValidUser(1L);

        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userPersistencePort.saveUser(any(User.class))).thenReturn(user);

        Token token = userUseCase.saveUser(user);

        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userPersistencePort, times(1)).saveUser(any(User.class));
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
}