package br.com.bytefood.auth_users.service;

import br.com.bytefood.auth_users.dtos.LoginRequest;
import br.com.bytefood.auth_users.dtos.LoginResponse;
import br.com.bytefood.auth_users.dtos.RegistrationRequest;
import br.com.bytefood.response.Response;

public interface AuthService {

    Response<?> register(RegistrationRequest registrationRequest);
    Response<LoginResponse> login(LoginRequest loginRequest);
}
