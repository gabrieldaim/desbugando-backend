package com.desbugando_backend.api.util;

import com.desbugando_backend.api.domain.turmas.RetornoTurmaParaOutrosDTO;
import com.desbugando_backend.api.domain.usuarios.TiposUsuarios;
import com.desbugando_backend.api.domain.usuarios.Usuarios;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class IsMatriculado {
    public boolean isMatriculado(Usuarios usuario, UUID idTurma) {
        List<RetornoTurmaParaOutrosDTO> turmas = usuario.getMatriculas();
        return usuario.getTipo() == TiposUsuarios.ADMIN || turmas.stream().anyMatch(turma -> turma.id().equals(idTurma));
    }
}
