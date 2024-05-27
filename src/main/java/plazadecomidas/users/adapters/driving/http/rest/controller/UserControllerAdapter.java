package plazadecomidas.users.adapters.driving.http.rest.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import plazadecomidas.users.adapters.driving.http.rest.dto.request.AddClientRequest;
import plazadecomidas.users.adapters.driving.http.rest.dto.request.AddEmployeeUserRequest;
import plazadecomidas.users.adapters.driving.http.rest.dto.request.AddOwnerUserRequest;
import plazadecomidas.users.adapters.driving.http.rest.dto.request.LogInRequest;
import plazadecomidas.users.adapters.driving.http.rest.dto.response.LogInResponse;
import plazadecomidas.users.adapters.driving.http.rest.dto.response.UserCreatedResponse;
import plazadecomidas.users.adapters.driving.http.rest.dto.response.UserPhoneResponse;
import plazadecomidas.users.adapters.driving.http.rest.exception.RoleMismatchException;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IClientUserRequestMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IEmployeeUserRequestMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.ILogInRequestMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.ILogInResponseMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IOwnerUserRequestMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IUserCreatedResponseMapper;
import plazadecomidas.users.adapters.driving.http.rest.mapper.IUserPhoneResponseMapper;
import plazadecomidas.users.adapters.driving.http.rest.util.ControllerAdapterConstants;
import plazadecomidas.users.domain.primaryport.IUserServicePort;
import plazadecomidas.users.util.ITokenUtils;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserControllerAdapter {

    private final IUserServicePort userServicePort;
    private final IOwnerUserRequestMapper ownerUserRequestMapper;
    private final IEmployeeUserRequestMapper employeeUserRequestMapper;
    private final IClientUserRequestMapper clientUserRequestMapper;
    private final IUserCreatedResponseMapper userCreatedResponseMapper;
    private final IUserPhoneResponseMapper userPhoneResponseMapper;
    private final ILogInRequestMapper logInRequestMapper;
    private final ILogInResponseMapper logInResponseMapper;
    private final ITokenUtils tokenUtils;

    @PostMapping("/register/owner")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserCreatedResponse> addOwnerUser(@RequestBody AddOwnerUserRequest request) {

        UserCreatedResponse response = userCreatedResponseMapper.toUserCreatedResponse(
                                        userServicePort.saveUser(
                                                ownerUserRequestMapper.addOwnerRequestToUser(request, ControllerAdapterConstants.OWNER_ROLE_ID)));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("register/employee")
    @PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserCreatedResponse> addEmployeeUser(@RequestHeader(value = "Authorization") String token, @RequestBody AddEmployeeUserRequest request) {

        if (!ControllerAdapterConstants.EMPLOYEE_ROLE_ID.equals(request.roleId())) {
            throw new RoleMismatchException(ControllerAdapterConstants.ROLE_MISMATCH_MESSAGE);
        }
        Long ownerId = extractToken(token);

        UserCreatedResponse response = userCreatedResponseMapper.toUserCreatedResponse(
                                        userServicePort.saveUserInBothServices(
                                                employeeUserRequestMapper.addEmployeeRequestToUser(request), token, ownerId, request.restaurantId()));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("loginUser")
    public ResponseEntity<LogInResponse> login(@RequestBody LogInRequest request) {

        LogInResponse response = logInResponseMapper.toLogInResponse(
                                    userServicePort.login(
                                            logInRequestMapper.logInRequestToUser(request)));

        return ResponseEntity.ok(response);
    }

    @PostMapping("register/client")
    public ResponseEntity<UserCreatedResponse> addClientUser(@RequestBody AddClientRequest request) {

        if (!ControllerAdapterConstants.CLIENT_ROLE_ID.equals(request.roleId())) {
            throw new RoleMismatchException(ControllerAdapterConstants.ROLE_MISMATCH_MESSAGE);
        }

        UserCreatedResponse response = userCreatedResponseMapper.toUserCreatedResponse(
                                        userServicePort.saveUser(
                                                clientUserRequestMapper.addClientRequestToUser(request)));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("user/verify-role")
    public ResponseEntity<Boolean> verifyRole(@RequestParam Long id, @RequestParam String role) {
        boolean isValidRole = userServicePort.validateRole(id, role);
        return ResponseEntity.ok(isValidRole);
    }

    @GetMapping("get-number")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> getNumber(@RequestParam Long id) {
        UserPhoneResponse response = userPhoneResponseMapper.toUserPhoneResponse(
                                        userServicePort.getUserPhone(id));

        return ResponseEntity.ok(response.phoneNumber());
    }

    private Long extractToken(String token) {
        String jwt = token.substring(7);
        return tokenUtils.getSpecificClaim(tokenUtils.validateToken(jwt), ControllerAdapterConstants.USER_CLAIM).asLong();
    }
}
