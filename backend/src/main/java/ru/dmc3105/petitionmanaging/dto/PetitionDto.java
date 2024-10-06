package ru.dmc3105.petitionmanaging.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public record PetitionDto(
        Long id,
        String reason,
        String description,
        @JsonIgnoreProperties("petition") StageEventDto lastEvent,
        @JsonIgnoreProperties("petition") StageEventDto creationEvent
) {

}
