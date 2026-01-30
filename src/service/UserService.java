package service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import domain.User;
import domain.UserAuthRow;
import persistence.repo.UserRepo;

import java.util.Optional;

public class UserService
{
    private final UserRepo repo;

    public UserService(UserRepo repo)
    {
        this.repo = repo;
    }

    public User register(String username, String email, String rawPassword){
        if (rawPassword == null || rawPassword.isBlank())
            throw new IllegalArgumentException("Password required");

        String hashedPassword = BCrypt.withDefaults()
                .hashToString(12, rawPassword.toCharArray());

        username = username == null ? null : username.trim();
        email = email == null ? null : email.trim();

        return repo.insert(username, email, hashedPassword);
    }

    public Optional<User> login(String username, String rawPassword) {
        if (username == null || username.isBlank()) return Optional.empty();
        if (rawPassword == null || rawPassword.isBlank()) return Optional.empty();

        Optional<UserAuthRow> authOpt = repo.findAuthByUsername(username.trim());
        if (authOpt.isEmpty()) return Optional.empty();

        UserAuthRow auth = authOpt.get();
        BCrypt.Result result = BCrypt.verifyer()
                .verify(rawPassword.toCharArray(), auth.passwordHash());

        if (!result.verified) return Optional.empty();

        return Optional.of(new User(auth.id(), auth.username(), auth.email()));
    }

}
