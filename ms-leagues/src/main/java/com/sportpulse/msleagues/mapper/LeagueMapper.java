package com.sportpulse.msleagues.mapper;

import com.sportpulse.msleagues.dto.LeagueResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LeagueMapper {

    LeagueResponseDto toDto(Integer id, String name, String type, String country, String logo, Integer currentSeason, String startDate, String endDate);
}