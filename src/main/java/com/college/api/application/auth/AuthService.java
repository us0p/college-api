package com.college.api.application.auth;

import com.college.api.application.exception.InvalidCredentialsException;
import com.college.api.domain.role.RolePermissionRepository;
import com.college.api.domain.user.User;
import com.college.api.domain.user.UserRepository;
import com.college.api.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RolePermissionRepository rolePermissionRepository;

    public record LoginResult(
            String token,
            Integer userId,
            String username,
            String email,
            String phoneNumber,
            String ra,
            Integer roleId,
            String roleName,
            List<String> permissions
    ) {}

    @Transactional(readOnly = true)
    public LoginResult login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        List<String> permissions = rolePermissionRepository.findByRoleId(user.getRole().getId())
                .stream()
                .map(rp -> rp.getPermission().getName())
                .toList();
        String token = jwtService.generateToken(user.getUsername(), user.getId(), user.getRole().getName(), permissions);
        return new LoginResult(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRa(),
                user.getRole().getId(),
                user.getRole().getName(),
                permissions
        );
    }
}
