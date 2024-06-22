package com.desbugando_backend.api.domain.usuarios;

import com.desbugando_backend.api.domain.turmas.Turmas;

import java.util.List;

public record RetornoUsuariosDTO(List<Usuarios> usuarios) {
}
