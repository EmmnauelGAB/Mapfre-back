package com.mapfre.mifel.vida.model.clm;

public enum TipoPersona {
    Moral("S"), // 0x0000004E
    Fisica("N"); // 0x00000053

    private final String value;

    TipoPersona(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
