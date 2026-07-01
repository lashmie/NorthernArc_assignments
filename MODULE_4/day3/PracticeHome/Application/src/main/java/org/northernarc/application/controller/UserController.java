package org.northernarc.application.controller;

import lombok.RequiredArgsConstructor;
import org.northernarc.application.dto.UserRequestDTO;
import org.northernarc.application.dto.UserResponseDTO;
import org.northernarc.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
@Autowired
    private final UserService userService;
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @RequestBody UserRequestDTO dto){

        UserResponseDTO response =
                userService.createUser(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO>
    getUserById(@PathVariable Long id){

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO>
    updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO dto){

        return ResponseEntity.ok(
                userService.updateUser(id,dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteUser(@PathVariable Long id){

        userService.deleteUser(id);

        return ResponseEntity.ok(
                "User deleted successfully"
        );
    }

}
