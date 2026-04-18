package com.sportpulse.msauth.service;

import com.sportpulse.msauth.dto.RegisterRequestDto;
import com.sportpulse.msauth.dto.RegisterResponseDto;
import com.sportpulse.msauth.exception.UserAlreadyExistsException;
import com.sportpulse.msauth.exception.UsernameAlreadyExistsException;
import com.sportpulse.msauth.mapper.UserMapper;
import com.sportpulse.msauth.model.User;
import com.sportpulse.msauth.model.UserRole;
import com.sportpulse.msauth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public RegisterResponseDto register(RegisterRequestDto request){

        validateUser(request);

        // MapStruct crea el User (sin password ni role)
        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);

        User savedUser = userRepository.save(user);

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
}
