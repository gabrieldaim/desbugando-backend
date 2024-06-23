package com.desbugando_backend.api.repositories;

import com.desbugando_backend.api.domain.matriculas.Matriculas;
import com.desbugando_backend.api.domain.postagens.Postagens;
import com.desbugando_backend.api.domain.turmas.Turmas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PostagemRepository extends JpaRepository<Postagens, UUID> {
    @Query("SELECT p FROM Postagens p WHERE p.turma.id = :id")
    List<Postagens> listarPostagensPorTurmaId(UUID id);
}
