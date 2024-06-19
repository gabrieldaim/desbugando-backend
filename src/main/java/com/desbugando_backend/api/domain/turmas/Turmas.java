package com.desbugando_backend.api.domain.turmas;

import com.desbugando_backend.api.domain.matriculas.Matriculas;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table(name = "turmas")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Turmas {
    @Id
    @GeneratedValue
    private UUID id;

    private String nome;

    @Column(name = "data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;


    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Matriculas> matriculas;
}
