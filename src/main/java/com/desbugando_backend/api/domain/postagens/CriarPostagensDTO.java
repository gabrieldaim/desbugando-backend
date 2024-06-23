package com.desbugando_backend.api.domain.postagens;

import java.util.UUID;

public record CriarPostagensDTO(String conteudo, UUID idTurma, boolean possuiImagem,String urlImagem) {
}
