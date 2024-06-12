package com.mapfre.mifel.vida.repository.esa;

import com.mapfre.mifel.vida.entity.esa.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface IMifelVidaULRepository extends JpaRepository<User, BigInteger> {

    @Procedure(procedureName = "User.obtenResultadosCotizacion")
    HashMap<String, Object> obtenResultadosCotizacion(@Param("p_num_control") String strNumeroCotizacion);

    @Procedure(procedureName = "User.altaTercero")
    HashMap<String, String> altaTercero(@Param("p_cod_origen") Integer originCode,
                                        @Param("p_cod_usr") String userCode,
                                        @Param("p_cod_ramo") String branchCode,
                                        @Param("p_tip_benef") String benefType,
                                        @Param("p_cadena_xml") String cadena);

    @Procedure(procedureName = "User.altaBeneficiario")
    Map<String, String> altaBeneficiario(@Param("p_tip_docum") String tip_docum,            //#1
                                         @Param("p_cod_docum") String cod_docum,            //#2
                                         @Param("p_mca_fisico") String mca_fisico,          //#3
                                         @Param("p_nom_tercero") String nom_tercero,        //#4
                                         @Param("p_ape1_tercero") String ape1_tercero,      //#5
                                         @Param("p_ape2_tercero") String ape2_tercero,      //#6
                                         @Param("p_cod_parentesco") Integer cod_parentesco, //#7
                                         @Param("p_fec_nacimiento") Date fec_nacimiento,    //#8
                                         @Param("p_nom_domicilio1") String nom_domicilio1,  //#9
                                         @Param("p_nom_domicilio2") String nom_domicilio2,  //#10
                                         @Param("p_num_exterior") String num_exterior,      //#11
                                         @Param("p_num_interior") String num_interior,      //#12
                                         @Param("p_nom_colonia") String nom_colonia,        //#13
                                         @Param("p_cod_pais") String cod_pais,              //#14
                                         @Param("p_cod_estado") String cod_estado,          //#15
                                         @Param("p_cod_prov") String cod_prov,              //#16
                                         @Param("p_cod_postal") String cod_postal,          //#17
                                         @Param("p_tlf_numero") String tlf_numero,          //#18
                                         @Param("p_email") String email);                   //#19

    @Procedure(procedureName = "User.setCotizacionEmision")
    Map<String, String> setCotizacionEmision(@Param("pcadena") String xml,
                                             @Param("pidcontextsession") String pidcontextsession);

    @Procedure(procedureName = "User.tramitesPendientes")
    Map<String, Object> tramitesPendientes(@Param("p_agente_id") Integer agente,
                                           @Param("p_cod_secc") Integer sector,
                                           @Param("p_gerente") Integer gerente,
                                           @Param("p_folio") String folio,
                                           @Param("p_end_err") Integer captura,
                                           @Param("p_estado") Integer estado,
                                           @Param("p_fecini") String desde,
                                           @Param("p_fecfin") String hasta);

    @Procedure(procedureName = "User.tramitesPendientes2")
    Map<String, Object> tramitesPendientes2(@Param("p_agente_id") int agente,
                                            @Param("p_cod_secc") int sector,
                                            @Param("p_gerente") int gerente,
                                            @Param("p_folio") String folio,
                                            @Param("p_poliza") String poliza,
                                            @Param("p_fecha") Date date);

    @Procedure(procedureName = "User.getBitacora")
    Map<String, Object> getBitacora(@Param("p_folio") Integer idsistema );

    @Procedure(procedureName = "User.getAnexos")
    List<Object[]> getAnexos(@Param("l_idsistema") Integer idsistema);


}
