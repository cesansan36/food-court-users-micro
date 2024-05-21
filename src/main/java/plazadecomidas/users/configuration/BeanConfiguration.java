package plazadecomidas.users.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import plazadecomidas.users.adapters.driven.authentication.userdetailsservice.UserDetailServ;
import plazadecomidas.users.adapters.driven.jpa.mysql.adapter.RoleAdapter;
import plazadecomidas.users.adapters.driven.jpa.mysql.adapter.UserAdapter;
import plazadecomidas.users.adapters.driven.jpa.mysql.mapper.IRoleEntityMapper;
import plazadecomidas.users.adapters.driven.jpa.mysql.mapper.IUserEntityMapper;
import plazadecomidas.users.adapters.driven.jpa.mysql.repository.IRoleRepository;
import plazadecomidas.users.adapters.driven.jpa.mysql.repository.IUserRepository;
import plazadecomidas.users.domain.primaryport.IUserServicePort;
import plazadecomidas.users.domain.primaryport.usecase.UserUseCase;
import plazadecomidas.users.domain.secondaryport.IRolePersistencePort;
import plazadecomidas.users.domain.secondaryport.IUserAuthentication;
import plazadecomidas.users.domain.secondaryport.IUserPersistencePort;
import plazadecomidas.users.util.ITokenUtils;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IUserEntityMapper userEntityMapper;
    private final IRoleEntityMapper roleEntityMapper;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public IUserServicePort userServicePort(IUserPersistencePort userPersistencePort, IUserAuthentication userAuthentication, IRolePersistencePort rolePresistencePort) {
        return new UserUseCase(userPersistencePort, rolePresistencePort, passwordEncoder, userAuthentication);
    }

    @Bean
    public IRolePersistencePort rolePersistencePort() {
        return new RoleAdapter(roleEntityMapper, roleRepository);
    }

    @Bean
    public IUserPersistencePort userPersistencePort() {
        return new UserAdapter(userRepository, userEntityMapper);
    }

    @Bean
    public UserDetailsService userDetailsService(IUserRepository userRepository, PasswordEncoder passwordEncoder, ITokenUtils tokenUtils) {
        return new UserDetailServ(userRepository, passwordEncoder, tokenUtils);
    }

    @Bean
    public IUserAuthentication userAuthentication(UserDetailsService userDetailsService) {
        return (IUserAuthentication)userDetailsService;
    }
}
