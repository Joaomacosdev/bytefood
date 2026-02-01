package br.com.bytefood.auth_users.service;

import br.com.bytefood.auth_users.dtos.UserDTO;
import br.com.bytefood.auth_users.entity.User;
import br.com.bytefood.response.Response;

import java.util.List;

public interface UserService {

    User getCurrentLoggedInUser();
    Response<List<UserDTO>> getAllUsers();
    Response<UserDTO> getOwnAccountDetails();
    Response<?> updateOwnAccount(UserDTO userDTO);
    Response<?> deactivateOwnAccount();


}
