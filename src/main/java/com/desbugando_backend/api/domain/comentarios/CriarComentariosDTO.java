package com.desbugando_backend.api.domain.comentarios;

import java.util.UUID;

public record CriarComentariosDTO(String conteudo, UUID postagemId, boolean possuiImagem, String urlImagem) {
}
