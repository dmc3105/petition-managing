package ru.dmc3105.petitionmanaging.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dmc3105.petitionmanaging.exception.PetitionNotFoundException;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.StageEvent;
import ru.dmc3105.petitionmanaging.model.User;
import ru.dmc3105.petitionmanaging.repository.PetitionRepository;
import ru.dmc3105.petitionmanaging.service.PetitionService;

import java.util.Date;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class DefaultPetitionService implements PetitionService {
    private StageEventService stageEventService;
    private PetitionRepository petitionRepository;

    @Override
    @Transactional
    public Petition addPetition(String reason, String description, User creator) {
        Petition newPetition = Petition.builder()
                .reason(reason)
                .description(description)
                .build();

        StageEvent creationEvent = StageEvent.builder()
                .occurenceDate(new Date())
                .isCurrent(true)
                .assignee(creator)
                .petition(newPetition)
                .stage(StageEvent.Stage.CREATED)
                .build();

        stageEventService.addStageEvent(creationEvent);
        return newPetition;
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<Petition> getAllPetitionsByCreator(User user) {
        return stageEventService.getStageEventsByAssignee(user)
                .filter(petition -> petition.getStage() == StageEvent.Stage.CREATED)
                .map(StageEvent::getPetition);
    }

    @Override
    public StageEvent getPetitionCurrentStageEvent(Petition petition) {
        return petition.getEvents().stream()
                .filter(StageEvent::getIsCurrent)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public Petition getPetitionById(Long id) {
        return petitionRepository.findById(id).orElseThrow(
                () -> new PetitionNotFoundException(id));
    }

    @Override
    public User getPetitionCreator(Petition petition) {
        return petition.getEvents().stream()
                .filter(stageEvent -> stageEvent.getStage() == StageEvent.Stage.CREATED)
                .findFirst()
                .orElseThrow()
                .getAssignee();
    }

    @Override
    public Petition updatePetition(Petition petition, String reason, String description) {
        petition.setReason(reason);
        petition.setDescription(description);
        petitionRepository.save(petition);
        return petition;
    }

    @Override
    public void deletePetition(Petition petition) {
        petitionRepository.delete(petition);
    }
}
