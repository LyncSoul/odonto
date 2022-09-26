package com.odonto.enums;

public enum TypeEscola {

    FUNDAMENTAL("Fundamental"),
    MEDIO("Médio"),
    FUNDAMENTAL_MEDIO("Fundamental e Médio");

	private String descricao;
	
	TypeEscola(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

}
