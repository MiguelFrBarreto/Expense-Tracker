package com.example.expenseTracker.services;

import java.util.Set;

import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.expenseTracker.config.TokenConfig;
import com.example.expenseTracker.dtos.JWTUserData;
import com.example.expenseTracker.dtos.LoginRequest;
import com.example.expenseTracker.dtos.RegisterRequest;
import com.example.expenseTracker.dtos.RegisterResponse;
import com.example.expenseTracker.dtos.UserResponse;
import com.example.expenseTracker.entities.User;
import com.example.expenseTracker.enums.Role;
import com.example.expenseTracker.exceptions.EmailAlreadyUsedException;
import com.example.expenseTracker.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public ResponseCookie login(LoginRequest request){
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication = authenticationManager.authenticate(userAndPass);

        User user = (User) authentication.getPrincipal();
        String token = tokenConfig.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("auth_token", token)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(86400)
            .sameSite("Lax")
            .build();

        return cookie;
    }

    public RegisterResponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.email())){
            throw new EmailAlreadyUsedException("Email already used");
        }
        
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        user.setRoles(Set.of(Role.ROLE_USER));
        
        userRepository.save(user);

        return new RegisterResponse(user.getName(), user.getEmail());
    }

    public UserResponse me(JWTUserData userData){
        return userService.findById(userData.userId());
    }
}