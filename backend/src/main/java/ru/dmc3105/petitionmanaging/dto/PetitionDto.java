package ru.dmc3105.petitionmanaging.dto;

public record PetitionDto(
        Long id,
        String reason,
        String description,
        StageEventDto currentStage,
        UserDto creator
) {

}
