package com.sportpulse.msauth.service;

import com.sportpulse.msauth.dto.LoginRequestDto;
import com.sportpulse.msauth.dto.LoginResponseDto;
import com.sportpulse.msauth.exception.InvalidCredentialsException;
import com.sportpulse.msauth.model.User;
import com.sportpulse.msauth.model.UserRole;
import com.sportpulse.msauth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceLoginTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_conCredencialesValidas_devuelveToken() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("test@email.com")
                .username("testuser")
                .password(encoder.encode("Password1"))
                .role(UserRole.USER)
                .build();

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("fake-jwt-token");

        LoginRequestDto request = new LoginRequestDto("test@email.com", "Password1");
        LoginResponseDto response = authService.login(request);

        assertEquals("fake-jwt-token", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(3600, response.getExpiresIn());
    }

    @Test
    void login_conEmailInexistente_lanzaInvalidCredentials() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        LoginRequestDto request = new LoginRequestDto("noexiste@email.com", "Password1");

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void login_conPasswordIncorrecta_lanzaInvalidCredentials() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("test@email.com")
                .username("testuser")
                .password(encoder.encode("Password1"))
                .role(UserRole.USER)
                .build();

        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));

        LoginRequestDto request = new LoginRequestDto("test@email.com", "WrongPassword1");

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }
}
