package com.desbugando_backend.api.repositories;

import com.desbugando_backend.api.domain.turmas.Turmas;
import com.desbugando_backend.api.domain.usuarios.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TurmasRepository extends JpaRepository<Turmas, UUID> {

    @Query("""
            SELECT t FROM Turmas t WHERE t.id IN :ids
            """)
    List<Turmas> listarTurmas(List<UUID> ids);
}
