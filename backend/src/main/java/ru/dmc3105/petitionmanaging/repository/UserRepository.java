package ru.dmc3105.petitionmanaging.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dmc3105.petitionmanaging.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
}
