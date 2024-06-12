package com.mapfre.mifel.vida.model.clm;

public class CLMPersona {
    private String sCOD_DOCUM;
    private String sTIP_DOCUM;
    private String sCOD_DOCUM_PADRE;
    private String sTIP_DOCUM_PADRE;
    private TipoPersona sTIP_PERSONA;
    private String sCVE_RFC;
    private String sOBSERVACIONES;
    private Direccion cDireccion;
    private String sTELEFONO;
    private String sEMAIL;

    public String getsCOD_DOCUM() {
        return sCOD_DOCUM;
    }

    public void setsCOD_DOCUM(String sCOD_DOCUM) {
        this.sCOD_DOCUM = sCOD_DOCUM;
    }

    public String getsTIP_DOCUM() {
        return sTIP_DOCUM;
    }

    public void setsTIP_DOCUM(String sTIP_DOCUM) {
        this.sTIP_DOCUM = sTIP_DOCUM;
    }

    public String getsCOD_DOCUM_PADRE() {
        return sCOD_DOCUM_PADRE;
    }

    public void setsCOD_DOCUM_PADRE(String sCOD_DOCUM_PADRE) {
        this.sCOD_DOCUM_PADRE = sCOD_DOCUM_PADRE;
    }

    public String getsTIP_DOCUM_PADRE() {
        return sTIP_DOCUM_PADRE;
    }

    public void setsTIP_DOCUM_PADRE(String sTIP_DOCUM_PADRE) {
        this.sTIP_DOCUM_PADRE = sTIP_DOCUM_PADRE;
    }

    public TipoPersona getsTIP_PERSONA() {
        return sTIP_PERSONA;
    }

    public void setsTIP_PERSONA(TipoPersona sTIP_PERSONA) {
        this.sTIP_PERSONA = sTIP_PERSONA;
    }

    public String getsCVE_RFC() {
        return sCVE_RFC;
    }

    public void setsCVE_RFC(String sCVE_RFC) {
        this.sCVE_RFC = sCVE_RFC;
    }

    public String getsOBSERVACIONES() {
        return sOBSERVACIONES;
    }

    public void setsOBSERVACIONES(String sOBSERVACIONES) {
        this.sOBSERVACIONES = sOBSERVACIONES;
    }

    public Direccion getcDireccion() {
        return cDireccion;
    }

    public void setcDireccion(Direccion cDireccion) {
        this.cDireccion = cDireccion;
    }

    public String getsTELEFONO() {
        return sTELEFONO;
    }

    public void setsTELEFONO(String sTELEFONO) {
        this.sTELEFONO = sTELEFONO;
    }

    public String getsEMAIL() {
        return sEMAIL;
    }

    public void setsEMAIL(String sEMAIL) {
        this.sEMAIL = sEMAIL;
    }
}
