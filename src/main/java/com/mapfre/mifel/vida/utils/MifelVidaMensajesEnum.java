package com.mapfre.mifel.vida.utils;

public enum MifelVidaMensajesEnum {
    SUCCESS_OPERATION(0, "operacion realizada exitosamente"),
    QUOTE_NULL(201, "No contiene el objeto quote"),
    POLICIYDATA_EMPTY(202, "No contiene el objeto policyData"),
    POLICIYDATA_INVALID(203, "El objeto policyData no contiene los valores obligatorios"),
    VARIABLESDATA_EMPTY(204, "No contiene el objeto datosVariables"),
    CAMPO_EMPTY(205, "No contiene el objeto Campo"),
    COVERAGES_EMPTY(206, "No contiene el objeto coverages o esta vacio"),
    COVERAGES_INVALID(207, "No contiene el objeto coverages los valores obligatorios"),
    NOT_AUTHORIZED(208, "El agentCode o el contactCode no estan dentro del la info del usuario"),
    GENERATE_POLICY_NULL(209, "No contiene el objeto generatePolicy"),
    QUOTE_DATA_NULL(210, "No contiene el objeto quoteData"),
    QUOTE_DATA_INVALID(211, "No contiene el objeto quoteData los valores obligatorios"),
    POLICY_DATA_PROJECT_NULL(212, "No contiene el objeto policyData"),
    POLICY_DATA_PROJECT_INVALID(213, "No contiene el objeto policyData los valores obligatorios"),
    ADDRESS_RISK_NULL(214, "No contiene el objeto address"),
    ADDRESS_RISK_INVALID(215, "No contiene el objeto address los valores obligatorios"),
    PERSONS_NULL(216, "No contiene el objeto persons o esta vacio"),
    PERSONS_INVALID(217, "No contiene el objeto persons no los valores obligatorios de acuerdo al tipo de persona"),
    ACCOUNT_INVALID(218, "No contiene el objeto bankAccounts no los valores obligatorios de acuerdo al tipo de cuenta"),
    GENERATE_POLICY_ERROR(219, "La operacion no pudo ser realizada exitosamente"),
    REQUEST_NULL(220,"No hay datos"),
    EXAMPLE_ID_NULL(33,"Id no puede ser nulo");

    MifelVidaMensajesEnum(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje=mensaje;
    }

    private int codigo;
    private String mensaje;
    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
