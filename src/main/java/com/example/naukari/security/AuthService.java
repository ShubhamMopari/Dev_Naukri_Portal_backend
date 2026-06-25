package com.example.naukari.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.naukari.DTO.LoginRequestDTO;
import com.example.naukari.DTO.LoginResponseDTO;
import com.example.naukari.entity.User;
import com.example.naukari.repository.UserRepository;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private AuthenticationManager authManager;

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getEmail(),
                loginRequestDTO.getPassword()
            )
        );
        User user = (User) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user);
        return new LoginResponseDTO(token, user.getEmail(), user.getRole(), user.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
