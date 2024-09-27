package ru.dmc3105.petitionmanaging.security.register;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.dmc3105.petitionmanaging.model.User;
import ru.dmc3105.petitionmanaging.repository.UserRepository;

@Service
@AllArgsConstructor
public class RegisterService {
    private UserRepository repository;
    private PasswordEncoder passwordEncoder;

    public void register(String username,
                         String password,
                         String firstname,
                         String lastname) {
        repository.save(User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .firstname(firstname)
                        .lastname(lastname)
        .build());
    }
}
