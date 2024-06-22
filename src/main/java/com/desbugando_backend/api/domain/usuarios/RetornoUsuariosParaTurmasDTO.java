package com.desbugando_backend.api.domain.usuarios;

import java.util.UUID;

public record RetornoUsuariosParaTurmasDTO (UUID id,String nome,String email,TiposUsuarios tipo){
}
