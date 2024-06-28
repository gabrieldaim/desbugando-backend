package com.desbugando_backend.api.controller;

import com.desbugando_backend.api.domain.usuarios.*;
import com.desbugando_backend.api.infra.security.CustomUserDetailsService;
import com.desbugando_backend.api.infra.security.TokenService;
import com.desbugando_backend.api.repositories.UsuariosRepository;
import com.desbugando_backend.api.util.InformacoesToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginUsuarioDTO body){
        Usuarios usuario = null;
        if (usuariosRepository.findByEmail(body.email()).isPresent()) {
            usuario = usuariosRepository.findByEmail(body.email()).get();
        } else {
            return ResponseEntity.badRequest().body("usuario ou senha incorreta.");
        }
        String token = tokenService.generateToken(usuario);

        if (usuario.getPrimeiroAcesso()){
            if (body.senha().equals(usuario.getSenhaGenerica())){
                return ResponseEntity.ok(new RetornoLoginDTO(usuario.getNome(),usuario.getEmail(),token));
            }
            return ResponseEntity.badRequest().body("usuario ou senha incorreta.");
        }
        if(passwordEncoder.matches(body.senha(), usuario.getSenha())){
            return ResponseEntity.ok(new RetornoLoginDTO(usuario.getNome(),usuario.getEmail(),token));
        }
        return ResponseEntity.badRequest().body("usuario ou senha incorreta.");
    }

    @PostMapping("/criar")
    public ResponseEntity criar(@RequestBody CriarLoginDTO body){
        Optional<Usuarios> usuario = usuariosRepository.findByEmail(body.email());
        InformacoesToken informacoesToken = new InformacoesToken(tokenService,customUserDetailsService);
        Usuarios usuarioToken = informacoesToken.getCurrentUser();
        if (usuarioToken.getTipo() == TiposUsuarios.ADMIN) {
            if (usuario.isEmpty()){
                Usuarios novoUsuario = new Usuarios();
                novoUsuario.setEmail(body.email());
                novoUsuario.setNome(body.nome());
                novoUsuario.setTipo(body.tipo());
                novoUsuario.setSenhaGenerica();
                usuariosRepository.save(novoUsuario);

                return ResponseEntity.ok(new RetornoCriacaoLoginDTO(novoUsuario.getId(),novoUsuario.getNome(),novoUsuario.getSenhaGenerica()));

            }else {
                return ResponseEntity.badRequest().body("Email já existente.");
            }
        }else {
            return ResponseEntity.unprocessableEntity().body("seu usuário não possui essa permissão.");
        }

    }

    @PutMapping("/atualizarSenha")
    public ResponseEntity atualizar(@RequestBody AtualizarSenhaDTO body) {
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
