package com.desbugando_backend.api.controller;

import com.desbugando_backend.api.domain.usuarios.*;
import com.desbugando_backend.api.infra.security.CustomUserDetailsService;
import com.desbugando_backend.api.infra.security.TokenService;
import com.desbugando_backend.api.repositories.UsuariosRepository;
import com.desbugando_backend.api.util.InformacoesToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    TokenService tokenService;
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    UsuariosRepository usuariosRepository;

    @GetMapping("/listar")
    public ResponseEntity listar() {
        Usuarios usuario = new InformacoesToken(tokenService,customUserDetailsService).getCurrentUser();
        if(usuario.getTipo() == TiposUsuarios.ADMIN){
            List<Usuarios> usuarios = usuariosRepository.findAll();
            return ResponseEntity.ok(new RetornoUsuariosDTO(usuarios));
        }
        return ResponseEntity.unprocessableEntity().body("Seu usuário não possui essa permissão");
    }

    @GetMapping("/testar")
    public ResponseEntity teste() {
        return ResponseEntity.ok("Autenticado com sucesso!");
    }

    @PutMapping("/atualizarDados")
    public ResponseEntity atualizarDados(@RequestBody AtualizarDadosUsuarioDTO body) {
        Usuarios usuario = new InformacoesToken(tokenService,customUserDetailsService).getCurrentUser();

        if (body.nome() != null){
            usuario.setNome(body.nome());
        }

        if (body.urlGithub() != null){
            usuario.setUrlGithub(body.urlGithub());
        }
        if (body.urlLinkedin() != null){
            usuario.setUrlLinkedin(body.urlLinkedin());
        }
        usuariosRepository.save(usuario);
        return ResponseEntity.ok("Dados atualizados com sucesso!");
    }

    @DeleteMapping ("/deletar")
    public ResponseEntity deletar(@RequestBody DeletarUsuarioDTO body) {
        Usuarios usuarioToken = new InformacoesToken(tokenService,customUserDetailsService).getCurrentUser();

        if (usuarioToken.getTipo() == TiposUsuarios.ADMIN){
            Optional<Usuarios> usuarioDeletado = usuariosRepository.findById(body.id());
            if(!usuarioDeletado.isEmpty()){
                usuariosRepository.deleteById(body.id());
                return ResponseEntity.ok("usuário deletado com sucesso");
            }
            return ResponseEntity.unprocessableEntity().body("Usuario não existe.");
        }
        return ResponseEntity.ok("Seu usuário não possui essa permissão");
    }

    @GetMapping("/senhaGenerica")
    public ResponseEntity getSenhaGenerica(@RequestBody GetSenhaGenericaDTO body) {
        Usuarios usuarioToken = new InformacoesToken(tokenService,customUserDetailsService).getCurrentUser();
        if(usuarioToken.getTipo() == TiposUsuarios.ADMIN){
            Usuarios usuario = usuariosRepository.findById(body.id()).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
            return ResponseEntity.ok(new RetornoSenhaGenericaDTO(usuario.getSenhaGenerica()));
        }
        return ResponseEntity.unprocessableEntity().body("Seu usuário não possui essa permissão");
    }
}
