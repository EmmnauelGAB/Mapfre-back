package com.mapfre.mifel.vida.helper;

import com.mapfre.mifel.vida.model.clm.CLMFisica;
import com.mapfre.mifel.vida.model.clm.CLMMoral;
import com.mapfre.mifel.vida.model.clm.CLMPersona;
import com.mapfre.mifel.vida.model.clm.TipoPersona;
import com.mapfre.mifel.vida.repository.e2e.IThirdRepository;
import com.mapfre.mifel.vida.repository.esa.IMifelVidaULRepository;
import com.mapfre.mifel.vida.utils.DateFormatUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.mapfre.mifel.vida.utils.VidaULContants.*;

@Service
public class CLM {
    private static final Logger logger = LogManager.getLogger(CLM.class);

    @Autowired
    IMifelVidaULRepository iMifelVidaULRepository;
    @Autowired
    IThirdRepository iThirdRepository;

    /**
     * ============================================
     * 'Funcion para dar de alta un tercero como CLM
     * ============================================
     */
    public String[] altaTercero(Map<String, String> arrDatosTercero, String tip_benef, String strRamo) {
        StringBuffer xml = new StringBuffer();

        String cod_usr = AltaTercero.USER_CODE_EMISION;
        Integer originCode = AltaTercero.ORIGIN_CODE;

        xml.append("<XML><TERCERO>");
        for (Map.Entry<String, String> entry : arrDatosTercero.entrySet()) {
            //System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
            xml.append(setDatosMap(entry.getKey(), entry.getValue()));
        }
        xml.append("</TERCERO></XML>");
        String[] salida = new String[2];
        Map<String, Object> valores= new LinkedHashMap<>();
        try {
            valores= iThirdRepository.altaTercero(
                    originCode,
                    cod_usr,
                    strRamo,
                    tip_benef,
                    xml.toString());
            logger.info("Valores Alta Tercer: " + valores);
            if(valores != null){
                salida[0] = (String)valores.get("p_tip_docum");
                salida[1] = (String)valores.get("p_cod_docum");
            }else{
                logger.info("No se puede dar de alta tercero revisar Datos");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return salida;
    }


    private StringBuffer setDatosMap(String id, String value) {
        StringBuffer objSB = new StringBuffer();
        objSB.append("<");
        objSB.append(id);
        objSB.append(">");
        objSB.append(value);
        objSB.append("</");
        objSB.append(id);
        objSB.append(">");
        return objSB;
    }

    public String[] altaBeneficiario(String mca_fisico, String nom_tercero, String ape1_tercero, String ape2_tercero, String cod_parentesco, String cod_ocupacion, String fec_nacimiento) {

        DateFormatUtil dateFormatUtil = new DateFormatUtil();
        String[] results= null;
        try {
            CLMPersona objPersona;//objPersona= new CLMPersona();
            /** Datos Persona Fisica */
            if (mca_fisico.equals("S")) {
                CLMFisica objPFisica = new CLMFisica();
                objPFisica.setNOM_TERCERO(nom_tercero);
                objPFisica.setAPE1_TERCERO(ape1_tercero);
                objPFisica.setAPE2_TERCERO(ape2_tercero);
                objPFisica.setCOD_PARENTESCO(cod_parentesco);
                objPFisica.setCOD_PROFESION(cod_ocupacion);
                objPFisica.setFEC_NACIMIENTO(dateFormatUtil.convertDate(fec_nacimiento, Dates.DATE_FORMAT_1));
                objPersona = objPFisica;
                objPersona.setsTIP_PERSONA(TipoPersona.Fisica);

                CLMFisica objPFisica2 = (CLMFisica) objPersona;
                //logger.info("persona fiscia parentesco:"+objPFisica2.getCOD_PARENTESCO());
            }else{
                CLMMoral objMoral= new CLMMoral();
                objMoral.setRAZON_SOCIAL(nom_tercero);
                objPersona = objMoral;
                objPersona.setsTIP_PERSONA(TipoPersona.Moral);
            }

            String[] res = this.altaBeneficiario(objPersona);
            results = res;

        } catch (Exception e) {
            logger.info("CLM.AltaBeneficiario() : " + e.getMessage());
        }
        return results;
    }

    public String[] altaBeneficiario(CLMPersona objPersona){

        Map<String, String> drBeneficiario = new LinkedHashMap<>();
        String[] results= null;
        DateFormatUtil dateFormatUtil = new DateFormatUtil();
        try {
            CLMFisica clmFisica= new CLMFisica();
            CLMMoral clmMoral =  new CLMMoral();
            String typePerson=objPersona.getsTIP_PERSONA().getValue();
            if (typePerson.equals(TipoPersona.Fisica.getValue())) {
                clmFisica = (CLMFisica) objPersona;
            }else {
                clmMoral = (CLMMoral) objPersona;
            }
            String tip_docum = "" ;
            String cod_docum = "";
            String mca_fisico = String.valueOf(objPersona.getsTIP_PERSONA().getValue());
            String nom_tercero =  objPersona.getsTIP_PERSONA().equals(TipoPersona.Fisica) ? clmFisica.getNOM_TERCERO() : clmMoral.getRAZON_SOCIAL();
            String ape1_tercero = clmFisica.getAPE1_TERCERO();
            String ape2_tercero = clmFisica.getAPE2_TERCERO();
            Integer cod_parentesco =  typePerson.equals(TipoPersona.Fisica.getValue()) ? convertInt(clmFisica.getCOD_PARENTESCO()) : convertInt(clmMoral.getCOD_PARENTESCO());
            Date dateTime = objPersona.getsTIP_PERSONA().equals(TipoPersona.Fisica) ? clmFisica.getFEC_NACIMIENTO() : clmMoral.getFEC_CONSTITUCION();
            Date fec_nacimiento =  dateTime.after(dateFormatUtil.truncateDate()) ? dateTime : null;
            String nom_domicilio1 = objPersona.getcDireccion() != null ? objPersona.getcDireccion().getsDOMICILIO1() : "";
            String nom_domicilio2 = objPersona.getcDireccion() != null ? objPersona.getcDireccion().getsDOMICILIO2() : "";
            String num_exterior = objPersona.getcDireccion() != null ? objPersona.getcDireccion().getsNUM_EXTERIOR() : "";
            String num_interior = objPersona.getcDireccion() != null ? objPersona.getcDireccion().getsNUM_INTERIOR() : "";
            String nom_colonia = objPersona.getcDireccion() != null ? objPersona.getcDireccion().getsCOLONIA() : "";
            String cod_pais = objPersona.getcDireccion() != null ? objPersona.getcDireccion().getsCOD_PAIS() : "";
            String cod_estado = objPersona.getcDireccion() != null ? objPersona.getcDireccion().getsCOD_ESTADO() : "";
            String cod_prov = objPersona.getcDireccion() != null ? objPersona.getcDireccion().getsCOD_POBLACION() : "";
            String cod_postal = objPersona.getcDireccion() != null ? objPersona.getcDireccion().getsCODIGO_POSTAL(): "";
            String tlf_numero = objPersona.getcDireccion() != null ? objPersona.getsTELEFONO() : "";
            String email = objPersona.getcDireccion() != null ? objPersona.getsEMAIL() : "";

            /*logger.info("tip_docum: "+tip_docum+", cod_docum:"+ cod_docum +", mca_fisico:"+ mca_fisico+", nom_tercero:"+ nom_tercero
                    +", ape1_tercero:"+ ape1_tercero+", ape2_tercero:"+ ape2_tercero +", cod_parentesco:"+ cod_parentesco+", fec_nacimiento:"+ fec_nacimiento
                    +", nom_domicilio1:"+ nom_domicilio1+", nom_domicilio2:"+ nom_domicilio2 +", num_exterior:"+ num_exterior+", num_interior:"+ num_interior
                    +",  nom_colonia:"+  nom_colonia+", cod_pais:"+ cod_pais +",  cod_estado:"+ cod_estado+", cod_prov:"+ cod_prov
                    +",   cod_postal:"+  cod_postal+", tlf_numero:"+ tlf_numero +",  email:"+ email);*/
            drBeneficiario = iThirdRepository.altaBeneficiario(tip_docum, cod_docum, mca_fisico, nom_tercero, ape1_tercero, ape2_tercero,
                    cod_parentesco, fec_nacimiento, nom_domicilio1, nom_domicilio2, num_exterior, num_interior, nom_colonia,cod_pais, cod_estado,
                    cod_prov, cod_postal, tlf_numero, email);
            //logger.info(drBeneficiario.get("p_tip_docum")+" | "+drBeneficiario.get("p_cod_docum"));
            String[] b = {drBeneficiario.get("p_tip_docum"), drBeneficiario.get("p_cod_docum")};
            results = b;

        }catch(Exception e){
            logger.info("CLM.AltaBeneficiario() : " + e.getMessage());
        }
        return results;
    }

    private Integer convertInt(String value){
        Integer res=0;
        try {
            res = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.info("No se puede convertir a entero: "+e.getMessage());
        }
        return res !=0 ? res : res;
    }
}
