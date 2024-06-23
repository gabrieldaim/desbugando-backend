package com.desbugando_backend.api.domain.postagens;

import com.desbugando_backend.api.domain.comentarios.RetornoComentariosParaOutrosDTO;
import com.desbugando_backend.api.domain.usuarios.RetornoUsuariosParaOutrosDTO;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record RetornoPostagensParaOutrosDTO(UUID id, RetornoUsuariosParaOutrosDTO usuario, String conteudo, Date dataCriacao, boolean possuiImagem, String urlImagem,
                                            List<RetornoComentariosParaOutrosDTO> comentarios) {
}
