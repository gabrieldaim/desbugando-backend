package com.desbugando_backend.api.domain.usuarios;

import com.desbugando_backend.api.domain.matriculas.Matriculas;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Matriculas> matriculas;
}
