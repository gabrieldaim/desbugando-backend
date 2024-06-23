package com.desbugando_backend.api.domain.turmas;

import com.desbugando_backend.api.domain.matriculas.Matriculas;
import com.desbugando_backend.api.domain.postagens.Postagens;
import com.desbugando_backend.api.domain.usuarios.RetornoUsuariosParaOutrosDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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


    @OneToMany(fetch = FetchType.LAZY,mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Matriculas> matriculas;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Postagens> postagens;

    public List<RetornoUsuariosParaOutrosDTO> getMatriculas(){
        return this.matriculas.stream()
                .map(matricula -> matricula.getUsuario().getDadosPrincipaisUsuario())
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public RetornoTurmaParaOutrosDTO getDadosPrincipaisTurma(){
        return new RetornoTurmaParaOutrosDTO(getId(),getNome(),getDataCriacao());
    }
}
