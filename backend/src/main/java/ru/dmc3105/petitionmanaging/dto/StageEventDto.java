package ru.dmc3105.petitionmanaging.dto;

import ru.dmc3105.petitionmanaging.model.StageEvent;

import java.util.Date;

public record StageEventDto(
        Long id,
        Date occurenceDate,
        StageEvent.Stage stage,
        UserDto assignee,
        PetitionDto petition
) {
}