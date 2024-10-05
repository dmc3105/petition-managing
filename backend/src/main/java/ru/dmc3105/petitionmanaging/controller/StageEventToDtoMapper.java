package ru.dmc3105.petitionmanaging.controller;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.dmc3105.petitionmanaging.dto.PetitionDto;
import ru.dmc3105.petitionmanaging.dto.StageEventDto;
import ru.dmc3105.petitionmanaging.model.Petition;
import ru.dmc3105.petitionmanaging.model.StageEvent;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class StageEventToDtoMapper {

    public abstract StageEventDto stageEventToDto(StageEvent stageEvent);

    @Mapping(target = "lastEvent", ignore = true)
    @Mapping(target = "creationEvent", ignore = true)
    protected abstract PetitionDto petitionToDto(Petition petition);
}
