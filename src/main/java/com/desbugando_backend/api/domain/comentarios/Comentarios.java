package com.desbugando_backend.api.domain.comentarios;

import com.desbugando_backend.api.domain.comentarios.Comentarios;
import com.desbugando_backend.api.domain.comentarios.RetornoComentariosParaOutrosDTO;
import com.desbugando_backend.api.domain.postagens.Postagens;
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

@Table(name = "comentarios")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comentarios {
    @Id
    @GeneratedValue
    private UUID id;

    private String conteudo;

    @Column(name = "data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postagem_id")
    @JsonIgnore
    private Postagens postagens;

    @Column(name = "possui_imagem")
    private boolean possuiImagem;

    @Column(name = "url_imagem")
    private String urlImagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuarios usuarios;

    public boolean getPossuiImagem(){
        return this.possuiImagem;
    }

    public RetornoUsuariosParaOutrosDTO getUsuarios(){
        return this.usuarios.getDadosPrincipaisUsuario();
    }

    @JsonIgnore
    public RetornoComentariosParaOutrosDTO getDadosPrincipaisComentarios(){
        return new RetornoComentariosParaOutrosDTO(getId(),getUsuarios(),getConteudo(),getDataCriacao(), getPossuiImagem(),getUrlImagem());
    }
}