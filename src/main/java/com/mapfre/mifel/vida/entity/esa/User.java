package com.mapfre.mifel.vida.entity.esa;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="G2009783_MMX")



@NamedStoredProcedureQuery(name = "User.altaTercero", procedureName = "dc_k_clm_gestion.p_alta_tercero", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_origen", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_usr", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_ramo", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_tip_benef", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cadena_xml", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "p_tip_docum", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "p_cod_docum", type = String.class) })

@NamedStoredProcedureQuery(name = "User.altaBeneficiario", procedureName = "dc_k_clm_gestion.p_alta_beneficiario", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.INOUT, name = "p_tip_docum", type = String.class),	//#1
        @StoredProcedureParameter(mode = ParameterMode.INOUT, name = "p_cod_docum", type = String.class),	//#2
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_mca_fisico", type = String.class),		//#3
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_nom_tercero", type = String.class),	//#4
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_ape1_tercero", type = String.class),	//#5
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_ape2_tercero", type = String.class),	//#6
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_parentesco", type = Integer.class),//#7
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_fec_nacimiento", type = Date.class),	//#8
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_nom_domicilio1", type = String.class),	//#9
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_nom_domicilio2", type = String.class),	//#10
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_num_exterior", type = String.class),	//#11
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_num_interior", type = String.class),	//#12
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_nom_colonia", type = String.class),	//#13
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_pais", type = String.class),		//#14
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_estado", type = String.class),		//#15
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_prov", type = String.class),		//#16
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_postal", type = String.class),		//#17
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_tlf_numero", type = String.class),		//#18
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_email", type = String.class) })		//#19

@NamedStoredProcedureQuery(name = "User.setCotizacionEmision", procedureName = "EM_K_GEN_WS.P_LANZA_PROCESO2_XML", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "pcadena", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "pnum_poliza", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "ptxt_error", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "pidcontextsession", type = String.class)})

@NamedStoredProcedureQuery(name = "User.obtenResultadosCotizacion", procedureName = "EM_K_GEN_WS.P_REGRESA_COTIZACION", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_num_control", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "p_cur_coberturas", type = void.class),
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "p_cur_primas", type = void.class),
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "p_cur_recibos", type = void.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "p_num_pagos", type = Integer.class) })

@NamedStoredProcedureQuery(name = "User.tramitesPendientes", procedureName = "GW_K_INFORMACION_GRAL.p_obt_tramites_pendientes", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_agente_id", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_secc", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_gerente", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_folio", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_end_err", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_estado", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_fecini", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_fecfin", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "p_registro", type = void.class) })

@NamedStoredProcedureQuery(name = "User.tramitesPendientes2", procedureName = "GW_K_INFORMACION_GRAL.p_obt_tramites_pendientes", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_agente_id", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_secc", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_gerente", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_folio", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_poliza", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_fecha", type = Date.class),
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "p_registro", type = void.class) })

@NamedStoredProcedureQuery(name = "User.getBitacora", procedureName = "GW_K_INFORMACION_GRAL.p_obt_bitacora", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_folio", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "p_registro", type = void.class),
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "p_registro2", type = void.class) })

@NamedStoredProcedureQuery(name = "User.getAnexos", procedureName = "PKG_ANEXOS.f_getAnexos", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "l_idsistema", type =  Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "l_retorno", type = void.class) })

