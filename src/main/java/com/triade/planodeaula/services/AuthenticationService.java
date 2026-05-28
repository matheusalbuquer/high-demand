package com.triade.planodeaula.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.triade.planodeaula.dtos.auth.LoginRequestDTO;
import com.triade.planodeaula.entites.User;
import com.triade.planodeaula.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class AuthenticationService {

    private final UserRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;




    public AuthenticationService(UserRepository usuarioRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }


    public UserDetails loadByUsername (String email) throws UsernameNotFoundException{
        User user = usuarioRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        return user;
    }

    public String getToken(LoginRequestDTO dto){

        //Get object to email
        User user = usuarioRepository.findByEmail(dto.email());

        //Verify if user is not null
        if (user == null) {
            throw new RuntimeException("User not found :)");
        }

        //Valid password is correct
        if(!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new BadCredentialsException("Credentials inválid");
        }

        //set token
        return tokenService.generateToken(user);

    }



}