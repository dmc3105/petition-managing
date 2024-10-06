package ru.dmc3105.petitionmanaging.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dmc3105.petitionmanaging.dto.PetitionDto;
import ru.dmc3105.petitionmanaging.dto.StageEventDto;
import ru.dmc3105.petitionmanaging.mapper.PetitionToDtoMapper;
import ru.dmc3105.petitionmanaging.mapper.StageEventToDtoMapper;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.service.PetitionService;
import ru.dmc3105.petitionmanaging.service.impl.StageEventService;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/admin/petition")
public class PetitionAdminController {
    private PetitionService petitionService;
    private StageEventService stageEventService;
    private StageEventToDtoMapper stageEventMapper;
    private PetitionToDtoMapper petitionMapper;

    @GetMapping("/{id}/event")
    public List<StageEventDto> getStageEventsByPetitionId(@PathVariable Long id) {
        Petition petition = petitionService.getPetitionById(id);
        return stageEventService.getAllByPetition(petition)
                .map(event -> stageEventMapper.stageEventToDto(event))
                .toList();
    }

    @PatchMapping("/{id}/view")
    public PetitionDto viewPetitionById(@PathVariable Long id, Principal principal) {
        final Petition updatedPetition = petitionService.viewPetitionById(id);
        return petitionMapper.petitionToDto(updatedPetition);
    }

    @PatchMapping("/{id}/process")
    public PetitionDto processPetitionById(@PathVariable Long id, Principal principal) {
        final Petition updatedPetition = petitionService.processPetitionById(id);
        return petitionMapper.petitionToDto(updatedPetition);
    }

    @PatchMapping("/{id}/complete")
    public PetitionDto cancelPetitionById(@PathVariable Long id, Principal principal) {
        final Petition updatedPetition = petitionService.completePetitionById(id);
        return petitionMapper.petitionToDto(updatedPetition);
    }
}
