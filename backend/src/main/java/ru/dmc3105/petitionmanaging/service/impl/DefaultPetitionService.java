package ru.dmc3105.petitionmanaging.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.StageEvent;
import ru.dmc3105.petitionmanaging.model.User;
import ru.dmc3105.petitionmanaging.service.PetitionService;

import java.util.Date;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class DefaultPetitionService implements PetitionService {
    private StageEventService stageEventService;

    @Override
    @Transactional
    public void addPetition(String reason, String description, User creator) {
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
}
