package com.desbugando_backend.api.controller;

import com.desbugando_backend.api.domain.comentarios.*;
import com.desbugando_backend.api.domain.postagens.*;
import com.desbugando_backend.api.domain.turmas.Turmas;
import com.desbugando_backend.api.domain.usuarios.TiposUsuarios;
import com.desbugando_backend.api.domain.usuarios.Usuarios;
import com.desbugando_backend.api.infra.security.CustomUserDetailsService;
import com.desbugando_backend.api.infra.security.TokenService;
import com.desbugando_backend.api.repositories.ComentarioRepository;
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

@RestController
@RequestMapping("/comentario")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioRepository comentarioRepository;
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
    public ResponseEntity<?> listar(@RequestBody GetComentariosDTO body){
        Usuarios usuarioToken = new InformacoesToken(tokenService,customUserDetailsService).getCurrentUser();
        Postagens postagem = postagemRepository.findById(body.postagemId()).orElseThrow(() -> new RuntimeException("Postagem não encontrada"));
        if (isMatriculado.isMatriculado(usuarioToken,postagem.getTurma().getId())) {
            List<Comentarios> comentarios = comentarioRepository.listarComentariosPorPostagemId(body.postagemId());
            return ResponseEntity.ok(new RetornoComentariosDTO(comentarios));
        }
        return ResponseEntity.unprocessableEntity().body("usuário não matriculado na turma informada");
    }

    @PostMapping("/criar")
    public ResponseEntity criar(@RequestBody CriarComentariosDTO body){
        Usuarios usuarioToken = new InformacoesToken(tokenService,customUserDetailsService).getCurrentUser();

            Postagens postagem = postagemRepository.findById(body.postagemId()).orElseThrow(() -> new RuntimeException("Postagem não encontrada"));

            Comentarios comentarios = new Comentarios();
            comentarios.setPostagens(postagem);
            comentarios.setUsuarios(usuarioToken);
            comentarios.setConteudo(body.conteudo());
            comentarios.setDataCriacao(dataHoraAtual.getDataCriacao());
            comentarios.setPossuiImagem(body.possuiImagem());
            comentarios.setUrlImagem(body.urlImagem());
            comentarioRepository.save(comentarios);

            return ResponseEntity.ok("Comentario criado com sucesso!");
    }

    @DeleteMapping("/deletar")
    public ResponseEntity deletar(@RequestBody DeletarComentarioDTO body) {
        Usuarios usuarioToken = new InformacoesToken(tokenService,customUserDetailsService).getCurrentUser();
        Comentarios comentarioDeletado = comentarioRepository.findById(body.idComentario()).orElseThrow(() -> new RuntimeException("Comentario não encontrado"));

        if (usuarioToken.getTipo() == TiposUsuarios.ADMIN || usuarioToken.getId().equals(comentarioDeletado.getUsuarios().id())){

                comentarioRepository.deleteById(body.idComentario());
                return ResponseEntity.ok("Comentario deletado com sucesso");

        }
        return ResponseEntity.unprocessableEntity().body("Seu usuário não possui essa permissão");
    }
}
