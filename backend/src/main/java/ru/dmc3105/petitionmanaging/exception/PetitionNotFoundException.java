package ru.dmc3105.petitionmanaging.exception;

public class PetitionNotFoundException extends RuntimeException {
    public PetitionNotFoundException(String message) {
        super(message);
    }

    public PetitionNotFoundException(Long petitionId) {
        super("Petition with id=%d not found".formatted(petitionId));
    }
}
