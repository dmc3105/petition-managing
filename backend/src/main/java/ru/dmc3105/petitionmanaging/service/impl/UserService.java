package ru.dmc3105.petitionmanaging.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.dmc3105.petitionmanaging.model.User;
import ru.dmc3105.petitionmanaging.repository.UserRepository;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService{
    private UserRepository userRepository;

    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User with username \"%s\" not found".formatted(username)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username);
    }
}