public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger codCia;

    private Long codRamo;

    private String numPolizaGrupo;

    private Long numContrato;

    private Long codAgt;

    private Long codSector;

    @Column(name = "COD_USR_APP")
    private String userApp;

    @Column(name = "COD_PASSWORD")
    private String password;

    @Column(name = "NOM_AGT")
    private String nameAgt;

    private LocalDateTime fecAlta;

    private LocalDateTime fecModificacion;

    private LocalDateTime fecVigencia;

    private String correo;

    private String rfc;

    private String activo;

    private LocalDateTime fecActu;

    private String codUsr;

    public User()
    {

    }


    public User(BigInteger codCia, Long codRamo, String numPolizaGrupo, Long numContrato, Long codAgt,
                Long codSector, String user, String password, String nameAgt, LocalDateTime fecAlta, LocalDateTime fecModificacion,
                LocalDateTime fecVigencia, String correo, String rfc, String activo, LocalDateTime fecActu, String codUsr) {
        super();
        this.codCia = codCia;
        this.codRamo = codRamo;
        this.numPolizaGrupo = numPolizaGrupo;
        this.numContrato = numContrato;
        this.codAgt = codAgt;
        this.codSector = codSector;
        this.userApp = user;
        this.password = password;
        this.nameAgt = nameAgt;
        this.fecAlta = fecAlta;
        this.fecModificacion = fecModificacion;
        this.fecVigencia = fecVigencia;
        this.correo = correo;
        this.rfc = rfc;
        this.activo = activo;
        this.fecActu = fecActu;
        this.codUsr = codUsr;
    }


    public BigInteger getCodCia() {
        return codCia;
    }


    public void setCodCia(BigInteger codCia) {
        this.codCia = codCia;
    }


    public Long getCodRamo() {
        return codRamo;
    }


    public void setCodRamo(Long codRamo) {
        this.codRamo = codRamo;
    }


    public String getNumPolizaGrupo() {
        return numPolizaGrupo;
    }


    public void setNumPolizaGrupo(String numPolizaGrupo) {
        this.numPolizaGrupo = numPolizaGrupo;
    }


    public Long getNumContrato() {
        return numContrato;
    }


    public void setNumContrato(Long numContrato) {
        this.numContrato = numContrato;
    }


    public Long getCodAgt() {
        return codAgt;
    }


    public void setCodAgt(Long codAgt) {
        this.codAgt = codAgt;
    }


    public Long getCodSector() {
        return codSector;
    }


    public void setCodSector(Long codSector) {
        this.codSector = codSector;
    }


    public String getUser() {
        return userApp;
    }


    public void setUser(String user) {
        this.userApp = user;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getNameAgt() {
        return nameAgt;
    }


    public void setNameAgt(String nameAgt) {
        this.nameAgt = nameAgt;
    }


    public LocalDateTime getFecAlta() {
        return fecAlta;
    }


    public void setFecAlta(LocalDateTime fecAlta) {
        this.fecAlta = fecAlta;
    }


    public LocalDateTime getFecModificacion() {
        return fecModificacion;
    }


    public void setFecModificacion(LocalDateTime fecModificacion) {
        this.fecModificacion = fecModificacion;
    }


    public LocalDateTime getFecVigencia() {
        return fecVigencia;
    }


    public void setFecVigencia(LocalDateTime fecVigencia) {
        this.fecVigencia = fecVigencia;
    }


    public String getCorreo() {
        return correo;
    }


    public void setCorreo(String correo) {
        this.correo = correo;
    }


    public String getRfc() {
        return rfc;
    }


    public void setRfc(String rfc) {
        this.rfc = rfc;
    }


    public String getActivo() {
        return activo;
    }


    public void setActivo(String activo) {
        this.activo = activo;
    }


    public LocalDateTime getFecActu() {
        return fecActu;
    }


    public void setFecActu(LocalDateTime fecActu) {
        this.fecActu = fecActu;
    }


    public String getCodUsr() {
        return codUsr;
    }


    public void setCodUsr(String codUsr) {
        this.codUsr = codUsr;
    }


    @Override
    public String toString() {
        return "User [codCia=" + codCia + ", cod_ramo=" + codRamo + ", numPolizaGrupo=" + numPolizaGrupo
                + ", numContrato=" + numContrato + ", codAgt=" + codAgt + ", codSector=" + codSector + ", user=" + userApp
                + ", password=" + password + ", nameAgt=" + nameAgt + ", fecAlta=" + fecAlta + ", fecModificacion="
                + fecModificacion + ", fecVigencia=" + fecVigencia + ", correo=" + correo + ", rfc=" + rfc + ", activo="
                + activo + ", fecActu=" + fecActu + ", codUsr=" + codUsr + "]";
    }
}
