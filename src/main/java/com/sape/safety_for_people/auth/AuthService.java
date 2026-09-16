package com.sape.safety_for_people.auth;

import com.sape.safety_for_people.model.Role;
import com.sape.safety_for_people.model.User;
import com.sape.safety_for_people.repository.RoleRepository;
import com.sape.safety_for_people.repository.UserRepository;
import com.sape.safety_for_people.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado."));

        String token = jwtUtil.generateToken(userDetails, user.getId());
        return AuthResponse.builder().token(token).build();
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario registrado con ese correo.");
        }

        // Si la petición no envía roleId o se busca asegurar rol estándar, asignamos el ID 2L (Usuario)
        Long roleIdToAssign = (request.getRoleId() != null) ? request.getRoleId() : 2L;

        Role role = roleRepository.findById(roleIdToAssign)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol no válido: " + roleIdToAssign));

        User newUser = new User();
        newUser.setName(request.getNombre());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setActive(true);
        newUser.setRole(role);
        userRepository.save(newUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername(newUser.getEmail());
        String token = jwtUtil.generateToken(userDetails, newUser.getId());
        return AuthResponse.builder().token(token).build();
    }
}