package plazadecomidas.users.adapters.driven.encoding.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import plazadecomidas.users.domain.secondaryport.IPasswordEncoderPort;

@RequiredArgsConstructor
public class PasswordEncoderAdapter implements IPasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }
}
