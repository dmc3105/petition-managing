package ru.dmc3105.petitionmanaging.controller;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.dmc3105.petitionmanaging.dto.PetitionDto;
import ru.dmc3105.petitionmanaging.dto.StageEventDto;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.StageEvent;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class PetitionToDtoMapper {
    @Mapping(target = "lastEvent", expression = "java(getCurrentEvent(petition))")
    @Mapping(target = "creationEvent", expression = "java(getCreationEvent(petition))")
    public abstract PetitionDto petitionToDto(Petition petition);

    protected StageEventDto getCurrentEvent(Petition petition) {
        return petition.getEvents().stream()
                .filter(StageEvent::getIsCurrent)
                .findFirst()
                .map(this::stageEventToDto)
                .orElse(null);
    }

    protected StageEventDto getCreationEvent(Petition petition) {
        return petition.getEvents().stream()
                .filter(event -> event.getStage() == StageEvent.Stage.CREATED)
                .findFirst()
                .map(this::stageEventToDto)
                .orElse(null);
    }

    @Mapping(target = "petition",ignore = true)
    protected abstract StageEventDto stageEventToDto(StageEvent stageEvent);
}