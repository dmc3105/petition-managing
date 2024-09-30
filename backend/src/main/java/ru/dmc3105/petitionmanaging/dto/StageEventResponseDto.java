package ru.dmc3105.petitionmanaging.dto;

import ru.dmc3105.petitionmanaging.model.StageEvent;

import java.util.Date;

public record StageEventResponseDto(
        Long id,
        Date occurenceDate,
        StageEvent.Stage stage,
        UserInfoResponseDto assignee,
        PetitionInfoResponseDto petition
) {
}
