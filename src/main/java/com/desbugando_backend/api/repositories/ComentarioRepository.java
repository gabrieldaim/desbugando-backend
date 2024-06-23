package com.desbugando_backend.api.repositories;

import com.desbugando_backend.api.domain.comentarios.Comentarios;
import com.desbugando_backend.api.domain.matriculas.Matriculas;
import com.desbugando_backend.api.domain.postagens.Postagens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ComentarioRepository extends JpaRepository<Comentarios, UUID> {
    @Query("SELECT c FROM Comentarios c WHERE c.postagens.id = :id")
    List<Comentarios> listarComentariosPorPostagemId(UUID id);
}
