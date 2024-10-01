package ru.dmc3105.petitionmanaging.controller;

import org.springframework.stereotype.Component;
import ru.dmc3105.petitionmanaging.dto.PetitionInfoResponseDto;
import ru.dmc3105.petitionmanaging.dto.PetitionResponseDto;
import ru.dmc3105.petitionmanaging.dto.UserInfoResponseDto;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.StageEvent;

@Component
public class PetitionToDtoMapper {
    public PetitionInfoResponseDto toPetitionInfoResponseDto(Petition petition) {
        return new PetitionInfoResponseDto(
                petition.getId(),
                petition.getReason(),
                petition.getDescription()
        );
    }

    public PetitionResponseDto toPetitionResponseDto(Petition petition, StageEvent stageEvent) {
        return new PetitionResponseDto(
                petition.getId(),
                petition.getReason(),
                petition.getDescription(),
                stageEvent.getStage(),
                stageEvent.getOccurenceDate(),
                new UserInfoResponseDto(
                        stageEvent.getAssignee().getId(),
                        stageEvent.getAssignee().getUsername(),
                        stageEvent.getAssignee().getFirstname(),
                        stageEvent.getAssignee().getLastname()
                )
        );
    }
}
