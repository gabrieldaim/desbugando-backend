package com.desbugando_backend.api.domain.comentarios;

import com.desbugando_backend.api.domain.postagens.Postagens;

import java.util.List;

public record RetornoComentariosDTO(List<Comentarios> comentarios) {
}
