package com.sportpulse.msteams.mapper;

import com.sportpulse.msteams.dto.StadiumDto;
import com.sportpulse.msteams.dto.TeamResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamResponseDto toDto(Integer id,
                          String name,
                          String country,
                          String logo,
                          Integer founded,
                          StadiumDto stadium);
}
