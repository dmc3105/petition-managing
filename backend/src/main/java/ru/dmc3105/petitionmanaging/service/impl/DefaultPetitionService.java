package ru.dmc3105.petitionmanaging.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dmc3105.petitionmanaging.controller.request.AddPetitionRequest;
import ru.dmc3105.petitionmanaging.controller.request.UpdatePetitionRequest;
import ru.dmc3105.petitionmanaging.exception.PetitionNotFoundException;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.StageEvent;
import ru.dmc3105.petitionmanaging.model.User;
import ru.dmc3105.petitionmanaging.repository.PetitionRepository;
import ru.dmc3105.petitionmanaging.service.PetitionService;

import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class DefaultPetitionService implements PetitionService {
    private StageEventService stageEventService;
    private UserService userService;
    private PetitionRepository petitionRepository;

    @Override
    @Transactional
    public Petition addPetition(AddPetitionRequest addPetitionRequest, String creatorUsername) {
        User creator = userService.getUserByUsername(creatorUsername);

        Petition newPetition = Petition.builder()
                .reason(addPetitionRequest.reason())
                .description(addPetitionRequest.description())
                .build();

        StageEvent creationEvent = StageEvent.createJustHappenedEvent(
                    StageEvent.Stage.CREATED,
                    creator,
                    newPetition
                );
        stageEventService.addStageEvent(creationEvent);

        newPetition.setEvents(List.of(creationEvent));
        return petitionRepository.save(newPetition);
    }

    @Override
    @Transactional(readOnly = true)
    public Stream<Petition> getAllPetitionsByCreatorUsername(String username) {
        User user = userService.getUserByUsername(username);
        return user.getEvents().stream()
                .filter(event -> event.getStage() == StageEvent.Stage.CREATED)
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
}
