package ru.dmc3105.petitionmanaging.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.dmc3105.petitionmanaging.dto.UserDto;
import ru.dmc3105.petitionmanaging.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserDtoToMapper {
    UserDto userToDto(User user);
}
