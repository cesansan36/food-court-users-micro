package plazadecomidas.users.domain.primaryport.usecase;

import org.springframework.security.crypto.password.PasswordEncoder;
import plazadecomidas.users.domain.model.Token;
import plazadecomidas.users.domain.model.User;
import plazadecomidas.users.domain.primaryport.IUserServicePort;
import plazadecomidas.users.domain.secondaryport.IUserAuthentication;
import plazadecomidas.users.domain.secondaryport.IUserPersistencePort;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final PasswordEncoder passwordEncoder;
    private final IUserAuthentication userAuthentication;

    public UserUseCase(IUserPersistencePort userPersistencePort, PasswordEncoder passwordEncoder, IUserAuthentication userAuthentication) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoder = passwordEncoder;
        this.userAuthentication = userAuthentication;
    }

    @Override
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

    @Override
    public boolean validateRole (Long id, String role) {
        User user = userPersistencePort.findById(id);
        return user.getRole().getName().equals(role);
    }

    @Override
    public Token login(User user) {

        String token = userAuthentication.login(user);

        return new Token(token);
    }
}
