package com.desbugando_backend.api.controller;

import com.desbugando_backend.api.domain.matriculas.Matriculas;
import com.desbugando_backend.api.domain.turmas.*;
import com.desbugando_backend.api.domain.usuarios.*;
import com.desbugando_backend.api.infra.security.CustomUserDetailsService;
import com.desbugando_backend.api.infra.security.TokenService;
import com.desbugando_backend.api.repositories.MatriculasRepository;
import com.desbugando_backend.api.repositories.TurmasRepository;
import com.desbugando_backend.api.util.DataHoraAtual;
import com.desbugando_backend.api.util.InformacoesToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/turmas")
@RequiredArgsConstructor
public class TurmasController {

    private final TurmasRepository turmasRepository;
    private final MatriculasRepository matriculasRepository;
    private final TokenService tokenService;

    @Autowired
    private DataHoraAtual dataHoraAtual;

    @Autowired
    CustomUserDetailsService customUserDetailsService;


    @GetMapping("/consultar")
    public ResponseEntity<RetornoTurmaDTO> login(){
        InformacoesToken informacoesToken = new InformacoesToken(tokenService,customUserDetailsService);
        Usuarios usuario = informacoesToken.getCurrentUser();
        if (usuario.getTipo() == TiposUsuarios.ADMIN) {
            return ResponseEntity.ok(new RetornoTurmaDTO(turmasRepository.findAll()));
        } else if (usuario.getTipo() == TiposUsuarios.ALUNO) {
            List<Matriculas> matriculas = matriculasRepository.findByUsuarioId(usuario.getId());
            List<Turmas> turmas = matriculas.stream().map(Matriculas::getTurma).toList();
            return ResponseEntity.ok(new RetornoTurmaDTO(turmas));
        } else {
            throw new RuntimeException("Tipo de usuário não suportado");
        }
    }

    @PostMapping("/criar")
    public ResponseEntity criar(@RequestBody TurmasCriacaoDTO body){
        InformacoesToken informacoesToken = new InformacoesToken(tokenService,customUserDetailsService);
        Usuarios usuario = informacoesToken.getCurrentUser();
        if (usuario.getTipo() == TiposUsuarios.ADMIN) {
            Turmas novaTurma = new Turmas();
            novaTurma.setNome(body.nome());
            novaTurma.setDataCriacao(dataHoraAtual.getDataCriacao());
            turmasRepository.save(novaTurma);

            return ResponseEntity.ok("turma criada com sucesso!");
        } else {
            return ResponseEntity.unprocessableEntity().body("seu usuário não tem essa permissão.");
        }
    }

    @DeleteMapping ("/deletar")
    public ResponseEntity deletar(@RequestBody DeletarTurmaDTO body) {
        Usuarios usuarioToken = new InformacoesToken(tokenService,customUserDetailsService).getCurrentUser();

        if (usuarioToken.getTipo() == TiposUsuarios.ADMIN){
            Optional<Turmas> turmaDeletada = turmasRepository.findById(body.id());
            if(!turmaDeletada.isEmpty()){
                turmasRepository.deleteById(body.id());
                return ResponseEntity.ok("Turma deletada com sucesso");
            }
            return ResponseEntity.unprocessableEntity().body("Turma não existe.");
        }
        return ResponseEntity.ok("Seu usuário não possui essa permissão");
    }

    @PutMapping("/atualizarDados")
    public ResponseEntity atualizarDados(@RequestBody AtualizarTurmaDTO body) {
        Usuarios usuarioToken = new InformacoesToken(tokenService,customUserDetailsService).getCurrentUser();

        if (usuarioToken.getTipo() == TiposUsuarios.ADMIN){
            Turmas turma = turmasRepository.findById(body.id()).orElseThrow(() -> new RuntimeException("Turma não encontrada"));
            turma.setNome(body.nome());
            turmasRepository.save(turma);

            return ResponseEntity.ok("turma atualizada com sucesso!");
        }

        return ResponseEntity.ok("Seu usuário não possui essa permissão");
    }
}

