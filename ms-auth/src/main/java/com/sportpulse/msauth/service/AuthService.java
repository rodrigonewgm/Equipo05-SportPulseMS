package com.sportpulse.msauth.service;

import com.sportpulse.msauth.dto.LoginRequestDto;
import com.sportpulse.msauth.dto.LoginResponseDto;
import com.sportpulse.msauth.dto.RegisterRequestDto;
import com.sportpulse.msauth.dto.RegisterResponseDto;

import com.sportpulse.msauth.exception.UserAlreadyExistsException;
import com.sportpulse.msauth.exception.InvalidCredentialsException;
import com.sportpulse.msauth.exception.UsernameAlreadyExistsException;
import com.sportpulse.msauth.mapper.AuthMapper;
import com.sportpulse.msauth.mapper.UserMapper;
import com.sportpulse.msauth.model.User;
import com.sportpulse.msauth.model.UserRole;
import com.sportpulse.msauth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthMapper authMapper;
    
    public RegisterResponseDto register(RegisterRequestDto request){

        validateUser(request);

        // MapStruct crea el User (sin password ni role)
        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(user);

        return userMapper.toResponse(savedUser);

   }

    public void validateUser(RegisterRequestDto request){

        if(userRepository.existsByEmail(request.getEmail())){
            throw new UserAlreadyExistsException();
        }

        if(userRepository.existsByUsername(request.getUsername())){
            throw new UsernameAlreadyExistsException();
        }
    }

    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user);

        return authMapper.toLoginResponseDto(user, token);
    }
}

