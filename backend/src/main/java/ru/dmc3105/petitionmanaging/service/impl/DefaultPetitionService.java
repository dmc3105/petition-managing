package ru.dmc3105.petitionmanaging.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.User;
import ru.dmc3105.petitionmanaging.repository.PetitionRepository;
import ru.dmc3105.petitionmanaging.repository.UserRepository;
import ru.dmc3105.petitionmanaging.service.PetitionService;

import java.util.List;

@AllArgsConstructor
@Service
public class DefaultPetitionService implements PetitionService {
    private PetitionRepository petitionRepository;
    private UserRepository userRepository;

    @Override
    public void createPetition(String reason, String description, String creatorUsername) {
        petitionRepository.save(Petition.builder()
                .stage(Petition.Stage.CREATED)
                .reason(reason)
                .description(description)
                .creator(userRepository.findUserByUsername(creatorUsername).orElseThrow(() -> new UsernameNotFoundException("Username not found")))
                .build()
        );
    }

    @Override
    public List<Petition> findAllPetitions() {
        return petitionRepository.findAll();
    }

    @Override
    public List<Petition> findAllPetitionsByCreatorUsername(String creatorUsername) {
        User creator = userRepository.findUserByUsername(creatorUsername).orElseThrow();
        return creator.getCreatedPetitions();
    }
}
