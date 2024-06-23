package com.desbugando_backend.api.domain.postagens;

import com.desbugando_backend.api.domain.comentarios.Comentarios;
import com.desbugando_backend.api.domain.comentarios.RetornoComentariosParaOutrosDTO;
import com.desbugando_backend.api.domain.turmas.RetornoTurmaDTO;
import com.desbugando_backend.api.domain.turmas.RetornoTurmaParaOutrosDTO;
import com.desbugando_backend.api.domain.turmas.Turmas;
import com.desbugando_backend.api.domain.usuarios.RetornoUsuariosParaOutrosDTO;
import com.desbugando_backend.api.domain.usuarios.Usuarios;
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

@Table(name = "postagens")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Postagens {
    @Id
    @GeneratedValue
    private UUID id;

    private String conteudo;

    @Column(name = "data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id")
    @JsonIgnore
    private Turmas turma;

    @Column(name = "possui_imagem")
    private boolean possuiImagem;

    @Column(name = "url_imagem")
    private String urlImagem;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "postagens", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentarios> comentarios;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuarios usuarios;

    public boolean getPossuiImagem(){
    return this.possuiImagem;
    }

    public RetornoUsuariosParaOutrosDTO getUsuarios(){
        return this.usuarios.getDadosPrincipaisUsuario();
    }

    public List<RetornoComentariosParaOutrosDTO> getComentarios(){
        return this.comentarios.stream()
                .map(comentario -> comentario.getDadosPrincipaisComentarios())
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public RetornoPostagensParaOutrosDTO getDadosPrincipaisPostagem(){
        return new RetornoPostagensParaOutrosDTO(getId(),getUsuarios(),getConteudo(),getDataCriacao(), getPossuiImagem(),getUrlImagem(), getComentarios());
    }
}