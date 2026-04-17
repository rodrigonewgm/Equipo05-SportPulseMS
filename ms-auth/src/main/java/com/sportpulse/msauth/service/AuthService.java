package com.sportpulse.msauth.service;

import com.sportpulse.msauth.dto.LoginRequestDto;
import com.sportpulse.msauth.dto.LoginResponseDto;
import com.sportpulse.msauth.dto.RegisterRequestDto;
import com.sportpulse.msauth.dto.RegisterResponseDto;
import com.sportpulse.msauth.exception.InvalidCredentialsException;
import com.sportpulse.msauth.exception.UsernameAlreadyExistsException;
import com.sportpulse.msauth.mapper.UserMapper;
import com.sportpulse.msauth.model.User;
import com.sportpulse.msauth.model.UserRole;
import com.sportpulse.msauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    public RegisterResponseDto register(RegisterRequestDto request) {
        validateUser(request);

        // MapStruct crea el User (sin password ni role)
        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    private void validateUser(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UsernameAlreadyExistsException("Ya existe un usuario con ese email");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException("Ya existe un usuario con ese username");
        }
    }

    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user);

        return LoginResponseDto.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(3600)
                .userId(user.getId().toString())
                .build();
    }
}