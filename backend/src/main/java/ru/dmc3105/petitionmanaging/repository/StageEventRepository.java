package ru.dmc3105.petitionmanaging.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.StageEvent;
import ru.dmc3105.petitionmanaging.model.User;

import java.util.stream.Stream;

public interface StageEventRepository extends JpaRepository<StageEvent, Long> {
    Stream<StageEvent> findAllByAssignee(User assignee);

    Stream<StageEvent> findAllByPetition(Petition petition);
}
