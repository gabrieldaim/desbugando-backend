package com.desbugando_backend.api.controller;

import com.desbugando_backend.api.domain.matriculas.DeletarMatriculaDTO;
import com.desbugando_backend.api.domain.matriculas.MatriculaCriacaoDTO;
import com.desbugando_backend.api.domain.matriculas.Matriculas;
import com.desbugando_backend.api.domain.turmas.DeletarTurmaDTO;
import com.desbugando_backend.api.domain.turmas.Turmas;
import com.desbugando_backend.api.domain.turmas.TurmasCriacaoDTO;
import com.desbugando_backend.api.domain.usuarios.TiposUsuarios;
import com.desbugando_backend.api.domain.usuarios.Usuarios;
import com.desbugando_backend.api.infra.security.CustomUserDetailsService;
import com.desbugando_backend.api.infra.security.TokenService;
import com.desbugando_backend.api.repositories.MatriculasRepository;
import com.desbugando_backend.api.repositories.TurmasRepository;
import com.desbugando_backend.api.repositories.UsuariosRepository;
import com.desbugando_backend.api.util.DataHoraAtual;
import com.desbugando_backend.api.util.InformacoesToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/matricula")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculasRepository matriculasRepository;
    private final UsuariosRepository usuariosRepository;
    private final TurmasRepository turmasRepository;
    private final TokenService tokenService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    DataHoraAtual dataHoraAtual;

    @PostMapping("/criar")
    public ResponseEntity criar(@RequestBody MatriculaCriacaoDTO body){
        Usuarios usuarioToken = new InformacoesToken(tokenService,customUserDetailsService).getCurrentUser();

        if (usuarioToken.getTipo() == TiposUsuarios.ADMIN) {
            Turmas turma = turmasRepository.findById(body.idTurma()).orElseThrow(() -> new RuntimeException("Turma não encontrada"));
            Usuarios usuario = usuariosRepository.findById(body.idUsuario()).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

            Matriculas matricula = new Matriculas();
            matricula.setTurma(turma);
            matricula.setUsuario(usuario);
            matricula.setDataMatricula(dataHoraAtual.getDataCriacao());
            matriculasRepository.save(matricula);

            return ResponseEntity.ok("matricula realiada com sucesso!");
        } else {
            return ResponseEntity.unprocessableEntity().body("seu usuário não tem essa permissão.");
        }
    }

    @DeleteMapping("/deletar")
    public ResponseEntity deletar(@RequestBody DeletarMatriculaDTO body) {
        Usuarios usuarioToken = new InformacoesToken(tokenService,customUserDetailsService).getCurrentUser();

        if (usuarioToken.getTipo() == TiposUsuarios.ADMIN){
            Matriculas matriculaDeletada = matriculasRepository.VerificarCadastroAluno(body.idUsuario(),body.idTurma());
            if(matriculaDeletada != null){
                matriculasRepository.DeletaCadastroAluno(body.idUsuario(),body.idTurma());
                return ResponseEntity.ok("Matricula deletada com sucesso");
            }
            return ResponseEntity.unprocessableEntity().body("Matricula não existe.");
        }
        return ResponseEntity.ok("Seu usuário não possui essa permissão");
    }
}
