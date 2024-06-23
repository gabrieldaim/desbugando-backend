package com.desbugando_backend.api.domain.usuarios;

import java.util.UUID;

public record RetornoUsuariosParaOutrosDTO(UUID id, String nome, String email, TiposUsuarios tipo,String urlLinkedin,String urlGithub,String urlFoto){
}
