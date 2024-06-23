package com.desbugando_backend.api.domain.usuarios;

import com.desbugando_backend.api.domain.comentarios.Comentarios;
import com.desbugando_backend.api.domain.matriculas.Matriculas;
import com.desbugando_backend.api.domain.postagens.Postagens;
import com.desbugando_backend.api.domain.turmas.RetornoTurmaParaOutrosDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Table(name = "usuarios")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuarios {
    @Id
    @GeneratedValue
    private UUID id;

    private String email;
    private String nome;
    private String senha;

    @Enumerated(EnumType.STRING)
    private TiposUsuarios tipo;

    @Column(name = "url_foto")
    private String urlFoto;

    @Column(name = "url_linkedin")
    private String urlLinkedin;

    @Column(name = "url_github")
    private String urlGithub;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Matriculas> matriculas;

    @Column(name = "senha_generica")
    private String senhaGenerica;

    @Column(name = "primeiro_acesso")
    private Boolean primeiroAcesso = true;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarios", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Postagens> postagens;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarios", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentarios> comentarios;

    public void setSenhaGenerica(){
        String randomString = RandomStringUtils.random(5, true, true);
        this.senhaGenerica = randomString;
    }

    public List<RetornoTurmaParaOutrosDTO> getMatriculas(){
        return this.matriculas.stream()
                .map(matricula -> matricula.getTurma().getDadosPrincipaisTurma())
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public RetornoUsuariosParaOutrosDTO getDadosPrincipaisUsuario(){
        return new RetornoUsuariosParaOutrosDTO(getId(),getNome(),getEmail(),getTipo(),getUrlLinkedin(),getUrlGithub(),getUrlFoto());
    }

    @JsonIgnore
    public String getSenhaGenerica(){
        return this.senhaGenerica;
    }
}


