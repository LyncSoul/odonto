package com.odonto.model;

import lombok.Data;

@Data
public class AuthenticationResponseProf {

    private Dentista dentista;
    private String jwt;

    public AuthenticationResponseProf() {

    }
    public AuthenticationResponseProf(Dentista dentista, String jwt) {
        this.dentista = dentista;
        this.jwt = jwt;
    }
}
