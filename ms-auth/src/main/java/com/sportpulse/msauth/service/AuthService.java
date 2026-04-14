package com.sportpulse.msauth.service;

import com.sportpulse.msauth.dto.RegisterRequestDto;
import com.sportpulse.msauth.dto.RegisterResponseDto;
import com.sportpulse.msauth.model.User;
import com.sportpulse.msauth.model.UserRole;
import com.sportpulse.msauth.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public RegisterResponseDto register(RegisterRequestDto request){

        // Validar si el email ya existe
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("USER_ALREADY_EXISTS");
        }

        // Validar si el username ya existe
        if(userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("USERNAME_ALREADY_EXISTS");
        }

        // Encriptar password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // Crear usuario
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(hashedPassword)
                .role(UserRole.USER)
                .build();

        User savedUser = userRepository.save(user);

        return RegisterResponseDto.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .createdAt(savedUser.getCreatedAt())
                .build();

    }
}
