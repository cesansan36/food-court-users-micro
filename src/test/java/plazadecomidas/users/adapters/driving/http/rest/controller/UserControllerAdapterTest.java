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
import plazadecomidas.users.TestData.DomainTestData;
import plazadecomidas.users.adapters.driving.http.rest.dto.request.AddOwnerUserRequest;
import plazadecomidas.users.adapters.driving.http.rest.dto.response.UserCreatedResponse;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IOwnerUserRequestMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IUserCreatedResponseMapper;
import plazadecomidas.users.domain.model.Token;
import plazadecomidas.users.domain.model.User;
import plazadecomidas.users.domain.primaryport.IUserServicePort;

import java.time.LocalDate;

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
    private IUserCreatedResponseMapper userCreatedResponseMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        userServicePort = mock(IUserServicePort.class);
        ownerUserRequestMapper = mock(IOwnerUserRequestMapper.class);
        userCreatedResponseMapper = mock(IUserCreatedResponseMapper.class);
        userControllerAdapter = new UserControllerAdapter(userServicePort, ownerUserRequestMapper, userCreatedResponseMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(userControllerAdapter).build();
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
}