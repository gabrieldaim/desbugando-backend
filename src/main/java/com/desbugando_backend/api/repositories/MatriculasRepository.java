package com.desbugando_backend.api.repositories;

import com.desbugando_backend.api.domain.matriculas.Matriculas;
import com.desbugando_backend.api.domain.postagens.Postagens;
import com.desbugando_backend.api.domain.usuarios.Usuarios;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MatriculasRepository extends JpaRepository<Matriculas, UUID> {
    List<Matriculas> findByUsuarioId(UUID usuarioId);

    @Query("select m from Matriculas m where m.usuario.id = :usuarioId and m.turma.id = :turmaId")
    Matriculas VerificarCadastroAluno(UUID usuarioId, UUID turmaId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Matriculas m WHERE m.usuario.id = :usuarioId AND m.turma.id = :turmaId")
    void DeletaCadastroAluno(UUID usuarioId, UUID turmaId);
}
