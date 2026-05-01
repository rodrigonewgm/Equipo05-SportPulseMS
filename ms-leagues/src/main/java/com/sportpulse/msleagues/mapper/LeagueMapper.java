package com.sportpulse.msleagues.mapper;

import com.sportpulse.msleagues.dto.ApiLeagueWrapper;
import com.sportpulse.msleagues.dto.ApiSeason;
import com.sportpulse.msleagues.dto.CurrentSeasonDto;
import com.sportpulse.msleagues.dto.LeagueDetailResponseDto;
import com.sportpulse.msleagues.dto.LeagueResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LeagueMapper {

    LeagueResponseDto toDto(Integer id, String name, String type, String country,
                            String logo, Integer currentSeason, String startDate, String endDate);

    @Mapping(target = "id", source = "league.id")
    @Mapping(target = "name", source = "league.name")
    @Mapping(target = "type", source = "league.type")
    @Mapping(target = "logo", source = "league.logo")
    @Mapping(target = "country", source = "country.name")
    @Mapping(target = "seasons", expression = "java(mapSeasons(wrapper.getSeasons()))")
    @Mapping(target = "currentSeason", expression = "java(mapCurrentSeason(wrapper.getSeasons()))")
    LeagueDetailResponseDto toDetailDto(ApiLeagueWrapper wrapper);

    default List<Integer> mapSeasons(List<ApiSeason> seasons) {
        return seasons.stream().map(ApiSeason::getYear).toList();
    }

    default CurrentSeasonDto mapCurrentSeason(List<ApiSeason> seasons) {
        ApiSeason current = seasons.stream()
                .filter(ApiSeason::getCurrent)
                .findFirst()
                .orElse(null);
        if (current == null) return null;
        return CurrentSeasonDto.builder()
                .year(current.getYear())
                .startDate(current.getStart())
                .endDate(current.getEnd())
                .current(current.getCurrent())
                .build();
    }
}
