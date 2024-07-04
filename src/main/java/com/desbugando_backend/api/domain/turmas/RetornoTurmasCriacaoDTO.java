package com.desbugando_backend.api.domain.turmas;

import java.util.Date;
import java.util.UUID;

public record RetornoTurmasCriacaoDTO(String nome, UUID id, Date dataCriacao){
}
