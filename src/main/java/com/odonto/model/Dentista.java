package com.odonto.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@Table(name = "dentista")
public class Dentista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dentista")
    private Long id;
    private String cro;
    private String telefone;
    private String urlFotoPerfil;

    @Size(min = 3, message = "O nome deve possuir mais de 3 caracteres")
    @NotNull(message = "não pode ser null")
    @NotBlank(message = "Nome não pode estar vazio")
    private String nome;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

}
