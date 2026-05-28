package com.triade.planodeaula.controllers;

import com.triade.planodeaula.dtos.user.UserRequestDTO;
import com.triade.planodeaula.dtos.user.UserResponseDTO;
import com.triade.planodeaula.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registrar")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create (@RequestBody UserRequestDTO dto){
        UserResponseDTO create = userService.create(dto);
        return ResponseEntity.ok(create);
    }
}
