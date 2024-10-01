package ru.dmc3105.petitionmanaging.controller;


import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dmc3105.petitionmanaging.dto.PetitionInfoResponseDto;
import ru.dmc3105.petitionmanaging.dto.StageEventResponseDto;
import ru.dmc3105.petitionmanaging.dto.UserInfoResponseDto;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.StageEvent;
import ru.dmc3105.petitionmanaging.model.User;
import ru.dmc3105.petitionmanaging.service.PetitionService;
import ru.dmc3105.petitionmanaging.service.impl.StageEventService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/petition")
public class StageEventsController {
    private StageEventService stageEventService;
    private PetitionService petitionService;

    @GetMapping("/{id}/event")
    @PreAuthorize("@authorizationService.hasAccessTo(principal, #id)")
    public List<StageEventResponseDto> getStageEventsByPetitionId(@PathVariable Long id) {
        Petition petition = petitionService.getPetitionById(id);
        return stageEventService.getAllByPetition(petition)
                .map(this::getStageEventDto).toList();
    }

    private StageEventResponseDto getStageEventDto(StageEvent stageEvent) {
        final User assignee = stageEvent.getAssignee();
        final Petition petition = stageEvent.getPetition();

        return new StageEventResponseDto(
                stageEvent.getId(),
                stageEvent.getOccurenceDate(),
                stageEvent.getStage(),
                new UserInfoResponseDto(
                        assignee.getId(),
                        assignee.getUsername(),
                        assignee.getFirstname(),
                        assignee.getLastname()
                ),
                new PetitionInfoResponseDto(
                        petition.getId(),
                        petition.getReason(),
                        petition.getDescription()
                )
        );
    }
}
