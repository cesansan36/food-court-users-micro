package plazadecomidas.users.domain.primaryport.usecase;

import org.springframework.security.crypto.password.PasswordEncoder;
import plazadecomidas.users.domain.model.Role;
import plazadecomidas.users.domain.model.Token;
import plazadecomidas.users.domain.model.User;
import plazadecomidas.users.domain.primaryport.IUserServicePort;
import plazadecomidas.users.domain.secondaryport.IRolePersistencePort;
import plazadecomidas.users.domain.secondaryport.IUserAuthentication;
import plazadecomidas.users.domain.secondaryport.IUserPersistencePort;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IRolePersistencePort rolePersistencePort;
    private final PasswordEncoder passwordEncoder;
    private final IUserAuthentication userAuthentication;

    public UserUseCase(IUserPersistencePort userPersistencePort, IRolePersistencePort rolePersistencePort, PasswordEncoder passwordEncoder, IUserAuthentication userAuthentication) {
        this.userPersistencePort = userPersistencePort;
        this.rolePersistencePort = rolePersistencePort;
        this.passwordEncoder = passwordEncoder;
        this.userAuthentication = userAuthentication;
    }

    @Override
    public Token saveUser (User user) {

        Role role = rolePersistencePort.findById(user.getRole().getId());

        User encodedPasswordUser = new User(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getDocumentNumber(),
                user.getCellPhoneNumber(),
                user.getBirthDate(),
                user.getEmail(),
                passwordEncoder.encode(user.getPassword()),
                role
        );

        User savedUser = userPersistencePort.saveUser(encodedPasswordUser);

        String token = userAuthentication.createToken(savedUser);
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
