package ru.dmc3105.petitionmanaging.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dmc3105.petitionmanaging.dto.PetitionInfoResponseDto;
import ru.dmc3105.petitionmanaging.dto.PetitionResponseDto;
import ru.dmc3105.petitionmanaging.dto.StageEventResponseDto;
import ru.dmc3105.petitionmanaging.dto.UpdatePetitionRequestDto;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.StageEvent;
import ru.dmc3105.petitionmanaging.service.PetitionService;
import ru.dmc3105.petitionmanaging.service.impl.StageEventService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/petition")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class PetitionAdminController {
    private PetitionService petitionService;
    private StageEventService stageEventService;
    private PetitionToDtoMapper mapper;
    private StageEventToDtoMapper stageEventMapper;

    @GetMapping("/{id}")
    public PetitionResponseDto getPetitionById(@PathVariable Long id) {
        final Petition petition = petitionService.getPetitionById(id);
        return toPetitionResponseDto(petition);
    }

    @GetMapping("/{id}/event")
    public List<StageEventResponseDto> getStageEventsByPetitionId(@PathVariable Long id) {
        Petition petition = petitionService.getPetitionById(id);
        return stageEventService.getAllByPetition(petition)
                .map(event -> stageEventMapper.toStageEventDto(event))
                .toList();
    }

    @DeleteMapping("/{id}/delete")
    public void deletePetitionById(@PathVariable Long id) {
        final Petition petition = petitionService.getPetitionById(id);
        petitionService.deletePetition(petition);
    }

    @PutMapping("/{id}/update")
    public PetitionInfoResponseDto updatePetitionById(@PathVariable Long id,
                                                      @RequestBody UpdatePetitionRequestDto updatePetitionRequestDto) {
        final Petition petition = petitionService.getPetitionById(id);
        final Petition updatedPetition = petitionService.updatePetition(petition,
                updatePetitionRequestDto.reason(),
                updatePetitionRequestDto.description());

        return mapper.toPetitionInfoResponseDto(updatedPetition);
    }

    private PetitionResponseDto toPetitionResponseDto(Petition petition) {
        final StageEvent currentEvent = petitionService.getPetitionCurrentStageEvent(petition);
        return mapper.toPetitionResponseDto(petition, currentEvent);
    }
}
