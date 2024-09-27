package ru.dmc3105.petitionmanaging.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dmc3105.petitionmanaging.model.PetitionStage;
import ru.dmc3105.petitionmanaging.model.User;

import java.util.List;

public interface PetitionStageRepository extends JpaRepository<PetitionStage, Long> {
    List<PetitionStage> findAllByAssigneeAndIsCurrentTrue(User assignee);
}
