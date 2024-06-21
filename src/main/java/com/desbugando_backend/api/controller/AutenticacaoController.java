package com.desbugando_backend.api.controller;

import com.desbugando_backend.api.domain.usuarios.*;
import com.desbugando_backend.api.infra.security.TokenService;
import com.desbugando_backend.api.repositories.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/autenticacao")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginUsuarioDTO body){
        Usuarios usuario = usuariosRepository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        String token = tokenService.generateToken(usuario);

        if (usuario.getPrimeiroAcesso()){
            if (body.senha().equals(usuario.getSenhaGenerica())){
                return ResponseEntity.ok(new RetornoLoginDTO(usuario.getNome(),usuario.getEmail(),token));
            }
            return ResponseEntity.badRequest().body("senha incorreta.");
        }
        if(passwordEncoder.matches(body.senha(), usuario.getSenha())){
            return ResponseEntity.ok(new RetornoLoginDTO(usuario.getNome(),usuario.getEmail(),token));
        }
        return ResponseEntity.badRequest().body("senha incorreta.");
    }

    @PostMapping("/criar")
    public ResponseEntity criar(@RequestBody CriarLoginDTO body){
        Optional<Usuarios> usuario = usuariosRepository.findByEmail(body.email());

        if (usuario.isEmpty()){
            Usuarios novoUsuario = new Usuarios();
            novoUsuario.setEmail(body.email());
            novoUsuario.setNome(body.nome());
            novoUsuario.setTipo(body.tipo());
            novoUsuario.setSenhaGenerica();
            usuariosRepository.save(novoUsuario);

            return ResponseEntity.ok(new RetornoCriacaoLoginDTO(novoUsuario.getId(),novoUsuario.getNome(),novoUsuario.getSenhaGenerica()));

        }

        return ResponseEntity.badRequest().body("Email já existente.");
    }

    @PutMapping("/atualizarSenha")
    public ResponseEntity atualizar(@RequestBody atualizarSenhaDTO body) {
        Usuarios usuario = usuariosRepository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));;

        if (body.isTrocaDeSenhaGenerica()) {
            usuario.setSenhaGenerica();
            usuario.setPrimeiroAcesso(true);
            usuario.setSenha(null);
            usuariosRepository.save(usuario);
            return ResponseEntity.ok(new RetornoCriacaoLoginDTO(usuario.getId(), usuario.getNome(), usuario.getSenhaGenerica()));
        }

        usuario.setSenhaGenerica(null);
        usuario.setPrimeiroAcesso(false);
        usuario.setSenha(passwordEncoder.encode(body.novaSenha()));
        usuariosRepository.save(usuario);
        return ResponseEntity.ok("Senha alterada com suscesso!");
        }

}
