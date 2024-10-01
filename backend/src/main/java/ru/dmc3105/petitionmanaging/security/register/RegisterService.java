package ru.dmc3105.petitionmanaging.security.register;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.dmc3105.petitionmanaging.model.Role;
import ru.dmc3105.petitionmanaging.model.User;
import ru.dmc3105.petitionmanaging.repository.RoleRepository;
import ru.dmc3105.petitionmanaging.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class RegisterService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public void register(String username,
                         String password,
                         String firstname,
                         String lastname) {
        final Role userRole = roleRepository.findRoleByName(Role.RoleName.USER).orElseThrow();

        userRepository.save(User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .firstname(firstname)
                .lastname(lastname)
                .roles(List.of(userRole))
                .build());
    }
}
