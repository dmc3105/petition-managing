package ru.dmc3105.petitionmanaging.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.dmc3105.petitionmanaging.model.Petition;

import java.util.List;

public interface PetitionRepository extends JpaRepository<Petition, Long> {
}
