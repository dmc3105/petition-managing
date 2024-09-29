package ru.dmc3105.petitionmanaging.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.dmc3105.petitionmanaging.model.StageEvent;
import ru.dmc3105.petitionmanaging.model.User;

import java.util.List;
import java.util.stream.Stream;

public interface StageEventRepository extends JpaRepository<StageEvent, Long> {
    @Transactional(readOnly = true)
    Stream<StageEvent> findAllByAssignee(User assignee);
}
