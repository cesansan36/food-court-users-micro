package plazadecomidas.users.configuration.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import plazadecomidas.users.adapters.driven.connection.exception.MicroserviceFailedInteractionException;
import plazadecomidas.users.adapters.driven.jpa.mysql.exception.RegistryAlreadyExistsException;
import plazadecomidas.users.adapters.driven.jpa.mysql.exception.RegistryNotFoundException;
import plazadecomidas.users.adapters.driving.http.rest.exception.RoleMismatchException;
import plazadecomidas.users.domain.exception.EmptyFieldException;
import plazadecomidas.users.domain.exception.FieldRuleInvalidException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(EmptyFieldException.class)
    public ResponseEntity<ExceptionResponse> handleEmptyFieldException(EmptyFieldException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(
                exception.getMessage(), HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }

    @ExceptionHandler(FieldRuleInvalidException.class)
    public ResponseEntity<ExceptionResponse> handleFieldRuleInvalidException(FieldRuleInvalidException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(
                exception.getMessage(), HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }

    @ExceptionHandler(RegistryAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleRegistryAlreadyExistsException(RegistryAlreadyExistsException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(
                exception.getMessage(), HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }

    @ExceptionHandler(RegistryNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleRegistryNotFoundException(RegistryNotFoundException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(
                exception.getMessage(), HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }

    @ExceptionHandler(RoleMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleRoleMismatchException(RoleMismatchException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(
                exception.getMessage(), HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }

    @ExceptionHandler(MicroserviceFailedInteractionException.class)
    public ResponseEntity<ExceptionResponse> handleMicroserviceFailedInteractionException(MicroserviceFailedInteractionException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(
                exception.getMessage(), HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now()));
    }
}
