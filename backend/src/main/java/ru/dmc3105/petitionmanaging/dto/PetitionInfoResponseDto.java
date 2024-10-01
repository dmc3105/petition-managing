package ru.dmc3105.petitionmanaging.dto;

public record PetitionInfoResponseDto(
        Long id,
        String reason,
        String description
) {
}
