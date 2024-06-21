package com.desbugando_backend.api.repositories;

import com.desbugando_backend.api.domain.usuarios.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuariosRepository extends JpaRepository<Usuarios, UUID> {

    Optional<Usuarios> findByEmail(String email);
}
