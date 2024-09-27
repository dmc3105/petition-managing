package ru.dmc3105.petitionmanaging.service;

import ru.dmc3105.petitionmanaging.model.Petition;

import java.util.List;

public interface PetitionService {
    void createPetition(String reason, String description, String creator);

    List<Petition> findAllPetitions();

    List<Petition> findAllByAssigneeUsername(String username);
}
