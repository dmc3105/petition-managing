package ru.dmc3105.petitionmanaging.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.dmc3105.petitionmanaging.model.Stage;

import java.util.Date;

public record StageEventDto(
        Long id,
        Date occurenceDate,
        Stage stage,
        UserDto assignee,
        @JsonIgnoreProperties({"lastEvent", "creationEvent"}) PetitionDto petition
) {
}
