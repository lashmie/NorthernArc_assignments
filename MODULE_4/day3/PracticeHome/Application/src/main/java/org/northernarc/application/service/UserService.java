package org.northernarc.application.service;

import org.northernarc.application.dto.UserRequestDTO;
import org.northernarc.application.dto.UserResponseDTO;
import org.northernarc.application.model.User;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO dto);

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(Long id,
                               UserRequestDTO dto);

    void deleteUser(Long id);

}
