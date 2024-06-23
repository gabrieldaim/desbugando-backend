package com.desbugando_backend.api.domain.turmas;

import java.util.Date;
import java.util.UUID;

public record RetornoTurmaParaOutrosDTO(UUID id, String nome, Date dataCriacao){
}
