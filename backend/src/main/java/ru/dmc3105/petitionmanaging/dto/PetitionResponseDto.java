package ru.dmc3105.petitionmanaging.dto;

import ru.dmc3105.petitionmanaging.model.StageEvent;

import java.util.Date;

public record PetitionResponseDto(
        Long id,
        String reason,
        String description,
        StageEvent.Stage currentStage,
        Date creationDate,
        UserResponseDto creator
) {

}
