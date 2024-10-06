package ru.dmc3105.petitionmanaging.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.Stage;
import ru.dmc3105.petitionmanaging.model.StageEvent;
import ru.dmc3105.petitionmanaging.model.User;
import ru.dmc3105.petitionmanaging.repository.StageEventRepository;

import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class StageEventService {
    private StageEventRepository repository;

    public void addStageEvent(StageEvent stageEvent) {
        repository.save(stageEvent);
    }

    @Transactional(readOnly = true)
    public Stream<StageEvent> getStageEventsByAssignee(User assignee) {
        return repository.findAllByAssignee(assignee);
    }

    public StageEvent getStageEventByPetitionAndStage(Petition petition, Stage stage) {
        return petition.getEvents().stream()
                .filter(event -> event.getStage() == stage)
                .findFirst()
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    public Stream<StageEvent> getAllByPetition(Petition petition) {
        return repository.findAllByPetition(petition);
    }
}
