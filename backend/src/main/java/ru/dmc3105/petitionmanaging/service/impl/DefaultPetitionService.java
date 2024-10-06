package ru.dmc3105.petitionmanaging.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dmc3105.petitionmanaging.exception.PetitionNotFoundException;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.Stage;
import ru.dmc3105.petitionmanaging.model.StageEvent;
import ru.dmc3105.petitionmanaging.model.User;
import ru.dmc3105.petitionmanaging.repository.PetitionRepository;
import ru.dmc3105.petitionmanaging.request.AddPetitionRequest;
import ru.dmc3105.petitionmanaging.request.UpdatePetitionRequest;
import ru.dmc3105.petitionmanaging.service.PetitionService;

import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class DefaultPetitionService implements PetitionService {
    private UserService userService;
    private PetitionRepository petitionRepository;

    @Override
    @Transactional
    public Petition addPetition(AddPetitionRequest addPetitionRequest, String creatorUsername) {
        User creator = userService.getUserByUsername(creatorUsername);

        Petition newPetition = Petition.createPetition(addPetitionRequest.reason(), addPetitionRequest.description());

        StageEvent creationStageEvent = StageEvent.createJustHappenedEvent(
                Stage.CREATED,
                creator,
                newPetition
        );

        newPetition.getEvents().add(creationStageEvent);

        return petitionRepository.save(newPetition);
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<Petition> getAllPetitionsByCreatorUsername(String username) {
        User user = userService.getUserByUsername(username);
        return user.getEvents().stream()
                .filter(event -> event.getStage() == Stage.CREATED)
                .map(StageEvent::getPetition);
    }

    @Override
    public Petition getPetitionById(Long id) {
        return petitionRepository.findById(id).orElseThrow(
                () -> new PetitionNotFoundException(id));
    }

    @Override
    public Petition updatePetition(Long id, UpdatePetitionRequest updatePetitionRequest) {
        Petition petition = getPetitionById(id);
        petition.setReason(updatePetitionRequest.reason());
        petition.setDescription(updatePetitionRequest.description());
        petitionRepository.save(petition);
        return petition;
    }

    @Override
    public void deletePetition(Long id) {
        petitionRepository.deleteById(id);
    }

    @Override
    public Petition viewPetitionById(Long id, String viewerUsername) {
        final StageEvent currentStage = getCurrentStageByPetitionId(id);
        switch (currentStage.getStage()) {
            case CREATED -> {
                return changePetitionStage(currentStage, Stage.VIEWED, viewerUsername);
            }
            default -> throw new IllegalStateException();
        }
    }

    @Override
    public Petition processPetitionById(Long id, String processorUsername) {
        final StageEvent currentStage = getCurrentStageByPetitionId(id);
        switch (currentStage.getStage()) {
            case CREATED, VIEWED -> {
                return changePetitionStage(currentStage, Stage.PROCESSING, processorUsername);
            }
            default -> throw new IllegalStateException();
        }
    }

    @Override
    public Petition completePetitionById(Long id, String executorUsername) {
        final StageEvent currentStage = getCurrentStageByPetitionId(id);
        switch (currentStage.getStage()) {
            case CREATED, VIEWED, PROCESSING -> {
                return changePetitionStage(currentStage, Stage.COMPLETED, executorUsername);
            }
            default -> throw new IllegalStateException();
        }
    }

    @Override
    public Petition cancelPetitionById(Long id, String cancelerUsername) {
        final StageEvent currentStage = getCurrentStageByPetitionId(id);
        switch (currentStage.getStage()) {
            case CREATED, VIEWED, PROCESSING -> {
                return changePetitionStage(currentStage, Stage.CANCELED, cancelerUsername);
            }
            default -> throw new IllegalStateException();
        }
    }

    private Petition changePetitionStage(StageEvent currentStageEvent, Stage newStage, String assigneeUsername) {
        currentStageEvent.setIsCurrent(false);
        final Petition petition = currentStageEvent.getPetition();
        final User assignee = userService.getUserByUsername(assigneeUsername);

        petition.getEvents().add(
                StageEvent.createJustHappenedEvent(
                        newStage,
                        assignee,
                        petition
                )
        );

        return petitionRepository.save(petition);
    }

    private StageEvent getCurrentStageByPetitionId(Long id) {
        Petition petition = getPetitionById(id);
        return petition.getEvents().stream()
                .filter(StageEvent::getIsCurrent)
                .findFirst()
                .orElseThrow();
    }
}