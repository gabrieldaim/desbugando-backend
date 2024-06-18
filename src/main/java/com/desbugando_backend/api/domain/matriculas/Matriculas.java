package com.desbugando_backend.api.domain.matriculas;

import com.desbugando_backend.api.domain.turmas.Turmas;
import com.desbugando_backend.api.domain.usuarios.Usuarios;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "matriculas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Matriculas {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuario;

    @ManyToOne
    @JoinColumn(name = "turma_id", nullable = false)
    private Turmas turma;

    @Column(name = "data_matricula", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataMatricula;
}
