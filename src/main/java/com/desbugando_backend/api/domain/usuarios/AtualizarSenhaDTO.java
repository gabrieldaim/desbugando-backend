package com.desbugando_backend.api.domain.usuarios;

public record AtualizarSenhaDTO(String email, Boolean isTrocaDeSenhaGenerica, String novaSenha) {
}
