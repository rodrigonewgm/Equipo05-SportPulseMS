package com.sportpulse.msfixtures.mapper;

import com.sportpulse.msfixtures.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FixtureMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "league", source = "league")
    @Mapping(target = "homeTeam", source = "homeTeam")
    @Mapping(target = "awayTeam", source = "awayTeam")
    @Mapping(target = "venue", source = "venue")
    FixtureResponseDto toDto(Integer id, String date, FixtureStatusDto status,
                             FixtureLeagueDto league, TeamInFixtureDto homeTeam,
                             TeamInFixtureDto awayTeam, VenueDto venue);
}
