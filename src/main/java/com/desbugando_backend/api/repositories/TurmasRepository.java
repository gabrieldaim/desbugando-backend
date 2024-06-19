package com.desbugando_backend.api.repositories;

import com.desbugando_backend.api.domain.turmas.Turmas;
import com.desbugando_backend.api.domain.usuarios.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TurmasRepository extends JpaRepository<Turmas, UUID> {
}
