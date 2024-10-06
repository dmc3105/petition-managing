package ru.dmc3105.petitionmanaging.service;

import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.request.AddPetitionRequest;
import ru.dmc3105.petitionmanaging.request.UpdatePetitionRequest;

import java.util.stream.Stream;

public interface PetitionService {
    Petition addPetition(AddPetitionRequest addPetitionRequest, String creatorUsername);

    Stream<Petition> getAllPetitionsByCreatorUsername(String username);

    Petition getPetitionById(Long id);

    Petition updatePetition(Long id, UpdatePetitionRequest updatePetitionRequest);

    void deletePetition(Long id);

    Petition viewPetitionById(Long id, String viewerUsername);

    Petition processPetitionById(Long id, String processorUsername);

    Petition completePetitionById(Long id, String executorUsername);

    Petition cancelPetitionById(Long id, String cancelerUsername);
}
