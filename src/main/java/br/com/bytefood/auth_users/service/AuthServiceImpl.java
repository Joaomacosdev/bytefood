package br.com.bytefood.auth_users.service;

import br.com.bytefood.auth_users.dtos.LoginRequest;
import br.com.bytefood.auth_users.dtos.LoginResponse;
import br.com.bytefood.auth_users.dtos.RegistrationRequest;
import br.com.bytefood.auth_users.entity.User;
import br.com.bytefood.auth_users.repository.UserRepository;
import br.com.bytefood.exception.BadRequestException;
import br.com.bytefood.exception.NotFoundException;
import br.com.bytefood.response.Response;
import br.com.bytefood.role.entity.Role;
import br.com.bytefood.role.repository.RoleRepository;
import br.com.bytefood.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;


    @Override
    public Response<?> register(RegistrationRequest registrationRequest) {

        log.info("INSIDE register()");

        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new BadRequestException("Email Already exists");
        }

        List<Role> userRoles;

        if (registrationRequest.getRoles() != null && !registrationRequest.getRoles().isEmpty()) {
            userRoles = registrationRequest.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName.toUpperCase())
                            .orElseThrow(() -> new NotFoundException("Role with: " + roleName + " not found")))
                    .toList();
        }else {
            Role defaultRole = roleRepository.findByName("CUSTOMER")
                    .orElseThrow(() -> new NotFoundException("Default CUSTOMER role not found"));

            userRoles = List.of(defaultRole);
        }

        log.info("PasswordEncoder instance: {}", passwordEncoder);


        User userToSave = User.builder()
                .name(registrationRequest.getName())
                .email(registrationRequest.getEmail())
                .phoneNumber(registrationRequest.getPhoneNumber())
                .address(registrationRequest.getAddress())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .roles(userRoles)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(userToSave);

        log.info("User registered successfully");


        return Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("User registered successfully")
                .build();


    }

    @Override
    public Response<LoginResponse> login(LoginRequest loginRequest) {

        log.info("INSIDE login()");

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("Invalid Email"));

        if (!user.isActive()){
            throw new NotFoundException("Account not active, please contact customer support");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new BadRequestException("Invalid Password");
        }

        String token = jwtUtils.generateToke(user.getEmail());

        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setRoles(roleNames);

        return Response.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login successfully")
                .data(loginResponse)
                .build();
    }
}
