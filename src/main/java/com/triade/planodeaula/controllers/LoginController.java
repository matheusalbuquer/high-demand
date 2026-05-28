package com.triade.planodeaula.controllers;

import com.triade.planodeaula.dtos.auth.LoginRequestDTO;
import com.triade.planodeaula.dtos.auth.LoginResponseDTO;
import com.triade.planodeaula.entites.User;
import com.triade.planodeaula.repositories.UserRepository;
import com.triade.planodeaula.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping
    public ResponseEntity<LoginResponseDTO> login (@RequestBody  LoginRequestDTO dto){

        String token = authenticationService.getToken(dto);

        User u = userRepository.findByEmail(dto.email());

         LoginResponseDTO resp = new LoginResponseDTO(
               token,
               dto.email()
       );

        return ResponseEntity.ok(resp);
    }

}
