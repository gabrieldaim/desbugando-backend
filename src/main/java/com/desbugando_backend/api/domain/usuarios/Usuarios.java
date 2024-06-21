package com.desbugando_backend.api.domain.usuarios;

import com.desbugando_backend.api.domain.matriculas.Matriculas;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.UUID;

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

    public void setSenhaGenerica(){
        String randomString = RandomStringUtils.random(5, true, true);
        this.senhaGenerica = randomString;
    }
}


