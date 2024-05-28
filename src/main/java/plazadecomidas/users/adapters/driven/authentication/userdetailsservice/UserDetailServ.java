package plazadecomidas.users.adapters.driven.authentication.userdetailsservice;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import plazadecomidas.users.adapters.driven.jpa.mysql.entity.UserEntity;
import plazadecomidas.users.adapters.driven.jpa.mysql.repository.IUserRepository;
import plazadecomidas.users.adapters.driven.jpa.mysql.util.PersistenceConstants;
import plazadecomidas.users.adapters.driven.authentication.util.AuthConstants;
import plazadecomidas.users.domain.secondaryport.IUserAuthentication;
import plazadecomidas.users.util.ITokenUtils;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UserDetailServ implements UserDetailsService, IUserAuthentication {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ITokenUtils tokenUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(PersistenceConstants.USER_NOT_FOUND_MESSAGE));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(user.getRoleEntity().getName()));

        return new User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException(AuthConstants.INVALID_USERNAME_OR_PASSWORD);
        }

        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }

    public String logInUser(String username, String password, Long userId) {

        Authentication authentication = authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenUtils.createToken(authentication, userId);
    }

    public String createUser(String username, String password, String role, Long userId) {

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, authorities);

        return tokenUtils.createToken(authentication, userId);
    }

    @Override
    public String login(plazadecomidas.users.domain.model.User user, Long idUser) {
        return logInUser(user.getEmail(), user.getPassword(), idUser);
    }

    @Override
    public String createToken(plazadecomidas.users.domain.model.User user) {
        return createUser(user.getEmail(), user.getPassword(), user.getRole().getName(), user.getId());
    }
}
