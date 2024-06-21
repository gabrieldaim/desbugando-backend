package com.desbugando_backend.api.controller;

import com.desbugando_backend.api.domain.usuarios.Usuarios;
import com.desbugando_backend.api.infra.security.CustomUserDetailsService;
import com.desbugando_backend.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    TokenService tokenService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @GetMapping
    public ResponseEntity<Object> getUser(Authentication authentication){
        Usuarios currentUser = customUserDetailsService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(currentUser.getEmail());
    }
}
