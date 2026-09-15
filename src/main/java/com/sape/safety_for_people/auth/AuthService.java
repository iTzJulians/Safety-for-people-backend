package com.sape.safety_for_people.auth;

import com.sape.safety_for_people.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(user);
        return AuthResponse.builder().token(token).build();
    }

    public AuthResponse register(RegisterRequest request) {
        // Aquí debes guardar el usuario en tu Repository encriptando la contraseña:
        // String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(user);
        return AuthResponse.builder().token(token).build();
    }
}