package com.sportpulse.msauth.mapper;

import com.sportpulse.msauth.dto.LoginResponseDto;
import com.sportpulse.msauth.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "token", source = "token")
    @Mapping(target = "tokenType", constant = "Bearer")
    @Mapping(target = "expiresIn", constant = "3600L")
    @Mapping(target = "userId", expression = "java(user.getId().toString())")
    LoginResponseDto toLoginResponseDto(User user, String token);
}
