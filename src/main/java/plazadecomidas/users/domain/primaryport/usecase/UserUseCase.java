package plazadecomidas.users.domain.primaryport.usecase;

import org.springframework.security.crypto.password.PasswordEncoder;
import plazadecomidas.users.domain.model.Token;
import plazadecomidas.users.domain.model.User;
import plazadecomidas.users.domain.primaryport.IUserServicePort;
import plazadecomidas.users.domain.secondaryport.IUserPersistencePort;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final PasswordEncoder passwordEncoder;

    public UserUseCase(IUserPersistencePort userPersistencePort, PasswordEncoder passwordEncoder) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoder = passwordEncoder;
    }

    public Token saveUser (User user) {

        User encodedPasswordUser = new User(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getDocumentNumber(),
                user.getCellPhoneNumber(),
                user.getBirthDate(),
                user.getEmail(),
                passwordEncoder.encode(user.getPassword()),
                user.getRole()
                );

        User savedUser = userPersistencePort.saveUser(encodedPasswordUser);

        String token =  "TODO: Unimplemented token - " + savedUser.getName();  // TODO Token will be implemented in future user story
        return new Token(token);
    }
}
