package plazadecomidas.users.adapters.driving.http.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import plazadecomidas.users.adapters.driving.http.rest.dto.request.AddOwnerUserRequest;
import plazadecomidas.users.adapters.driving.http.rest.dto.request.LogInRequest;
import plazadecomidas.users.adapters.driving.http.rest.dto.response.LogInResponse;
import plazadecomidas.users.adapters.driving.http.rest.dto.response.UserCreatedResponse;
import plazadecomidas.users.adapters.driving.http.rest.mapper.ILogInRequestMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.ILogInResponseMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IOwnerUserRequestMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IUserCreatedResponseMapper;
import plazadecomidas.users.adapters.driving.http.rest.util.ControllerAdapterConstants;
import plazadecomidas.users.domain.model.User;
import plazadecomidas.users.domain.primaryport.IUserServicePort;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserControllerAdapter {

    private final IUserServicePort userServicePort;
    private final IOwnerUserRequestMapper ownerUserRequestMapper;
    private final IUserCreatedResponseMapper userCreatedResponseMapper;
    private final ILogInRequestMapper logInRequestMapper;
    private final ILogInResponseMapper logInResponseMapper;

    @PostMapping("/register/owner")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserCreatedResponse> addOwnerUser(@RequestBody AddOwnerUserRequest request) {

        UserCreatedResponse response = userCreatedResponseMapper.toUserCreatedResponse(
                                        userServicePort.saveUser(
                                                ownerUserRequestMapper.addOwnerRequestToUser(request, ControllerAdapterConstants.OWNER_ROLE_ID)));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("loginUser")
    public ResponseEntity<LogInResponse> login(@RequestBody LogInRequest request) {

        LogInResponse response = logInResponseMapper.toLogInResponse(
                                    userServicePort.login(
                                            logInRequestMapper.logInRequestToUser(request)));

        return ResponseEntity.ok(response);
    }

    @GetMapping("user/verify-role")
    public ResponseEntity<Boolean> verifyRole(@RequestParam Long id, @RequestParam String role) {
        boolean isValidRole = userServicePort.validateRole(id, role);
        return ResponseEntity.ok(isValidRole);
    }
}
