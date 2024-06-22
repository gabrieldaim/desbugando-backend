package com.desbugando_backend.api.util;

import com.desbugando_backend.api.domain.usuarios.Usuarios;
import com.desbugando_backend.api.infra.security.CustomUserDetailsService;
import com.desbugando_backend.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InformacoesToken {

    private final TokenService tokenService;
    private final CustomUserDetailsService customUserDetailsService;

    private Usuarios currentUser;
    private String email;
    private UUID id;

    @Autowired
    public InformacoesToken(TokenService tokenService, CustomUserDetailsService customUserDetailsService) {
        this.tokenService = tokenService;
        this.customUserDetailsService = customUserDetailsService;
        this.currentUser = customUserDetailsService.getCurrentUser();
    }

    public UUID getID() {
        if (this.currentUser != null) {
            this.id = this.currentUser.getId();
        }
        return id;
    }

    public Usuarios getCurrentUser(){
        return currentUser;
    }
}
