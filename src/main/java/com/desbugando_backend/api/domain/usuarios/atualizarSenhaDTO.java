package com.desbugando_backend.api.domain.usuarios;

public record atualizarSenhaDTO(String email, Boolean isTrocaDeSenhaGenerica, String novaSenha) {
}
