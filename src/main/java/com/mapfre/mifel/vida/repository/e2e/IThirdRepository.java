package com.mapfre.mifel.vida.repository.e2e;

import com.mapfre.mifel.vida.entity.e2e.Tercero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
@org.springframework.transaction.annotation.Transactional
public interface IThirdRepository extends JpaRepository<Tercero, BigInteger> {

    @Procedure(name = "Tercero.obtenResultadosCotizacion")
    HashMap<String, Object> obtenResultadosCotizacion(@Param("p_num_control") String strNumeroCotizacion);

    @Transactional
    @Procedure(procedureName = "Tercero.altaTercero")
    Map<String, Object> altaTercero(@Param("p_cod_origen") Integer originCode,
                                    @Param("p_cod_usr") String userCode,
                                    @Param("p_cod_ramo") String branchCode,
                                    @Param("p_tip_benef") String benefType,
                                    @Param("p_cadena_xml") String cadena);

    @Transactional
    @Procedure(procedureName = "Tercero.setCotizacionEmision")
    HashMap<String, Object> setCotizacionEmision(@Param("pcadena") String xml,
                                                 @Param("pidcontextsession") String pidcontextsession);

    @Transactional
    @Procedure(procedureName = "Tercero.altaBeneficiario")
    Map<String, String> altaBeneficiario(@Param("p_tip_docum") String tip_docum, // #1
                                         @Param("p_cod_docum") String cod_docum, // #2
                                         @Param("p_mca_fisico") String mca_fisico, // #3
                                         @Param("p_nom_tercero") String nom_tercero, // #4
                                         @Param("p_ape1_tercero") String ape1_tercero, // #5
                                         @Param("p_ape2_tercero") String ape2_tercero, // #6
                                         @Param("p_cod_parentesco") Integer cod_parentesco, // #7
                                         @Param("p_fec_nacimiento") Date fec_nacimiento, // #8
                                         @Param("p_nom_domicilio1") String nom_domicilio1, // #9
                                         @Param("p_nom_domicilio2") String nom_domicilio2, // #10
                                         @Param("p_num_exterior") String num_exterior, // #11
                                         @Param("p_num_interior") String num_interior, // #12
                                         @Param("p_nom_colonia") String nom_colonia, // #13
                                         @Param("p_cod_pais") String cod_pais, // #14
                                         @Param("p_cod_estado") String cod_estado, // #15
                                         @Param("p_cod_prov") String cod_prov, // #16
                                         @Param("p_cod_postal") String cod_postal, // #17
                                         @Param("p_tlf_numero") String tlf_numero, // #18
                                         @Param("p_email") String email); // #19

    @Transactional
    @Procedure(procedureName = "Tercero.cargaDatos")
    Map<String, String> cargaDatos(@Param("p_cod_cia") Integer cod_cia, // #1
                                   @Param("p_tip_docum") String tip_docum, // #2
                                   @Param("p_cod_docum") String cod_docum, // #3
                                   @Param("p_mca_fisico") String mca_fisico, // #4
                                   @Param("p_reg_fiscal") Integer reg_fiscal, // #5
                                   @Param("p_nombre_razon") String nombre_razon, // #6
                                   @Param("p_ape_paterno") String ape_paterno, // #7
                                   @Param("p_ape_materno") String apr_materno, // #8
                                   @Param("p_nom_domicilio1") String nom_domicilio1, // #9
                                   @Param("p_nom_domicilio2") String nom_domicilio2, // #10
                                   @Param("p_num_ext") String num_ext, // #11
                                   @Param("p_num_int") String num_int, // #12
                                   @Param("p_cod_postal") String cod_postal, // #13
                                   @Param("p_cod_prov") Integer cod_prov, // #14
                                   @Param("p_cod_estado") Integer cod_estado, // #15
                                   @Param("p_cod_pais") String cod_pais, // #16
                                   @Param("p_cod_usr") String cod_usr, // #17
                                   @Param("p_fec_actu") Date fec_actu, // #18
                                   @Param("p_proceso_val") Integer proceso_val, //#19
                                   @Param("p_mensaje_val") String mensaje_val); // #20
}
