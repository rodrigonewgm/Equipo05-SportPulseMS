package com.sportpulse.msteams.mapper;

import com.sportpulse.msteams.dto.StadiumDetailDto;
import com.sportpulse.msteams.dto.TeamDetailResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamDetailMapper {

    TeamDetailResponseDto toDto(Integer id, String name, String country,
                                String logo, Integer founded, Boolean national,
                                StadiumDetailDto stadium);
}
