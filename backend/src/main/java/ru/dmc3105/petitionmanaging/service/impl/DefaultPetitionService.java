package ru.dmc3105.petitionmanaging.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.PetitionStage;
import ru.dmc3105.petitionmanaging.model.User;
import ru.dmc3105.petitionmanaging.repository.PetitionRepository;
import ru.dmc3105.petitionmanaging.repository.PetitionStageRepository;
import ru.dmc3105.petitionmanaging.repository.UserRepository;
import ru.dmc3105.petitionmanaging.service.PetitionService;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class DefaultPetitionService implements PetitionService {
    private PetitionStageRepository petitionStageRepository;
    private PetitionRepository petitionRepository;
    private UserRepository userRepository;

    @Override
    @Transactional
    public void createPetition(String reason, String description, String creatorUsername) {
        var assignee = userRepository.findUserByUsername(creatorUsername).orElseThrow(
                () -> new UsernameNotFoundException("Username not found")
        );
        var petition = Petition.builder()
                .reason(reason)
                .description(description)
                .build();

        PetitionStage.Stage stage = PetitionStage.Stage.CREATED;

        petitionRepository.save(petition);

        petitionStageRepository.save(
                PetitionStage.builder()
                        .stage_creation_date(new Date())
                        .assignee(assignee)
                        .petition(petition)
                        .stage(stage)
                        .isCurrent(true)
                        .build()
        );
    }

    @Override
    public List<Petition> findAllPetitions() {
        return petitionRepository.findAll();
    }

    @Override
    public List<Petition> findAllByAssigneeUsername(String assigneeUsername) {
        User creator = userRepository.findUserByUsername(assigneeUsername).orElseThrow();
        List<PetitionStage> petitionStage = petitionStageRepository.findAllByAssigneeAndIsCurrentTrue(creator);
        return petitionStage.stream().map(PetitionStage::getPetition).toList();
    }
}
