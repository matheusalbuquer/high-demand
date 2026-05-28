package com.triade.planodeaula.services;

import com.triade.planodeaula.dtos.user.UserRequestDTO;
import com.triade.planodeaula.dtos.user.UserResponseDTO;
import com.triade.planodeaula.entites.User;
import com.triade.planodeaula.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService (UserRepository usuarioRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository =  usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO create (UserRequestDTO dto){

        User emailVerify = usuarioRepository.findByEmail(dto.email());

        if(emailVerify != null){
            throw new RuntimeException("Email ja esta cadastrado no nosso sistema");
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));

        usuarioRepository.save(user);

        return new UserResponseDTO(user.getId(),user.getName(),user.getEmail()) ;
    }

}
