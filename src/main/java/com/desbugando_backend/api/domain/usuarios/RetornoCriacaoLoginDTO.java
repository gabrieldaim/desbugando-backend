package com.desbugando_backend.api.domain.usuarios;

import java.util.UUID;

public record RetornoCriacaoLoginDTO(UUID uuid,String nome, String senhaGenerica) {
}
