package ru.dmc3105.petitionmanaging.controller;

import org.springframework.stereotype.Component;
import ru.dmc3105.petitionmanaging.dto.PetitionInfoResponseDto;
import ru.dmc3105.petitionmanaging.dto.StageEventResponseDto;
import ru.dmc3105.petitionmanaging.dto.UserInfoResponseDto;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.StageEvent;
import ru.dmc3105.petitionmanaging.model.User;

@Component
public class StageEventToDtoMapper {
    public StageEventResponseDto toStageEventDto(StageEvent stageEvent) {
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
