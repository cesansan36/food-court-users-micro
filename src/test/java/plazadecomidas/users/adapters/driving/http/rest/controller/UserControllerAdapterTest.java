package plazadecomidas.users.adapters.driving.http.rest.controller;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import plazadecomidas.users.TestData.ControllerTestData;
import plazadecomidas.users.TestData.DomainTestData;
import plazadecomidas.users.adapters.driving.http.rest.dto.request.AddClientRequest;
import plazadecomidas.users.adapters.driving.http.rest.dto.request.AddEmployeeUserRequest;
import plazadecomidas.users.adapters.driving.http.rest.dto.request.AddOwnerUserRequest;
import plazadecomidas.users.adapters.driving.http.rest.dto.request.LogInRequest;
import plazadecomidas.users.adapters.driving.http.rest.dto.response.LogInResponse;
import plazadecomidas.users.adapters.driving.http.rest.dto.response.UserCreatedResponse;
import plazadecomidas.users.adapters.driving.http.rest.dto.response.UserEmailResponse;
import plazadecomidas.users.adapters.driving.http.rest.dto.response.UserPhoneResponse;
import plazadecomidas.users.adapters.driving.http.rest.exception.RoleMismatchException;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IClientUserRequestMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IEmployeeUserRequestMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.ILogInRequestMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.ILogInResponseMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IOwnerUserRequestMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IUserCreatedResponseMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IUserEmailResponseMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IUserPhoneResponseMapper;
import plazadecomidas.users.configuration.exceptionhandler.ControllerAdvisor;
import plazadecomidas.users.domain.model.Token;
import plazadecomidas.users.domain.model.User;
import plazadecomidas.users.domain.primaryport.IUserServicePort;
import plazadecomidas.users.util.ITokenUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerAdapterTest {

    @InjectMocks private UserControllerAdapter userControllerAdapter;

    @Mock private IUserServicePort userServicePort;
    @Mock private IOwnerUserRequestMapper ownerUserRequestMapper;
    @Mock private IEmployeeUserRequestMapper employeeUserRequestMapper;
    @Mock private IClientUserRequestMapper clientUserRequestMapper;
    @Mock private IUserCreatedResponseMapper userCreatedResponseMapper;
    @Mock private IUserPhoneResponseMapper userPhoneResponseMapper;
    @Mock private IUserEmailResponseMapper userEmailResponseMapper;
    @Mock private ILogInRequestMapper logInRequestMapper;
    @Mock private ILogInResponseMapper logInResponseMapper;
    @Mock private ITokenUtils tokenUtils;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userControllerAdapter).setControllerAdvice(new ControllerAdvisor()).build();
    }

    @Test
    @DisplayName("Add Owner User")
    void addOwnerUser() throws Exception {

        Object inputObject = new Object() {
            public final String name = "Roberto";
            public final String lastName = "Hongo";
            public final String documentNumber = "12345678";
            public final String cellPhoneNumber = "+12345678";
            public final String birthDate = LocalDate.now().toString();
            public final String email = "somemail@example.com";
            public final String password = "1234";
        };
        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(inputObject);

        Token token = new Token("token");
        UserCreatedResponse response = new UserCreatedResponse("token");

        when(ownerUserRequestMapper.addOwnerRequestToUser(any(AddOwnerUserRequest.class), anyLong())).thenReturn(DomainTestData.getValidUser(1L));
        when(userServicePort.saveUser(any(User.class))).thenReturn(token);
        when(userCreatedResponseMapper.toUserCreatedResponse(any(Token.class))).thenReturn(response);

        MockHttpServletRequestBuilder request = post("/users/register/owner")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(ownerUserRequestMapper, times(1)).addOwnerRequestToUser(any(AddOwnerUserRequest.class), anyLong());
        verify(userServicePort, times(1)).saveUser(any(User.class));
        verify(userCreatedResponseMapper, times(1)).toUserCreatedResponse(any(Token.class));
    }

    @Test
    @DisplayName("Add Employee User")
    void addEmployeeUserSuccess() throws Exception {

        Object inputObject = new Object() {
            public final String name = "Roberto";
            public final String lastName = "Hongo";
            public final String documentNumber = "12345678";
            public final String cellPhoneNumber = "+12345678";
            public final String birthDate = LocalDate.now().toString();
            public final String email = "somemail@example.com";
            public final String password = "1234";
            public final Long roleId = 3L;
            public final Long restaurantId = 2L;
        };
        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(inputObject);

        Token token = new Token("token");
        UserCreatedResponse response = new UserCreatedResponse("token");
        String headerToken = "Bearer token";
        Claim claim = ControllerTestData.getIdClaim(1L);

        when(tokenUtils.validateToken(anyString())).thenReturn(mock(DecodedJWT.class));
        when(tokenUtils.getSpecificClaim(any(DecodedJWT.class), anyString())).thenReturn(claim);
        when(employeeUserRequestMapper.addEmployeeRequestToUser(any(AddEmployeeUserRequest.class))).thenReturn(DomainTestData.getValidUser(1L));
        when(userServicePort.saveUserInBothServices(any(User.class), anyString(), anyLong(), anyLong())).thenReturn(token);
        when(userCreatedResponseMapper.toUserCreatedResponse(any(Token.class))).thenReturn(response);

        MockHttpServletRequestBuilder request = post("/users/register/employee")
                .header("Authorization", headerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        System.out.println("El token está bien =================");
        System.out.println(objectMapper.writeValueAsString(response));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(employeeUserRequestMapper, times(1)).addEmployeeRequestToUser(any(AddEmployeeUserRequest.class));
        verify(userServicePort, times(1)).saveUserInBothServices(any(User.class), anyString(), anyLong(), anyLong());
        verify(userCreatedResponseMapper, times(1)).toUserCreatedResponse(any(Token.class));
    }

    @Test
    @DisplayName("Add Employee User Fail Because Role Mismatch")
    @ExceptionHandler(IllegalArgumentException.class)
    void addEmployeeUserFailBecauseRoleMismatch() throws Exception {

        Object inputObject = new Object() {
            public final String name = "Roberto";
            public final String lastName = "Hongo";
            public final String documentNumber = "12345678";
            public final String cellPhoneNumber = "+12345678";
            public final String birthDate = LocalDate.now().toString();
            public final String email = "somemail@example.com";
            public final String password = "1234";
            public final Long roleId = 2L;
        };
        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(inputObject);

        String bearerToken = "Bearer token";

        MockHttpServletRequestBuilder request = post("/users/register/employee")
                .header("Authorization", bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(RoleMismatchException.class, result.getResolvedException()));

        verify(employeeUserRequestMapper, times(0)).addEmployeeRequestToUser(any(AddEmployeeUserRequest.class));
        verify(userServicePort, times(0)).saveUser(any(User.class));
        verify(userCreatedResponseMapper, times(0)).toUserCreatedResponse(any(Token.class));
    }

    @Test
    @DisplayName("Add Client User")
    void addClientUser() throws Exception {

        Object inputObject = new Object() {
            public final String name = "Roberto";
            public final String lastName = "Hongo";
            public final String documentNumber = "12345678";
            public final String cellPhoneNumber = "+12345678";
            public final String email = "somemail@example.com";
            public final String password = "1234";
            public final Long roleId = 4L;
        };
        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(inputObject);

        Token token = new Token("token");
        UserCreatedResponse response = new UserCreatedResponse("token");

        when(clientUserRequestMapper.addClientRequestToUser(any(AddClientRequest.class))).thenReturn(DomainTestData.getValidUser(1L));
        when(userServicePort.saveUser(any(User.class))).thenReturn(token);
        when(userCreatedResponseMapper.toUserCreatedResponse(any(Token.class))).thenReturn(response);

        MockHttpServletRequestBuilder request = post("/users/register/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(clientUserRequestMapper, times(1)).addClientRequestToUser(any(AddClientRequest.class));
        verify(userServicePort, times(1)).saveUser(any(User.class));
        verify(userCreatedResponseMapper, times(1)).toUserCreatedResponse(any(Token.class));
    }

    @Test
    @DisplayName("Add Client User Fail Because Role Mismatch")
    @ExceptionHandler(IllegalArgumentException.class)
    void addClientUserFailBecauseRoleMismatch() throws Exception {

        Object inputObject = new Object() {
            public final String name = "Roberto";
            public final String lastName = "Hongo";
            public final String documentNumber = "12345678";
            public final String cellPhoneNumber = "+12345678";
            public final String email = "somemail@example.com";
            public final String password = "1234";
            public final Long roleId = 3L;
        };
        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(inputObject);

        MockHttpServletRequestBuilder request = post("/users/register/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(RoleMismatchException.class, result.getResolvedException()));

        verify(clientUserRequestMapper, times(0)).addClientRequestToUser(any(AddClientRequest.class));
        verify(userServicePort, times(0)).saveUser(any(User.class));
        verify(userCreatedResponseMapper, times(0)).toUserCreatedResponse(any(Token.class));
    }

    @ParameterizedTest
    @DisplayName("Verify Role")
    @CsvSource({
            "1, OWNER, true",
            "2, CUSTOMER, false"
    })
    void verifyRole(Long id, String role, boolean expected) throws Exception {

        when(userServicePort.validateRole(anyLong(), anyString())).thenReturn(expected);

        String url = String.format("/users/user/verify-role?id=%s&role=%s", id, role);

        MockHttpServletRequestBuilder request = get(url);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expected)));

        verify(userServicePort, times(1)).validateRole(anyLong(), anyString());
    }

    @Test
    @DisplayName("Login User")
    void loginUser() throws Exception {
        Object inputObject = new Object() {
            public final String email = "somemail@example.com";
            public final String password = "1234";
        };
        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(inputObject);

        User user = mock(User.class);
        Token token = mock(Token.class);
        LogInResponse response = mock(LogInResponse.class);

        when(logInRequestMapper.logInRequestToUser(any(LogInRequest.class))).thenReturn(user);
        when(userServicePort.login(any(User.class))).thenReturn(token);
        when(logInResponseMapper.toLogInResponse(any(Token.class))).thenReturn(response);

        MockHttpServletRequestBuilder request = post("/users/loginUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(logInRequestMapper, times(1)).logInRequestToUser(any(LogInRequest.class));
        verify(userServicePort, times(1)).login(any(User.class));
        verify(logInResponseMapper, times(1)).toLogInResponse(any(Token.class));
    }

    @Test
    void getNumber() throws Exception {
        Long id = 1L;
        UserPhoneResponse response = new UserPhoneResponse("123456789");
        String phone = "123456789";

        when(userServicePort.getUserPhone(anyLong())).thenReturn(phone);
        when(userPhoneResponseMapper.toUserPhoneResponse(anyString())).thenReturn(response);

        String url = String.format("/users/get-number?id=%s", id);

        MockHttpServletRequestBuilder request = get(url);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(phone));

        verify(userServicePort, times(1)).getUserPhone(anyLong());
        verify(userPhoneResponseMapper, times(1)).toUserPhoneResponse(anyString());
    }

    @Test
    void getEmail() throws Exception {
        String bearerToken = "Bearer token";
        String email = "somemail@example.com";
        UserEmailResponse response = new UserEmailResponse(email);
        Long id = 1L;

        Claim claim = ControllerTestData.getIdClaim(id);

        when(userServicePort.getUserEmail(anyLong())).thenReturn(email);
        when(tokenUtils.validateToken(anyString())).thenReturn(mock(DecodedJWT.class));
        when(tokenUtils.getSpecificClaim(any(DecodedJWT.class), anyString())).thenReturn(claim);
        when(userEmailResponseMapper.toUserEmailResponse(anyString())).thenReturn(response);

        MockHttpServletRequestBuilder request = get("/users/get-email")
                                                .header(HttpHeaders.AUTHORIZATION, bearerToken);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(email));

        verify(userServicePort, times(1)).getUserEmail(anyLong());
        verify(tokenUtils, times(1)).validateToken(anyString());
        verify(tokenUtils, times(1)).getSpecificClaim(any(DecodedJWT.class), anyString());
        verify(userEmailResponseMapper, times(1)).toUserEmailResponse(anyString());
    }
}