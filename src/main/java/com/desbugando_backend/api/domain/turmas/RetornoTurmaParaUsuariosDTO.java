package com.desbugando_backend.api.domain.turmas;

import com.desbugando_backend.api.domain.usuarios.TiposUsuarios;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public record RetornoTurmaParaUsuariosDTO (UUID id, String nome, Date dataCriacao){
}
