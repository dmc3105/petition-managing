package ru.dmc3105.petitionmanaging.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dmc3105.petitionmanaging.controller.request.UpdatePetitionRequest;
import ru.dmc3105.petitionmanaging.dto.PetitionDto;
import ru.dmc3105.petitionmanaging.dto.StageEventDto;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.service.PetitionService;
import ru.dmc3105.petitionmanaging.service.impl.StageEventService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/petition")

public class PetitionAdminController {
    private PetitionService petitionService;
    private StageEventService stageEventService;
    private PetitionToDtoMapper mapper;
    private StageEventToDtoMapper stageEventMapper;

    @GetMapping("/{id}")
    public PetitionDto getPetitionById(@PathVariable Long id) {
        final Petition petition = petitionService.getPetitionById(id);
        return mapper.petitionToDto(petition);
    }

    @GetMapping("/{id}/event")
    public List<StageEventDto> getStageEventsByPetitionId(@PathVariable Long id) {
        Petition petition = petitionService.getPetitionById(id);
        return stageEventService.getAllByPetition(petition)
                .map(event -> stageEventMapper.stageEventToDto(event))
                .toList();
    }

    @DeleteMapping("/{id}/delete")
    public void deletePetitionById(@PathVariable Long id) {
        final Petition petition = petitionService.getPetitionById(id);
        petitionService.deletePetition(petition);
    }

    @PutMapping("/{id}/update")
    public PetitionDto updatePetitionById(@PathVariable Long id,
                                          @RequestBody UpdatePetitionRequest updatePetitionRequestDto) {
        final Petition petition = petitionService.getPetitionById(id);
        final Petition updatedPetition = petitionService.updatePetition(petition,
                updatePetitionRequestDto.reason(),
                updatePetitionRequestDto.description());

        return mapper.petitionToDto(updatedPetition);
    }
}
