package com.odonto.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DentistaDTO {

    @JsonIgnore
    private Long id;

    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
