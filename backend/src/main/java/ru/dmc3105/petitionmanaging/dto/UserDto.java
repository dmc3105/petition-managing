package ru.dmc3105.petitionmanaging.dto;

public record UserDto(
        Long id,
        String username,
        String firstname,
        String lastname
) {
}
