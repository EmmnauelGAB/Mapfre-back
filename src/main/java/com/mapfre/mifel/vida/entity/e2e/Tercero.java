package com.mapfre.mifel.vida.entity.e2e;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@NamedStoredProcedureQuery(name = "Tercero.altaTercero", procedureName = "dc_k_clm_gestion.p_alta_tercero", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_origen", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_usr", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_ramo", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_tip_benef", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cadena_xml", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name= "p_tip_docum", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "p_cod_docum",type = String.class) })

@NamedStoredProcedureQuery(name = "Tercero.setCotizacionEmision", procedureName = "EM_K_GEN_WS.P_LANZA_PROCESO2_XML", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "pcadena", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "pnum_poliza", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "ptxt_error", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "pidcontextsession", type = String.class)})

@NamedStoredProcedureQuery(name = "Tercero.altaBeneficiario", procedureName = "dc_k_clm_gestion.p_alta_beneficiario", parameters = {
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

@NamedStoredProcedureQuery(name = "Tercero.obtenResultadosCotizacion", procedureName = "EM_K_GEN_WS.P_REGRESA_COTIZACION", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_num_control", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "p_cur_coberturas", type = void.class),
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "p_cur_primas", type = void.class),
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "p_cur_recibos", type = void.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "p_num_pagos", type = Integer.class) })

@NamedStoredProcedureQuery(name = "Tercero.cargaDatos", procedureName = "TRON2000.GC_K_CARGA_MAS_DTS_FSC_2_MMX.p_carga_datos", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_cia", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_tip_docum", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_docum", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_mca_fisico", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_reg_fiscal", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_nombre_razon", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_ape_paterno", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_ape_materno", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_nom_domicilio1", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_nom_domicilio2", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_num_ext", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_num_int", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_postal", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_prov", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_estado", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_pais", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cod_usr", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_fec_actu", type = Date.class),
        @StoredProcedureParameter(mode = ParameterMode.INOUT, name = "p_proceso_val", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.INOUT, name = "p_mensaje_val", type = String.class),
})

@Entity
public class Tercero implements Serializable {

    @Id
    //BigInteger id;
    private String tip_docum;
    private String cod_docum;

    @Column(name="pcadena", columnDefinition = "CLOB NOT NULL")
    @Lob
    private String pcadena;

    public String getTip_docum() {
        return tip_docum;
    }

    public void setTip_docum(String tip_docum) {
        this.tip_docum = tip_docum;
    }

    public String getCod_docum() {
        return cod_docum;
    }

    public void setCod_docum(String cod_docum) {
        this.cod_docum = cod_docum;
    }
}
