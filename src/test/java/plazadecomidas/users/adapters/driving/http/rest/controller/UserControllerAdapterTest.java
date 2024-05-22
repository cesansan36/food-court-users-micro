package plazadecomidas.users.adapters.driving.http.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import plazadecomidas.users.TestData.DomainTestData;
import plazadecomidas.users.adapters.driving.http.rest.dto.request.AddClientRequest;
import plazadecomidas.users.adapters.driving.http.rest.dto.request.AddEmployeeUserRequest;
import plazadecomidas.users.adapters.driving.http.rest.dto.request.AddOwnerUserRequest;
import plazadecomidas.users.adapters.driving.http.rest.dto.request.LogInRequest;
import plazadecomidas.users.adapters.driving.http.rest.dto.response.LogInResponse;
import plazadecomidas.users.adapters.driving.http.rest.dto.response.UserCreatedResponse;
import plazadecomidas.users.adapters.driving.http.rest.exception.RoleMismatchException;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IClientUserRequestMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IEmployeeUserRequestMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.ILogInRequestMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.ILogInResponseMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IOwnerUserRequestMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IUserCreatedResponseMapper;
import plazadecomidas.users.configuration.exceptionhandler.ControllerAdvisor;
import plazadecomidas.users.domain.model.Token;
import plazadecomidas.users.domain.model.User;
import plazadecomidas.users.domain.primaryport.IUserServicePort;

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

class UserControllerAdapterTest {

    private UserControllerAdapter userControllerAdapter;

    private IUserServicePort userServicePort;
    private IOwnerUserRequestMapper ownerUserRequestMapper;
    private IEmployeeUserRequestMapper employeeUserRequestMapper;
    private IClientUserRequestMapper clientUserRequestMapper;
    private IUserCreatedResponseMapper userCreatedResponseMapper;
    private ILogInRequestMapper logInRequestMapper;
    private ILogInResponseMapper logInResponseMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        userServicePort = mock(IUserServicePort.class);
        ownerUserRequestMapper = mock(IOwnerUserRequestMapper.class);
        employeeUserRequestMapper = mock(IEmployeeUserRequestMapper.class);
        clientUserRequestMapper = mock(IClientUserRequestMapper.class);
        userCreatedResponseMapper = mock(IUserCreatedResponseMapper.class);
        logInRequestMapper = mock(ILogInRequestMapper.class);
        logInResponseMapper = mock(ILogInResponseMapper.class);
        userControllerAdapter = new UserControllerAdapter(userServicePort, ownerUserRequestMapper, employeeUserRequestMapper, clientUserRequestMapper, userCreatedResponseMapper, logInRequestMapper, logInResponseMapper);

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
        };
        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(inputObject);

        Token token = new Token("token");
        UserCreatedResponse response = new UserCreatedResponse("token");

        when(employeeUserRequestMapper.addEmployeeRequestToUser(any(AddEmployeeUserRequest.class))).thenReturn(DomainTestData.getValidUser(1L));
        when(userServicePort.saveUser(any(User.class))).thenReturn(token);
        when(userCreatedResponseMapper.toUserCreatedResponse(any(Token.class))).thenReturn(response);

        MockHttpServletRequestBuilder request = post("/users/register/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(employeeUserRequestMapper, times(1)).addEmployeeRequestToUser(any(AddEmployeeUserRequest.class));
        verify(userServicePort, times(1)).saveUser(any(User.class));
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

        MockHttpServletRequestBuilder request = post("/users/register/employee")
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
}