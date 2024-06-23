package com.desbugando_backend.api.domain.comentarios;

import com.desbugando_backend.api.domain.usuarios.RetornoUsuariosParaOutrosDTO;

import java.util.Date;
import java.util.UUID;

public record RetornoComentariosParaOutrosDTO(UUID id, RetornoUsuariosParaOutrosDTO usuario, String conteudo, Date dataCriacao, boolean possuiImagem, String urlImagem) {
}
