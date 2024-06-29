package com.desbugando_backend.api.domain.usuarios;

public record RetornoLoginDTO(String nome,String email, String token,TiposUsuarios tipo,String url_foto) {
}
