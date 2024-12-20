package plazadecomidas.users.domain.primaryport.usecase;

import plazadecomidas.users.domain.model.Role;
import plazadecomidas.users.domain.model.Token;
import plazadecomidas.users.domain.model.User;
import plazadecomidas.users.domain.primaryport.IUserServicePort;
import plazadecomidas.users.domain.secondaryport.IPasswordEncoderPort;
import plazadecomidas.users.domain.secondaryport.IRestaurantConnectionPort;
import plazadecomidas.users.domain.secondaryport.IRolePersistencePort;
import plazadecomidas.users.domain.secondaryport.IUserAuthentication;
import plazadecomidas.users.domain.secondaryport.IUserPersistencePort;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IRolePersistencePort rolePersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;
    private final IUserAuthentication userAuthentication;
    private final IRestaurantConnectionPort restaurantConnectionPort;

    public UserUseCase(IUserPersistencePort userPersistencePort, IRolePersistencePort rolePersistencePort, IPasswordEncoderPort passwordEncoderPort, IUserAuthentication userAuthentication, IRestaurantConnectionPort restaurantConnectionPort) {
        this.userPersistencePort = userPersistencePort;
        this.rolePersistencePort = rolePersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.userAuthentication = userAuthentication;
        this.restaurantConnectionPort = restaurantConnectionPort;
    }

    @Override
    public Token saveUser(User user) {
        Role role = rolePersistencePort.findById(user.getRole().getId());

        User encodedPasswordUser = new User(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getDocumentNumber(),
                user.getCellPhoneNumber(),
                user.getBirthDate(),
                user.getEmail(),
                passwordEncoderPort.encode(user.getPassword()),
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

        User userFound = userPersistencePort.findByEmail(user.getEmail());

        String token = userAuthentication.login(user, userFound.getId());

        return new Token(token);
    }

    @Override
    public Token saveUserInBothServices(User user, String ownerToken, Long ownerId, Long restaurantId) {
        Role role = rolePersistencePort.findById(user.getRole().getId());

        User encodedPasswordUser = new User(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getDocumentNumber(),
                user.getCellPhoneNumber(),
                user.getBirthDate(),
                user.getEmail(),
                passwordEncoderPort.encode(user.getPassword()),
                role
        );

        User savedUser = userPersistencePort.saveUser(encodedPasswordUser);

        restaurantConnectionPort.saveUser(ownerToken, ownerId, savedUser.getId(), restaurantId);

        String token = userAuthentication.createToken(savedUser);
        return new Token(token);
    }

    @Override
    public String getUserPhone(Long id) {
        return userPersistencePort.getUserPhone(id);
    }

    @Override
    public String getUserEmail(Long id) {
        return userPersistencePort.getUserEmail(id);
    }
}
