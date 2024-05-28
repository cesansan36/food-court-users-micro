package plazadecomidas.users.domain.secondaryport;

public interface IPasswordEncoderPort {
    String encode(String password);
}
