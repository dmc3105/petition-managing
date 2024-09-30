package ru.dmc3105.petitionmanaging.dto;

public record UserInfoResponseDto(
        Long id,
        String username,
        String firstname,
        String lastname
){
}
