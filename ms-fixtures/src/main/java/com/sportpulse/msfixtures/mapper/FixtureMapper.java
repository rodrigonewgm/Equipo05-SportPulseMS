package com.sportpulse.msfixtures.mapper;

import com.sportpulse.msfixtures.dto.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FixtureMapper {

    FixtureResponseDto toDto(Integer id, String date, FixtureStatusDto status,
                             FixtureLeagueDto league, TeamInFixtureDto homeTeam,
                             TeamInFixtureDto awayTeam, VenueDto venue);
}
