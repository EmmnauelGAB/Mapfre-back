package com.mapfre.mifel.vida.model.clm;

public enum TipoGenero {
    Femenino("Femenino"),
    Masculino("Masculino");

    private final String value;

    TipoGenero(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
