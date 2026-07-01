package org.northernarc.application.service;

import org.northernarc.application.dto.UserRequestDTO;
import org.northernarc.application.dto.UserResponseDTO;
import org.northernarc.application.exception.UserNotFoundException;
import org.northernarc.application.model.User;
import org.northernarc.application.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserResponseDTO createUser(
            UserRequestDTO dto) {

        if(userRepo.findByEmail(dto.getEmail())
                .isPresent()){

            throw new RuntimeException(
                    "Email already exists");
        }

        User user = mapToEntity(dto);

        User savedUser =
                userRepo.save(user);

        return mapToResponseDTO(savedUser);
    }


    private User mapToEntity(
            UserRequestDTO dto){

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());

        return user;
    }
    @Override
    public UserResponseDTO getUserById(
            Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                "User not found with id "
                                        + id
                        ));

        return mapToResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        return userRepo.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }


    @Override
    public UserResponseDTO updateUser(
            Long id,
            UserRequestDTO dto) {

        User user = userRepo.findById(id)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                "User not found with id "
                                        + id
                        ));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());

        User updatedUser =
                userRepo.save(user);

        return mapToResponseDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                "User not found with id "
                                        + id
                        ));

        userRepo.delete(user);
    }

    private UserResponseDTO mapToResponseDTO(
            User user){

        UserResponseDTO dto =
                new UserResponseDTO();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        return dto;
    }
}
