package ru.dmc3105.petitionmanaging.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dmc3105.petitionmanaging.model.Petition;

public interface PetitionRepository extends JpaRepository<Petition, Long> {
}
