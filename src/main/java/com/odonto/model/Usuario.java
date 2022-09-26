package com.odonto.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @NotEmpty(message = "Login é obrigatorio")
    @Size(min = 5, max = 255, message = "O email deve conter entre 5 e 50 caracteres")
    @Column(unique = true)
    private String login;

    @NotEmpty(message = "A senha é obrigatória!")
    @Size(min = 5, max = 255, message = "A senha deve conter entre 5 e 255 caracteres")
    private String senha;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
