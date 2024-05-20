package plazadecomidas.users.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import plazadecomidas.users.adapters.driven.jpa.mysql.adapter.UserAdapter;
import plazadecomidas.users.adapters.driven.jpa.mysql.mapper.IUserEntityMapper;
import plazadecomidas.users.adapters.driven.jpa.mysql.repository.IUserRepository;
import plazadecomidas.users.domain.primaryport.IUserServicePort;
import plazadecomidas.users.domain.primaryport.usecase.UserUseCase;
import plazadecomidas.users.domain.secondaryport.IUserPersistencePort;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public IUserServicePort userServicePort(IUserPersistencePort userPersistencePort) {
        return new UserUseCase(userPersistencePort, passwordEncoder);
    }

    @Bean
    public IUserPersistencePort userPersistencePort() {
        return new UserAdapter(userRepository, userEntityMapper);
    }
}
