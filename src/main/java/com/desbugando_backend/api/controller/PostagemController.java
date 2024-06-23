package com.desbugando_backend.api.controller;

import com.desbugando_backend.api.domain.matriculas.DeletarMatriculaDTO;
import com.desbugando_backend.api.domain.matriculas.MatriculaCriacaoDTO;
import com.desbugando_backend.api.domain.matriculas.Matriculas;
import com.desbugando_backend.api.domain.postagens.*;
import com.desbugando_backend.api.domain.turmas.DeletarTurmaDTO;
import com.desbugando_backend.api.domain.turmas.RetornoTurmaDTO;
import com.desbugando_backend.api.domain.turmas.Turmas;
import com.desbugando_backend.api.domain.turmas.TurmasCriacaoDTO;
import com.desbugando_backend.api.domain.usuarios.TiposUsuarios;
import com.desbugando_backend.api.domain.usuarios.Usuarios;
import com.desbugando_backend.api.infra.security.CustomUserDetailsService;
import com.desbugando_backend.api.infra.security.TokenService;
import com.desbugando_backend.api.repositories.MatriculasRepository;
import com.desbugando_backend.api.repositories.PostagemRepository;
import com.desbugando_backend.api.repositories.TurmasRepository;
import com.desbugando_backend.api.repositories.UsuariosRepository;
import com.desbugando_backend.api.util.DataHoraAtual;
import com.desbugando_backend.api.util.InformacoesToken;
import com.desbugando_backend.api.util.IsMatriculado;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/postagem")
@RequiredArgsConstructor
public class PostagemController {

    private final PostagemRepository postagemRepository;
    private final UsuariosRepository usuariosRepository;
    private final TurmasRepository turmasRepository;
    private final TokenService tokenService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    DataHoraAtual dataHoraAtual;
    @Autowired
    IsMatriculado isMatriculado;


    @GetMapping("/listar")
    public ResponseEntity<?> listar(@RequestBody GetPostagensDTO body){
        Usuarios usuarioToken = new InformacoesToken(tokenService,customUserDetailsService).getCurrentUser();

        if (isMatriculado.isMatriculado(usuarioToken,body.turmaId())) {
            List<Postagens> postagens = postagemRepository.listarPostagensPorTurmaId(body.turmaId());
            return ResponseEntity.ok(new RetornoPostagensDTO(postagens));
        }
        return ResponseEntity.unprocessableEntity().body("usuário não matriculado na turma informada");
    }

    @PostMapping("/criar")
    public ResponseEntity criar(@RequestBody CriarPostagensDTO body){
        Usuarios usuarioToken = new InformacoesToken(tokenService,customUserDetailsService).getCurrentUser();

            Turmas turma = turmasRepository.findById(body.idTurma()).orElseThrow(() -> new RuntimeException("Turma não encontrada"));

            Postagens postagens = new Postagens();
            postagens.setTurma(turma);
            postagens.setUsuarios(usuarioToken);
            postagens.setConteudo(body.conteudo());
            postagens.setDataCriacao(dataHoraAtual.getDataCriacao());
            postagens.setPossuiImagem(body.possuiImagem());
            postagens.setUrlImagem(body.urlImagem());
            postagemRepository.save(postagens);

            return ResponseEntity.ok("Postagem criada com sucesso!");
    }

    @DeleteMapping("/deletar")
    public ResponseEntity deletar(@RequestBody DeletarPostagemDTO body) {
        Usuarios usuarioToken = new InformacoesToken(tokenService,customUserDetailsService).getCurrentUser();
        Postagens postagemDeletada = postagemRepository.findById(body.idPostagem()).orElseThrow(() -> new RuntimeException("Postagem não encontrada"));

        if (usuarioToken.getTipo() == TiposUsuarios.ADMIN || usuarioToken.getId().equals(postagemDeletada.getUsuarios().id())){

                postagemRepository.deleteById(body.idPostagem());
                return ResponseEntity.ok("Postagem deletada com sucesso");

        }
        return ResponseEntity.unprocessableEntity().body("Seu usuário não possui essa permissão");
    }
}
