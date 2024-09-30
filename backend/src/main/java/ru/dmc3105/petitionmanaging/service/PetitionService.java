package ru.dmc3105.petitionmanaging.service;

import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.StageEvent;
import ru.dmc3105.petitionmanaging.model.User;

import java.util.stream.Stream;

public interface PetitionService {
    Petition addPetition(String reason, String description, User creator);

    Stream<Petition> getAllPetitionsByCreator(User user);

    StageEvent getPetitionCurrentStageEvent(Petition petition);

    Petition getPetitionById(Long id);

    User getPetitionCreator(Petition petition);

    Petition updatePetition(Petition petition, String reason, String description);

    void deletePetition(Petition petition);
}
