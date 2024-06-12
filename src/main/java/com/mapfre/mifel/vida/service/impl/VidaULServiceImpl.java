package com.mapfre.mifel.vida.service.impl;

import com.ctc.wstx.shaded.msv_core.datatype.xsd.TotalDigitsFacet;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapfre.mifel.vida.exception.EmptyValueException;
import com.mapfre.mifel.vida.helper.CLM;
import com.mapfre.mifel.vida.helper.EmisionVidaFM;
import com.mapfre.mifel.vida.model.Coverages;
import com.mapfre.mifel.vida.model.Fund;
import com.mapfre.mifel.vida.model.OracleCursorWrapper;
import com.mapfre.mifel.vida.model.enumeration.DocumentType;
import com.mapfre.mifel.vida.model.enumeration.Periodicity;
import com.mapfre.mifel.vida.model.print.ResultXmlHpExstream;
import com.mapfre.mifel.vida.model.request.FundDistribution;
import com.mapfre.mifel.vida.model.request.IssueRequest;
import com.mapfre.mifel.vida.model.request.SimulationRequest;
import com.mapfre.mifel.vida.model.response.VidaULCotizacionResponse;
import com.mapfre.mifel.vida.model.response.VidaULEmisionResponse;
import com.mapfre.mifel.vida.model.ul.*;
import com.mapfre.mifel.vida.repository.e2e.IThirdRepository;
import com.mapfre.mifel.vida.repository.esa.IMifelVidaULRepository;
import com.mapfre.mifel.vida.service.VidaULService;
import com.mapfre.mifel.vida.utils.FundConstants;
import com.mapfre.mifel.vida.utils.HostUtil;
import com.mapfre.mifel.vida.utils.MifelVidaMensajesEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.proxy.annotation.GetProxy;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;

import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static com.mapfre.mifel.vida.utils.VidaULContants.*;
import static com.mapfre.mifel.vida.utils.MifelVidaMensajesEnum.*;

@Service
public class VidaULServiceImpl implements VidaULService {


    private final Logger LOGGER = LogManager.getLogger(VidaULServiceImpl.class);

    @Autowired
    CLM clm;

    @Autowired
    IMifelVidaULRepository iMifelVidaULRepository;

    @Autowired
    private IThirdRepository iThirdRepository;

    @Autowired
    @Qualifier("bankDataSource")
    DataSource dataSource;

    /**
     * Bandera para activar Cliente MAPFRE
     **/
    private String banderaCLM;

    public void setBanderaCLM(String banderaCLM) {
        this.banderaCLM = banderaCLM;
    }

    /*@Override
    public VidaULEmisionResponse generaEmisionMock(IssuanceRequest request) {

        VidaULEmisionResponse response = new VidaULEmisionResponse();
        int min = 10;
        int max = 1000000;
        response.setPnum_poliza(String.valueOf((int)Math.floor(Math.random()*(max-min+1)+min)));
        response.setPtxt_error("");
        return response;
    }*/

    /**
     * ramo
     */
    private String cod_ramo;

    public String getCod_ramo() {
        return cod_ramo;
    }

    public void setCod_ramo(String cod_ramo) {
        this.cod_ramo = cod_ramo;
    }

    @Override
    public VidaULEmisionResponse getEmision(IssueRequest request) {
        /** Se llama a la clase CLM */
        //CLM clm = new CLM();
        String[] drContratante = new String[2];
        /** Se obtiene el ramo */
        String strRamo = getCod_ramo();
        VidaULEmisionResponse response = new VidaULEmisionResponse();
        /***************************************************************************************************************
         *  Se forma el arreglo de <DATOS_POLIZA>
         */
        PolicyData datosPolizaDTO = request.getPolicyData();
        Map<String, String> mapPoliza = new LinkedHashMap<>();
        if (BANDERA_CLM.equals("True")) {
            //datosPolizaDTO.setCod_ramo("");
            mapPoliza = datosPolizaDTO.dameLista(datosPolizaDTO);
        } else {
            mapPoliza = datosPolizaDTO.dameLista(datosPolizaDTO);
        }
        /**
         * Datos Fijos
         */
        mapPoliza.put("FEC_EFEC_SPTO", request.getPolicyData().getPolicyEffectiveDate());
        mapPoliza.put("FEC_VCTO_SPTO", request.getPolicyData().getPolicyEndDate());

        if (request.getPolicyData().getManagerType().equals("AG")) {
            mapPoliza.put("COD_GESTOR", request.getPolicyData().getAgentCode());
        } else {
            mapPoliza.put("COD_GESTOR", "");
        }
        mapPoliza.put("MCA_IMPRESION", "N");

        /***************************************************************************************************************
         *  Se forma el arreglo de <DATOS_VARIABLES>
         */
        List<Map<String, String>> listaDatosVariables = new ArrayList<Map<String, String>>();
        int strNumSecu = 0;
        String strValor = "";
        String sCOD_MODALIDAD = "";
        /** Se recorre el arreglo de nodos de DATOS_VARIABLES*/
        for (VariableData datosV : request.getVariableData()) {
            strNumSecu = 1;
            /**Se recorre el arreglo de nodos de CAMPO*/
            for (FieldRisk campo : datosV.getFields()) {
                Map<String, String> mapDVariables = new LinkedHashMap<>();
                if (campo.getFieldValue().length() > 10) {
                    strValor = campo.getFieldValue().trim().substring(0, 10);
                } else {
                    strValor = campo.getFieldValue();
                }
                campo.convertDate(campo);

                mapDVariables.put("COD_CAMPO", campo.getFieldCode()); //+"|"
                mapDVariables.put("VAL_CAMPO", campo.getFieldValue()); //+"|"
                mapDVariables.put("VAL_COR_CAMPO", strValor); //+"|"
                mapDVariables.put("TIP_NIVEL", datosV.getLevelType()); //+"|"
                mapDVariables.put("NUM_SECU", String.valueOf(strNumSecu)); //+"|"
                mapDVariables.put("NUM_RIESGO", datosV.getRiskNumber()); //+"|"
                mapDVariables.put("COD_RAMO", strRamo);
                
                if(campo.getFieldCode().equals("ANIOS_DURACION_POLIZA")) {
                	mapPoliza.put("ANIOS_MAX_DURACION", (String)campo.getFieldValue());
                }
                else if(campo.getFieldCode().equals("COD_MODALIDAD")) {
                	sCOD_MODALIDAD = campo.getFieldValue();
                }
                
                listaDatosVariables.add(mapDVariables);
                strNumSecu++;
            }
        }

        /***************************************************************************************************************
         *  Se forma el arreglo de <DATOS_VARIABLES COVERAGE>
         */
        List<Map<String, String>> listaDatosVariablesCoverage = new ArrayList<Map<String, String>>();
        strNumSecu = 0;
        strValor = "";
        String sCodLista = "";
        String sCodListaValor = "";
//        sCOD_MODALIDAD = "";
        /** Se recorre el arreglo de nodos de DATOS_VARIABLES COVERAGE*/
        for (VariableDataCoverage datosV : request.getVariableDataCoverage()) {
            strNumSecu = 1;
            /**Se recorre el arreglo de nodos de CAMPO*/
            for (FieldRisk campo : datosV.getFields()) {
                Map<String, String> mapDVariables = new LinkedHashMap<>();
                campo.convertDate(campo);

                mapDVariables.put("COD_CAMPO", campo.getFieldCode()); //+"|"
                mapDVariables.put("VAL_CAMPO", campo.getFieldValue()); //+"|"
                mapDVariables.put("TXT_CAMPO", ""); //+"|"
                sCodLista = campo.getFieldCode();
                if(sCodLista.equals("CODIGO_FUNDO") || sCodLista.equals("PERCENTAGEM_FUNDO"))
                	sCodListaValor="600";
                else
                	sCodListaValor="601";
                mapDVariables.put("COD_LISTA", sCodListaValor); //+"|"
                mapDVariables.put("NUM_RIESGO", datosV.getRiskNumber()); //+"|"
                mapDVariables.put("NUM_PERIODO", "1"); //+"|"
                mapDVariables.put("NUM_OCURRENCIA", datosV.getLevelType());
                mapDVariables.put("NUM_SECU", String.valueOf(strNumSecu)); //+"|"
                
                listaDatosVariablesCoverage.add(mapDVariables);
                strNumSecu++;
            }
        }

        /** ************************************************************************************************************
        
        /** ************************************************************************************************************
         * Se forma el arreglo de <COBERTURAS>
         */
        List<Map<String, String>> listCoberturas = new ArrayList<Map<String, String>>();
        strNumSecu = 1;
        for (Coverage coberturas : request.getCoverage()) {
            Map<String, String> mapCoberturas = new LinkedHashMap<>();
            mapCoberturas.put("COD_COB", coberturas.getCoverageCode()); //+"|"
            mapCoberturas.put("SUMA_ASEG", coberturas.getSumInsuredAmn()); //+"|"
            mapCoberturas.put("SUMA_ASEG_SPTO", coberturas.getSumInsuredAmn()); //+"|"
            mapCoberturas.put("NUM_SECU", String.valueOf(strNumSecu)); //+"|"
            mapCoberturas.put("MCA_BAJA_COB", "N");
            mapCoberturas.put("COD_RAMO", "112");
            listCoberturas.add(mapCoberturas);
            strNumSecu++;
        }

        /** ************************************************************************************************************
         * Se forma el arreglo de <DATOS_CONTRATANTE>
         */
        Map<String, String> mapContratante = request.getContractorData().dameLista(request.getContractorData());
        
        String name = request.getContractorData().getContractingFistName().orElse("");
        String middleName =  request.getContractorData().getContractingMiddleName().orElse("");
        String lastName =  request.getContractorData().getContractingLastName().orElse("");
        String rfc = request.getContractorData().getDocumCode().orElse("XAXX010101000");
        String address1 = request.getContractorData().getAddressName1().orElse("");
        String address2 = request.getContractorData().getAddressName2().orElse("");
        String address3 = request.getContractorData().getAddressName3().orElse("");
        String postalCode = request.getContractorData().getPostalCode().orElse("");
        String stateCode = request.getContractorData().getStateCode().orElse("");
        String provinceCode = request.getContractorData().getProvinceCode().orElse("");
        String countryCode = request.getContractorData().getCountryCode().orElse("");
        String telephone = request.getContractorData().getTelephoneNumber().orElse("");
        
        /** 'Datos Fijos*/
        mapContratante.put("TIP_DOCUM", "RFC");
        mapContratante.put("RFC", rfc);
        mapContratante.put("NOM_RIESGO", name + " " + middleName + " " + lastName);
        mapContratante.put("NOM_DOMICIO1_COM", address1);
        mapContratante.put("NOM_DOMICIO2_COM", address2);
        mapContratante.put("NOM_DOMICIO3_COM", address3);
        mapContratante.put("COD_POSTAL_COM", postalCode);
        mapContratante.put("COD_ESTADO_COM", stateCode);
        mapContratante.put("COD_PROV_COM", provinceCode);
        mapContratante.put("COD_PAIS_COM", countryCode);
        mapContratante.put("TLF_NUMERO_COM", telephone);
        mapContratante.put("TXT_ETIQUETA2", address3);
        mapContratante.put("NOM_LOCALIDAD", address3);
        mapContratante.put("NOM_LOCALIDAD_COM", address3);
        mapContratante.put("TXT_ETIQUETA4", address3 + address2);
        mapContratante.put("COD_LOCALIDAD", provinceCode);
        mapContratante.put("NUM_SECU", "1");
        mapContratante.put("FEC_EFEC_RIESGO", request.getPolicyData().getPolicyEffectiveDate());
        mapContratante.put("FEC_VCTO_RIESGO", request.getPolicyData().getPolicyEndDate());

        if (BANDERA_CLM.equals("True")) {
            /**Se obtiene la clave de CLM para el contratante*/
            String tip_benef = request.getContractorData().getBeneficiaryType();
            drContratante = clm.altaTercero(mapContratante, tip_benef, strRamo);
            mapContratante.clear();
            mapContratante.put("TIP_BENEF", tip_benef);
            mapContratante.put("TIP_DOCUM", drContratante[0]);
            mapContratante.put("COD_DOCUM", drContratante[1]);
            mapContratante.put("RFC", rfc);
            mapContratante.put("NUM_SECU", "1");
            mapContratante.put("NOM_RIESGO", request.getContractorData().getContractingFistName() + " " + request.getContractorData().getContractingMiddleName() + " " + request.getContractorData().getContractingLastName());
            mapContratante.put("FEC_EFEC_RIESGO", request.getPolicyData().getPolicyEffectiveDate());
            mapContratante.put("FEC_VCTO_RIESGO", request.getPolicyData().getPolicyEndDate());
            /**'Se modifica el valor del COD_DOCUM en los datos de la póliza*/
            mapPoliza.put("TIP_DOCUM", drContratante[0]);
            mapPoliza.replace("COD_DOCUM", drContratante[1]);
        }
        /** ************************************************************************************************************
         * Se forma el arreglo de <DATOS_ASEGURADO>
         */
        Map<String, String> mapAsegurado = request.getInsuredData().getList(request.getInsuredData());

        /** 'Datos Fijos*/
        mapAsegurado.put("TIP_DOCUM", "RFC");
        mapAsegurado.put("RFC", request.getInsuredData().getDocumCode());
        mapAsegurado.put("COD_IDENTIFICADOR", USER_CODE + "_" + getClientHost1().replace(".", "_"));
        mapAsegurado.put("PAIS", "MEX");
        mapAsegurado.put("NOM_RIESGO", request.getInsuredData().getContractingFistName() + " " + request.getInsuredData().getContractingMiddleName() + " " + request.getInsuredData().getContractingLastName());
        mapAsegurado.put("NOM_DOMICILIO1_COM", request.getInsuredData().getAddressName1());
        mapAsegurado.put("NOM_DOMICILIO2_COM", request.getInsuredData().getAddressName2());
        mapAsegurado.put("NOM_DOMICILIO3_COM", request.getInsuredData().getAddressName3());
        mapAsegurado.put("COD_POSTAL_COM", request.getInsuredData().getPostalCode());
        mapAsegurado.put("COD_ESTADO_COM", request.getInsuredData().getStateCode());
        mapAsegurado.put("COD_PAIS_COM", request.getInsuredData().getCountryCode());
        mapAsegurado.put("COD_LOCALIDAD", request.getInsuredData().getProvinceCode());
        mapAsegurado.put("COD_PARENTESCO", "4");
        mapAsegurado.put("NUM_SECU", "1");
        mapAsegurado.put("FEC_EFEC_RIESGO", request.getPolicyData().getPolicyEffectiveDate());
        mapAsegurado.put("FEC_VCTO_RIESGO", request.getPolicyData().getPolicyEndDate());
        /**----- HRR*/
        mapAsegurado.put("COD_POSTAL_ETIQUETA", request.getInsuredData().getPostalCode()); //'HRR
        mapAsegurado.put("COD_ESTADO_ETIQUETA", request.getInsuredData().getStateCode()); //'HRR
        mapAsegurado.put("COD_LOCALIDAD_COM", request.getInsuredData().getProvinceCode());     //'HRR
        mapAsegurado.put("COD_LOCALIDAD_ETIQUETA", request.getInsuredData().getProvinceCode());     //'HRR
        mapAsegurado.put("COD_OFICINA", "");     //'HRR
        mapAsegurado.put("COD_PAIS_ETIQUETA", request.getInsuredData().getCountryCode());     //'HRR
        mapAsegurado.put("COD_USR", "AGENTETW");     //'HRR
        mapAsegurado.put("PCT_PARTICIPACION", "100");     //'HRR

        if (BANDERA_CLM.equals("True")) {
            /** Se obtiene la clave de CLM para el asegurado */
            String[] drAsegurado = clm.altaTercero(mapAsegurado, "1", strRamo);
            mapAsegurado.clear();
            mapAsegurado.put("TIP_BENEF", "1");
            mapAsegurado.put("TIP_DOCUM", drAsegurado[0]);
            mapAsegurado.put("COD_DOCUM", drAsegurado[1]);
            mapAsegurado.put("NOM_RIESGO", request.getInsuredData().getContractingFistName() + " " + request.getInsuredData().getContractingMiddleName() + " " + request.getInsuredData().getContractingLastName());
            mapAsegurado.put("FEC_EFEC_RIESGO", request.getPolicyData().getPolicyEffectiveDate());
            mapAsegurado.put("FEC_VCTO_RIESGO", request.getPolicyData().getPolicyEndDate());
            mapAsegurado.put("COD_MODALIDAD", sCOD_MODALIDAD);
        }
        /** Se forma el arreglo de <BENEFICIARIOS> ********************************************************************/
        List<Map<String, String>> listaBeneficiarios = new ArrayList<>();
        String strCodDocum = "DOCUMENT";
        String strTipDocum = "CNE";
        if (request.getBeneficiary() != null) {
            for (Beneficiaries beneficiario : request.getBeneficiary()) {
                Map<String, String> mapBeneficiarios = new LinkedHashMap<>();
                if (BANDERA_CLM.equals("True")) {
                    /** Se crea la clave BEN del Beneficiario */
                    String[] drBeneficiario = clm.altaBeneficiario(
                            beneficiario.getPersonType(), beneficiario.getContractingFistName(),
                            beneficiario.getContractingMiddleName(), beneficiario.getContractingLastName(),
                            beneficiario.getRelationshipCode(), beneficiario.getOccupationCode(),
                            beneficiario.getDateBirth());
                    if (drBeneficiario != null) {
                        strCodDocum = drBeneficiario[1];
                        strTipDocum = drBeneficiario[0];
                    }
                    mapBeneficiarios.put("TIP_DOCUM", strTipDocum); //+"|"
                    mapBeneficiarios.put("COD_DOCUM", strCodDocum);//+"|"
                    mapBeneficiarios.put("PCT_PARTICIPACION", beneficiario.getPercentageParticipation());//+"|"
                    mapBeneficiarios.put("TIP_BENEF", beneficiario.getBeneficiaryType());//+"|"
                    mapBeneficiarios.put("NUM_SECU", beneficiario.getSequenceNumber());//+"|"
                    listaBeneficiarios.add(mapBeneficiarios);
                } else {
                    strCodDocum = beneficiario.getContractingMiddleName().substring(0, 2) + beneficiario.getContractingLastName().substring(0, 1) + beneficiario.getContractingFistName().substring(0, 1);
                    mapBeneficiarios.put("TIP_DOCUM", "CLI"); //+"|"
                    mapBeneficiarios.put("COD_DOCUM", strCodDocum); //+"|"
                    mapBeneficiarios.put("MCA_FISICO", beneficiario.getPersonType());//+"|"
                    mapBeneficiarios.put("NOM_TERCERO", beneficiario.getContractingFistName());//+"|"
                    mapBeneficiarios.put("APE1_TERCERO", beneficiario.getContractingMiddleName());//+"|"
                    mapBeneficiarios.put("APE2_TERCERO", beneficiario.getContractingLastName());//+"|"
                    mapBeneficiarios.put("COD_OCUPACION", beneficiario.getOccupationCode());//+"|"
                    mapBeneficiarios.put("PCT_PARTICIPACION", beneficiario.getPercentageParticipation());//+"|"
                    mapBeneficiarios.put("TIP_BENEF", beneficiario.getBeneficiaryType());//+"|"
                    mapBeneficiarios.put("NUM_SECU", beneficiario.getSequenceNumber());//+"|"
                    listaBeneficiarios.add(mapBeneficiarios);
                }
            }
        }
        /** Se forma el arreglo de <DATOS_BANCARIOS> ******************************************************************/
        String tipo_pago;
        Date fchaVto;
        Map<String, String> mapDatosBancarios = new LinkedHashMap<>();
        /** Sept 19 : Se cambia la estructura del XML para Datos Bancarios */
        if (validaExistE(request, DatosMinimos.D_BANCARIOS)) {
            tipo_pago = request.getBankData().getPaymentType(); //Obtiene Valor de tipo pago
            BankData datosBancariosDTO = request.getBankData();

            /** Case "APE1_TERCERO"*/
            mapDatosBancarios.put("APE1_TITULAR", datosBancariosDTO.getContractingMiddleName());

            /** Case "NOM_TERCERO" */
            mapDatosBancarios.put("NOM_TITULAR", datosBancariosDTO.getContractingFistName());
            /** Case "CTA_CTE" */
            if (datosBancariosDTO.getPaymentType().equals(DatosBancarios.DEBITO) || datosBancariosDTO.getPaymentType().equals(DatosBancarios.CLABE)) {
                mapDatosBancarios.put("NUM_CUENTA", datosBancariosDTO.getAccountNumber());
            }
            /** Case "NUM_TARJETA" */
            else if (datosBancariosDTO.getPaymentType().equals(DatosBancarios.TC)) {
                mapDatosBancarios.put("NUM_CUENTA", datosBancariosDTO.getCardNumber());
            }
            /** Case "ANO_VCTO_TARJETA" */
            mapDatosBancarios.put("ANO_VCTO_TARJETA", "20" + datosBancariosDTO.getCardExpirationYear());
            /** Case "APE2_TERCERO" */
            if (datosBancariosDTO.getPaymentType().equals(DatosBancarios.DEBITO) || datosBancariosDTO.getPaymentType().equals(DatosBancarios.TC)) {
                mapDatosBancarios.put("APE2_TITULAR", datosBancariosDTO.getContractingLastName());
            }
            /** Case Else */
            mapDatosBancarios.put("COD_ENTIDAD", datosBancariosDTO.getEntityCode());
            mapDatosBancarios.put("FEC_VCTO_TARJETA", datosBancariosDTO.getCardExpirationDate());
            mapDatosBancarios.put("MES_VCTO_TARJETA", datosBancariosDTO.getCardExpirationMonth());
            mapDatosBancarios.put("EMAIL", datosBancariosDTO.getEmail());
            mapDatosBancarios.put("COD_USR", datosBancariosDTO.getUserCode());
            mapDatosBancarios.put("COD_TARJETA", datosBancariosDTO.getCardCode());
            mapDatosBancarios.put("TIP_TARJETA", datosBancariosDTO.getCardType());
            mapDatosBancarios.put("FEC_CORTE", datosBancariosDTO.getCutOffDate());
            mapDatosBancarios.put("NUM_PLAZA", String.valueOf(datosBancariosDTO.getPlaceNumber()));

            /** Select Case Tipo_pago */
            switch (tipo_pago) {
                case DatosBancarios.DEBITO: {
                    mapDatosBancarios.put("TIP_CUENTA", "A");
                }
                break;
                case DatosBancarios.TC: {
                    mapDatosBancarios.put("TIP_CUENTA", "C");
                }
                break;
                case DatosBancarios.CLABE: {
                    mapDatosBancarios.put("TIP_CUENTA", "D");
                }
                break;
            }
            mapDatosBancarios.put("COD_DOCUM", drContratante[1]);
        }
        /** Se convierten los ArrayList a arreglos de cadenas sencillos *******************************************/
        /** Se corre el proceso de emision */
        String strNumeroPoliza;
        EmisionVidaFM objEmisionVida = new EmisionVidaFM(iMifelVidaULRepository, iThirdRepository);
        strNumeroPoliza = objEmisionVida.getEmision(mapPoliza, listaDatosVariables, listaDatosVariablesCoverage, listCoberturas, mapContratante,
                mapAsegurado, listaBeneficiarios, mapDatosBancarios, "");

        /** Se crea salida */
        if (strNumeroPoliza != null || strNumeroPoliza.length() == 0) {
            response.setPolicyId(strNumeroPoliza);
            response.setCode(MifelVidaMensajesEnum.SUCCESS_OPERATION.getCodigo());

            //Load Tax Info
//            objEmisionVida.setTaxData(request);

        } else {
            response.setCode(MifelVidaMensajesEnum.GENERATE_POLICY_ERROR.getCodigo());
        }
        return response;

    }

    /**
     * Retorna el hostname o address del servidor local
     *
     * @return
     */
    private String getClientHost1() {
        HostUtil hostUtil = new HostUtil();
        return hostUtil.getAddress();
    }

    @Override
    public String validateEntry(IssueRequest request) {
        String response = "";
        /**
         * Valida todos los nodos
         */
        if (validaNodos(request)) {

            /** Se valida que existan los datos minimos dentro de cada nodo ***********************************************/
            /**<DATOS_POLIZA>  Datos minimos para el nodo de DATOS_POLIZA de la cadena de Emision de Vida **/
            Object[] objetoPoliza = {request.getPolicyData().getBranchCode(),
                    request.getPolicyData().getAgentCode(), request.getPolicyData().getPolicyEffectiveDate(), request.getPolicyData().getPolicyEndDate(),
                    request.getPolicyData().getDocumCode(), request.getPolicyData().getManagerType(), request.getPolicyData().getPaymentMethodCode()};
            Map<String, Object> polizaDatos = this.creaHasMap(DatosMinimos.DATOS_POLIZA_EMI_VIDA, objetoPoliza);
            validaDatosMinimos(polizaDatos, DatosMinimos.D_POLIZA);
            /** Obtiene cod_ramo*/setCod_ramo(request.getPolicyData().getBranchCode());
            /**<DATOS_VARIABLES> **/
            for (VariableData datos : request.getVariableData()) {
                Object[] objetoVariables = {datos.getRiskNumber(), datos.getLevelType()};
                Map<String, Object> variablesDatos = this.creaHasMap(DatosMinimos.DATOS_VARIABLES_EMI_VIDA, objetoVariables);
                validaDatosMinimos(variablesDatos, DatosMinimos.D_VARIABLES);
                /**<CAMPO> **/
                for (FieldRisk campo : datos.getFields()) {
                    Object[] objetoCampo = {campo.getFieldCode(), campo.getFieldValue()};
                    Map<String, Object> campoDatos = this.creaHasMap(DatosMinimos.CAMPO_VARIABLE_EMI_VIDA, objetoCampo);
                    validaDatosMinimos(campoDatos, DatosMinimos.D_CAMPO);
                }
            }
            /**<COBERTURA> **/
            for (Coverage cobertura : request.getCoverage()) {
                Object[] objetoVariables = {cobertura.getCoverageCode(), cobertura.getSumInsuredAmn()};
                Map<String, Object> coberturaDatos = this.creaHasMap(DatosMinimos.COBERTURA_VIDA, objetoVariables);
                validaDatosMinimos(coberturaDatos, DatosMinimos.D_COBERTURA);
            }
            /**<DATOS_CONTRATANTE> **/
            Object[] objetoContratante = {
                    request.getContractorData().getDocumCode(), request.getContractorData().getContractingFistName(), request.getContractorData().getContractingMiddleName(),
                    request.getContractorData().getContractingLastName(), request.getContractorData().getAddressName1(), request.getContractorData().getAddressName2(),
                    request.getContractorData().getAddressName3(), request.getContractorData().getPostalCode(), request.getContractorData().getStateCode(),
                    request.getContractorData().getProvinceCode(), request.getContractorData().getCountryCode(), request.getContractorData().getTelephoneNumber(),
                    request.getContractorData().getDateBirth(), request.getContractorData().getGender(), request.getContractorData().getMaritalStatusCode(),
                    request.getContractorData().getPersonType(), request.getContractorData().getBeneficiaryType(), request.getContractorData().getOccupationCode(),
                    request.getContractorData().getEmail()};
            Map<String, Object> contratanteDatos = this.creaHasMap(DatosMinimos.CAMPO_CONTRATANTE_EMI_VIDA, objetoContratante);
            validaDatosMinimos(contratanteDatos, DatosMinimos.D_CONTRATANTE);

            /**<DATOS_ASEGURADO> **/
            Object[] objetoAsegurado = {
                    request.getInsuredData().getDocumCode(), request.getInsuredData().getContractingFistName(), request.getInsuredData().getContractingMiddleName(),
                    request.getInsuredData().getContractingLastName(), request.getInsuredData().getAddressName1(), request.getInsuredData().getAddressName2(),
                    request.getInsuredData().getAddressName3(), request.getInsuredData().getPostalCode(), request.getInsuredData().getStateCode(),
                    request.getInsuredData().getProvinceCode(), request.getInsuredData().getCountryCode(), request.getInsuredData().getTelephoneNumber(),
                    request.getInsuredData().getDateBirth(), request.getInsuredData().getGender(), request.getInsuredData().getMaritalStatusCode(),
                    request.getInsuredData().getPersonType(), request.getInsuredData().getOccupationCode(), request.getInsuredData().getBeneficiaryType(),
                    request.getInsuredData().getInsuredObservations()};
            Map<String, Object> aseguradoDatos = this.creaHasMap(DatosMinimos.CAMPO_ASEGURADO_EMI_VIDA, objetoAsegurado);
            validaDatosMinimos(aseguradoDatos, DatosMinimos.D_ASEGURADO);
        }
        /**<BENEFICIARIO>
         * {"MCA_FISICO", "NOM_TERCERO", "APE1_TERCERO", "APE2_TERCERO", "COD_OCUPACION", "PCT_PARTICIPACION",
         "TIP_BENEF", "NUM_SECU"};**/
        if (validaExistE(request, DatosMinimos.D_BENEFICIARIO)) {
            for (Beneficiaries beneficiario : request.getBeneficiary()) {
                Object[] objetoBeneficiario = {beneficiario.getPersonType(), beneficiario.getContractingFistName(),
                        beneficiario.getContractingMiddleName(), beneficiario.getContractingLastName(), beneficiario.getOccupationCode(),
                        beneficiario.getPercentageParticipation(), beneficiario.getBeneficiaryType(), beneficiario.getSequenceNumber()};
                Map<String, Object> beneficiarioDatos = this.creaHasMap(DatosMinimos.CAMPO_BENEFICIARIO_EMI_VIDA, objetoBeneficiario);
                validaDatosMinimos(beneficiarioDatos, DatosMinimos.D_BENEFICIARIO);
            }
        }
        /**<DATOS_BANCARIOS> Se valida si existen los Datos Bancarios **/
        if (validaExistE(request, DatosMinimos.D_BANCARIOS)) {
            if (request.getBankData().getPaymentType().equals(DatosBancarios.DEBITO)) {
                Object[] objetoDebito = {request.getBankData().getContractingFistName(), request.getBankData().getContractingMiddleName(),
                        request.getBankData().getContractingLastName(), request.getBankData().getEntityCode(),
                        request.getBankData().getAccountNumber(), request.getBankData().getEmail(),
                        request.getBankData().getCardExpirationMonth(), request.getBankData().getCardExpirationYear()};
                Map<String, Object> bancarioDebito = this.creaHasMap(DatosMinimos.CAMPO_DB_DEBITO_VIDA, objetoDebito);
                validaDatosMinimos(bancarioDebito, DatosMinimos.D_BANCARIOS);
            }
            if (request.getBankData().getPaymentType().equals(DatosBancarios.CLABE)) {
                Object[] objetoClabe = {
                        request.getBankData().getContractingFistName(), request.getBankData().getContractingMiddleName(),
                        request.getBankData().getEntityCode(), request.getBankData().getAccountNumber(),
                        request.getBankData().getEmail()};
                Map<String, Object> bancarioClabe = this.creaHasMap(DatosMinimos.CAMPO_DB_CLABE_VIDA, objetoClabe);
                validaDatosMinimos(bancarioClabe, DatosMinimos.D_BANCARIOS);
            }
            if (request.getBankData().getPaymentType().equals(DatosBancarios.TC)) {
                Object[] objetoTC = {
                        request.getBankData().getContractingFistName(), request.getBankData().getContractingMiddleName(),
                        request.getBankData().getContractingLastName(), request.getBankData().getCardType(),
                        request.getBankData().getEntityCode(), request.getBankData().getCardNumber(),
                        request.getBankData().getEmail(), request.getBankData().getCardExpirationMonth(),
                        request.getBankData().getCardExpirationYear()};
                Map<String, Object> bancarioTC = this.creaHasMap(DatosMinimos.CAMPO_DB_TC_VIDA, objetoTC);
                validaDatosMinimos(bancarioTC, DatosMinimos.D_BANCARIOS);
            }
        }
        response = "success";
        LOGGER.info("Las validaciones son: " + response);
        //this.construyeXML(request);
        return response;
    }



    private Boolean validaExistE(IssueRequest request, String nodo) {
        Boolean exist = false;
        switch (nodo) {
            case DatosMinimos.D_BENEFICIARIO: {
                try {
                    if (request.getBeneficiary() == null) {//request.getBeneficiario().stream().collect(Collectors.counting()) == 0
                        LOGGER.info(Errors.ERROR_NODO + nodo);
                        //throw new EmtyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
                    } else {
                        exist = true;
                    }
                } catch (NullPointerException ex) {
                    LOGGER.info(Errors.ERROR_NODO + nodo);
                    throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
                }
                break;
            }
            case DatosMinimos.D_BANCARIOS: {
                try {
                    if (request.getBankData() == null) {
                        //throw new EmtyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
                        LOGGER.info(Errors.ERROR_NODO + nodo);
                    } else {
                        exist = true;
                    }
                } catch (NullPointerException ex) {
                    LOGGER.info(Errors.ERROR_NODO + nodo);
                    throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
                }
                break;
            }
        }
        return exist;
    }

    /**
     * Se valida que existan las etiquetas necesarias para la cotizacion
     *
     * @param request
     * @return Boolean
     */
    private Boolean validaNodos(IssueRequest request) {
        Boolean existeNodo = false;
        String nodo = "";
        nodo = DatosMinimos.D_POLIZA; /**<DATOS_POLIZA>*/
        try {
            if (request.getPolicyData() == null) {
                throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
            }
        } catch (NullPointerException ex) {
            throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
        }
        nodo = DatosMinimos.D_VARIABLES; /**<DATOS_VARIABLES>*/
        try {
            if (request.getVariableData().stream().collect(Collectors.counting()) == 0) {
                throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
            }
        } catch (NullPointerException ex) {
            throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
        }
        nodo = DatosMinimos.D_COBERTURA; /**<COBERTURAS>*/
        try {
            if (request.getCoverage().stream().collect(Collectors.counting()) == 0) {

                throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
            }
        } catch (NullPointerException ex) {
            throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
        }
        nodo = DatosMinimos.D_ASEGURADO; /**<DATOS_CONTRATANTE> se cambia a DATOS_ASEGURADO*/
        try {
            if (request.getInsuredData() == null) {

                throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
            }
        } catch (NullPointerException ex) {
            throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
        }
        existeNodo = true;
        return existeNodo;
    }

    /**
     * Crear un MAP a partir de dos arreglos
     *
     * @return LinkedHashMap
     */
    private Map<String, Object> creaHasMap(String[] nombreV, Object[] valores) {
        /*Map<String, Object> myMap = IntStream.range(0, nombreV.length)
                .collect(HashMap::new, (m, i) -> m.put(nombreV[i], valores[i]), Map::putAll);*/
        Map<String, Object> myMap = new LinkedHashMap<>();
        if (nombreV.length == valores.length) {
            for (int i = 0; i < nombreV.length; i++) {
                myMap.put(nombreV[i], valores[i]);
            }
        }
        return myMap;
    }

    /**
     * 'Funcion para validar los datos minimos necesarios para el nodo indicado dentro de la cadena de XML
     *
     * @return
     */
    private String validaDatosMinimos(Map<String, Object> lista, String nodo) {
        String mensaje = "";
        Integer numErrores = 0;
        for (Map.Entry<String, Object> entry : lista.entrySet()) {
            if (entry.getValue() == null || entry.getValue().toString().length() == 0) {///validar que no contenga la palabra string
                LOGGER.info("El elemento: " + entry.getKey() + " no puede ser vacio o nulo");
                mensaje = "El nodo <" + nodo + "> requiere del siguiente atributo y su valor: " + entry.getKey();
                numErrores++;
                throw new EmptyValueException(10, mensaje);
            }else if(entry.getValue().equals("string")){
                mensaje = "El nodo <" + nodo + "> requiere datos validos: " ;//SE quita el valor de entry.getKey() ya que viene en español
                throw new EmptyValueException(10, mensaje);
            }
        }
        mensaje = "success";
        return mensaje;
    }

    @Override
    public String validaEtiquetas(SimulationRequest request) {
        //VidaEnteraEmisionResponse response = new VidaEnteraEmisionResponse();
        String response = "";
        /**
         * Valida todos los nodos
         */
        if (validaNodosCotizacion(request)) {

            /** Se valida que existan los datos minimos dentro de cada nodo ***********************************************/
            /**<DATOS_POLIZA>  Datos minimos para el nodo de DATOS_POLIZA de la cadena de Emision de Vida **/
        	LOGGER.debug("Poliza");
            Object[] objetoPoliza = {request.getPolicyData().getBranchCode(),
                    request.getPolicyData().getAgentCode(), request.getPolicyData().getPolicyEffectiveDate(), request.getPolicyData().getPolicyEndDate(),
                    request.getPolicyData().getDocumCode(), request.getPolicyData().getManagerType(), request.getPolicyData().getPaymentMethodCode()};
            Map<String, Object> polizaDatos = this.creaHasMap(DatosMinimos.DATOS_POLIZA_EMI_VIDA, objetoPoliza);
            validaDatosMinimos(polizaDatos, DatosMinimos.D_POLIZA);
            /** Obtiene cod_ramo*/setCod_ramo(request.getPolicyData().getBranchCode());
            /**<DATOS_VARIABLES> **/
            LOGGER.debug("Datos variables");
            for (VariableData datos : request.getVariableData()) {
                Object[] objetoVariables = {datos.getRiskNumber(), datos.getLevelType()};
                Map<String, Object> variablesDatos = this.creaHasMap(DatosMinimos.DATOS_VARIABLES_EMI_VIDA, objetoVariables);
                validaDatosMinimos(variablesDatos, DatosMinimos.D_VARIABLES);
                /**<CAMPO> **/
                for (FieldRisk campo : datos.getFields()) {
                    Object[] objetoCampo = {campo.getFieldCode(), campo.getFieldValue()};
                    Map<String, Object> campoDatos = this.creaHasMap(DatosMinimos.CAMPO_VARIABLE_EMI_VIDA, objetoCampo);
                    validaDatosMinimos(campoDatos, DatosMinimos.D_CAMPO);
                }
            }
            /**<COBERTURA> **/
            LOGGER.debug("Cobertura");
            for (Coverage cobertura : request.getCoverage()) {
                Object[] objetoVariables = {cobertura.getCoverageCode(), cobertura.getSumInsuredAmn()};
                Map<String, Object> coberturaDatos = this.creaHasMap(DatosMinimos.COBERTURA_VIDA, objetoVariables);
                validaDatosMinimos(coberturaDatos, DatosMinimos.D_COBERTURA);
            }
            /**<DATOS_CONTRATANTE> **/
            LOGGER.debug("Contratante");
            Object[] objetoContratante = {
                    request.getInsuredData().getDocumCode(), request.getInsuredData().getContractingFistName(), request.getInsuredData().getContractingMiddleName(),
                    request.getInsuredData().getContractingLastName(), request.getInsuredData().getAddressName1(), request.getInsuredData().getAddressName2(),
                    request.getInsuredData().getAddressName3(), request.getInsuredData().getPostalCode(), request.getInsuredData().getStateCode(),
                    request.getInsuredData().getProvinceCode(), request.getInsuredData().getCountryCode(), request.getInsuredData().getTelephoneNumber(),
                    //request.getInsuredData().getDateBirth(), 
                    request.getInsuredData().getGender(), request.getInsuredData().getMaritalStatusCode(),
                    request.getInsuredData().getPersonType(), request.getInsuredData().getBeneficiaryType(), request.getInsuredData().getOccupationCode(),
                    request.getInsuredData().getEmail()};
            Map<String, Object> contratanteDatos = this.creaHasMap(DatosMinimos.CAMPO_CONTRATANTE_EMI_VIDA, objetoContratante);
            validaDatosMinimos(contratanteDatos, DatosMinimos.D_ASEGURADO);
        }
        /**<BENEFICIARIO>
         * {"MCA_FISICO", "NOM_TERCERO", "APE1_TERCERO", "APE2_TERCERO", "COD_OCUPACION", "PCT_PARTICIPACION",
         "TIP_BENEF", "NUM_SECU"};**/
        //if (validaExist(request, DatosMinimos.D_BENEFICIARIO)) {
        LOGGER.debug("Beneficiario");
        if(request.getBeneficiary().isPresent()) {
            for (Beneficiaries beneficiario : request.getBeneficiary().get()) {
                Object[] objetoBeneficiario = {beneficiario.getPersonType(), beneficiario.getContractingFistName(),
                        beneficiario.getContractingMiddleName(), beneficiario.getContractingLastName(), beneficiario.getOccupationCode(),
                        beneficiario.getPercentageParticipation(), beneficiario.getBeneficiaryType(), beneficiario.getSequenceNumber()};
                Map<String, Object> beneficiarioDatos = this.creaHasMap(DatosMinimos.CAMPO_BENEFICIARIO_EMI_VIDA, objetoBeneficiario);
                validaDatosMinimos(beneficiarioDatos, DatosMinimos.D_BENEFICIARIO);
            }
        }
        /**<DATOS_BANCARIOS> Se valida si existen los Datos Bancarios **/
        LOGGER.debug("Datos bancarios");
        if (validaExist(request, DatosMinimos.D_BANCARIOS)) {
            if (request.getBankData().getPaymentType().equals(DatosBancarios.DEBITO)) {
                Object[] objetoDebito = {request.getBankData().getContractingFistName(), request.getBankData().getContractingMiddleName(),
                        request.getBankData().getContractingLastName(), request.getBankData().getEntityCode(),
                        request.getBankData().getAccountNumber(), request.getBankData().getEmail(),
                        request.getBankData().getCardExpirationMonth(), request.getBankData().getCardExpirationYear()};
                Map<String, Object> bancarioDebito = this.creaHasMap(DatosMinimos.CAMPO_DB_DEBITO_VIDA, objetoDebito);
                validaDatosMinimos(bancarioDebito, DatosMinimos.D_BANCARIOS);
            }
            if (request.getBankData().getPaymentType().equals(DatosBancarios.CLABE)) {
                Object[] objetoClabe = {
                        request.getBankData().getContractingFistName(), request.getBankData().getContractingMiddleName(),
                        request.getBankData().getEntityCode(), request.getBankData().getAccountNumber(),
                        request.getBankData().getEmail()};
                Map<String, Object> bancarioClabe = this.creaHasMap(DatosMinimos.CAMPO_DB_CLABE_VIDA, objetoClabe);
                validaDatosMinimos(bancarioClabe, DatosMinimos.D_BANCARIOS);
            }
            if (request.getBankData().getPaymentType().equals(DatosBancarios.TC)) {
                Object[] objetoTC = {
                        request.getBankData().getContractingFistName(), request.getBankData().getContractingMiddleName(),
                        request.getBankData().getContractingLastName(), request.getBankData().getCardType(),
                        request.getBankData().getEntityCode(), request.getBankData().getCardNumber(),
                        request.getBankData().getEmail(), request.getBankData().getCardExpirationMonth(),
                        request.getBankData().getCardExpirationYear()};
                Map<String, Object> bancarioTC = this.creaHasMap(DatosMinimos.CAMPO_DB_TC_VIDA, objetoTC);
                validaDatosMinimos(bancarioTC, DatosMinimos.D_BANCARIOS);
            }
        }
        response = "success";
        LOGGER.info("Las validaciones son: " + response);
        //this.construyeXML(request);
        return response;
    }



    private Boolean validaExist(SimulationRequest request, String nodo) {
        Boolean exist = false;
        switch (nodo) {
            /*case DatosMinimos.D_BENEFICIARIO: {
                try {
                    if (request.getBeneficiary() == null) {//request.getBeneficiario().stream().collect(Collectors.counting()) == 0
                        LOGGER.info(Errors.ERROR_NODO + nodo);
                        //throw new EmtyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
                    } else {
                        exist = true;
                    }
                } catch (NullPointerException ex) {
                    LOGGER.info(Errors.ERROR_NODO + nodo);
                    throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
                }
                break;
            }*/
            case DatosMinimos.D_BANCARIOS: {
                try {
                    if (request.getBankData() == null) {
                        //throw new EmtyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
                        LOGGER.info(Errors.ERROR_NODO + nodo);
                    } else {
                        exist = true;
                    }
                } catch (NullPointerException ex) {
                    LOGGER.info(Errors.ERROR_NODO + nodo);
                    throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
                }
                break;
            }
        }
        return exist;
    }

    /**
     * Se valida que existan las etiquetas necesarias para la cotizacion
     *
     * @param request
     * @return Boolean
     */
    private Boolean validaNodosCotizacion(SimulationRequest request) {
        Boolean existeNodo = false;
        String nodo = "";
        nodo = DatosMinimos.D_POLIZA; /**<DATOS_POLIZA>*/
        try {
            if (request.getPolicyData() == null) {
                throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
            }
        } catch (NullPointerException ex) {
            throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
        }
        nodo = DatosMinimos.D_VARIABLES; /**<DATOS_VARIABLES>*/
        try {
            if (request.getVariableData().stream().collect(Collectors.counting()) == 0) {
                throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
            }
        } catch (NullPointerException ex) {
            throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
        }
        nodo = DatosMinimos.D_COBERTURA; /**<COBERTURAS>*/
        try {
            if (request.getCoverage().stream().collect(Collectors.counting()) == 0) {

                throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
            }
        } catch (NullPointerException ex) {
            throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
        }
        nodo = DatosMinimos.D_ASEGURADO; /**<DATOS_CONTRATANTE> se cambia a DATOS_ASEGURADO*/
        try {
            if (request.getInsuredData() == null) {

                throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
            }
        } catch (NullPointerException ex) {
            throw new EmptyValueException(Errors.ERROR_400, Errors.ERROR_NODO + nodo);
        }
        existeNodo = true;
        return existeNodo;
    }


    /**
     * =================================================================================================================
     * Función para Cotizar VIDA
     * =================================================================================================================
     *
     * @return
     */
    @Override
    public VidaULCotizacionResponse getCotizacion(SimulationRequest request) {
        /** Se obtiene el ramo */
        String strRamo = "112";//getCod_ramo();
        String[] drContratante = new String[2];
        /** Se llama a la clase CLM */
        //CLM clm = new CLM();
        /***************************************************************************************************************
         *  Se forma el arreglo de <DATOS_POLIZA>
         */
        setBanderaCLM("false");
        PolicyData datosPolizaDTO = request.getPolicyData();
        Map<String, String> mapPoliza = new LinkedHashMap<>();
        if (BANDERA_CLM.equals("True")) {
            //datosPolizaDTO.setCod_ramo("");
            mapPoliza = datosPolizaDTO.dameLista(datosPolizaDTO);
        } else {
            mapPoliza = datosPolizaDTO.dameLista(datosPolizaDTO);
        }
        
        String fechaNac = null;
        /**
         * Datos Fijos
         */
        mapPoliza.put("FEC_EFEC_SPTO", request.getPolicyData().getPolicyEffectiveDate());
        mapPoliza.put("FEC_VCTO_SPTO", request.getPolicyData().getPolicyEndDate());

        if (request.getPolicyData().getManagerType().equals("AG")) {
            mapPoliza.put("COD_GESTOR", request.getPolicyData().getAgentCode());
        } else {
            mapPoliza.put("COD_GESTOR", "");
        }
        mapPoliza.put("MCA_IMPRESION", "N");
        mapPoliza.put("TXT_MOTIVO_SPTO", "C");
        
        /***************************************************************************************************************
         *  Se forma el arreglo de <DATOS_VARIABLES>
         */
        List<Map<String, String>> listaDatosVariables = new ArrayList<Map<String, String>>();
        int strNumSecu = 0;
        String strValor = "";
        /** Se recorre el arreglo de nodos de DATOS_VARIABLES*/
        strNumSecu = 1;
        String sCOD_MODALIDAD = "";
        for (VariableData datosV : request.getVariableData()) {

            /**Se recorre el arreglo de nodos de CAMPO*/
            for (FieldRisk campo : datosV.getFields()) {
                Map<String, String> mapDVariables = new LinkedHashMap<>();
                if (campo.getFieldValue().length() > 10) {
                    strValor = campo.getFieldValue().trim().substring(0, 10);
                } else {
                    strValor = campo.getFieldValue();
                }
                campo.convertDate(campo);
                
                mapDVariables.put("COD_CAMPO", campo.getFieldCode()); //+"|"
                mapDVariables.put("VAL_CAMPO", campo.getFieldValue()); //+"|"
                mapDVariables.put("VAL_COR_CAMPO", strValor); //+"|"
                mapDVariables.put("TIP_NIVEL", datosV.getLevelType()); //+"|"
                mapDVariables.put("NUM_SECU", String.valueOf(strNumSecu)); //+"|"
                mapDVariables.put("NUM_RIESGO", datosV.getRiskNumber()); //+"|"
                mapDVariables.put("COD_RAMO", strRamo);
                
                if(campo.getFieldCode().equals("ANIOS_DURACION_POLIZA")) {
                	mapPoliza.put("ANIOS_MAX_DURACION", (String)campo.getFieldValue());
                }
                if(campo.getFieldCode().equals("COD_MODALIDAD")) {
                	sCOD_MODALIDAD = campo.getFieldValue();
//                	mapDVariables.put("NUM_RIESGO", "1");
//                	mapDVariables.put("TIP_NIVEL", "2");
                }
                if(campo.getFieldCode().equals("FEC_NACIMIENTO")) {
                	fechaNac = campo.getFieldValue();

                }
//                if(campo.getFieldCode().equals("FEC_NACIMIENTO")) {
//                	mapDVariables.put("NUM_RIESGO", "1");
//                	mapDVariables.put("TIP_NIVEL", "2");
//                }
//                if(campo.getFieldCode().equals("MCA_SEXO")) {
//                	mapDVariables.put("NUM_RIESGO", "1");
//                	mapDVariables.put("TIP_NIVEL", "2");
//                }
//                if(campo.getFieldCode().equals("DTO_AGENTE_DIR")) {
//                	mapDVariables.put("NUM_RIESGO", "1");
//                	mapDVariables.put("TIP_NIVEL", "3");
//                }
//                if(campo.getFieldCode().equals("ANIOS_DURACION_POLIZA")) {
//                	mapDVariables.put("NUM_RIESGO", "1");
//                	mapDVariables.put("TIP_NIVEL", "1");
//                }
//                if(campo.getFieldCode().equals("TIP_PERFIL_INV")) {
//                	mapDVariables.put("NUM_RIESGO", "1");
//                	mapDVariables.put("TIP_NIVEL", "2");
//                }
//                if(campo.getFieldCode().equals("NUM_FUNDOS")) {
//                	mapDVariables.put("NUM_RIESGO", "1");
//                	mapDVariables.put("TIP_NIVEL", "2");
//                }
//                if(campo.getFieldCode().equals("TIP_PRIMAS")) {
//                	mapDVariables.put("NUM_RIESGO", "1");
//                	mapDVariables.put("TIP_NIVEL", "2");
//                }
//                if(campo.getFieldCode().equals("PRIMA_COB_1000")) {
//                	mapDVariables.put("NUM_RIESGO", "1");
//                	mapDVariables.put("TIP_NIVEL", "2");
//                }
                
                listaDatosVariables.add(mapDVariables);
                LOGGER.info("listaDatosVariables: " + mapDVariables.values());
                strNumSecu++;
                
            }
            
        }

        /***************************************************************************************************************
         *  Se forma el arreglo de <DATOS_VARIABLES COVERAGE>
         */
        List<Map<String, String>> listaDatosVariablesCoverage = new ArrayList<Map<String, String>>();
        strNumSecu = 0;
        strValor = "";
        String sCodLista = "";
        String sCodListaValor = "";
//        sCOD_MODALIDAD = "";
        /** Se recorre el arreglo de nodos de DATOS_VARIABLES COVERAGE*/
        for (VariableDataCoverage datosV : request.getVariableDataCoverage()) {
            strNumSecu = 1;
            /**Se recorre el arreglo de nodos de CAMPO*/
            for (FieldRisk campo : datosV.getFields()) {
                Map<String, String> mapDVariablesCoverage = new LinkedHashMap<>();
                campo.convertDate(campo);

                mapDVariablesCoverage.put("COD_CAMPO", campo.getFieldCode()); //+"|"
                mapDVariablesCoverage.put("VAL_CAMPO", campo.getFieldValue()); //+"|"
                if(sCodLista.equals("FORMA_PAGO"))
                	mapDVariablesCoverage.put("TXT_CAMPO", ""); //+"|"
                else
                	mapDVariablesCoverage.put("TXT_CAMPO", "1"); //+"|"
                sCodLista = campo.getFieldCode();
                if(sCodLista.equals("CODIGO_FUNDO") || sCodLista.equals("PERCENTAGEM_FUNDO"))
                	sCodListaValor="600";
                else
                	sCodListaValor="601";
                
                mapDVariablesCoverage.put("COD_LISTA", sCodListaValor); //+"|"
                mapDVariablesCoverage.put("NUM_RIESGO", datosV.getRiskNumber()); //+"|"
                mapDVariablesCoverage.put("NUM_PERIODO", "1"); //+"|"
                mapDVariablesCoverage.put("NUM_OCURRENCIA", datosV.getLevelType());
                mapDVariablesCoverage.put("NUM_SECU", String.valueOf(strNumSecu)); //+"|"
                
                listaDatosVariablesCoverage.add(mapDVariablesCoverage);
                LOGGER.info("Cotizacion - listaDatosVariablesCoverage: " + mapDVariablesCoverage.values());
                strNumSecu++;
            }
        }

        /** ************************************************************************************************************
        
        /** ************************************************************************************************************
         * Se forma el arreglo de <COBERTURAS>
         */
        List<Map<String, String>> listCoberturas = new ArrayList<Map<String, String>>();
        strNumSecu = 1;
        String cob1000 = null;
        
        if(request.getPolicyData().getContractNumber().equals("11228") || request.getPolicyData().getContractNumber().equals("11229")) {
        	List<Coverage> reqcoverages = request.getCoverage();
        	Coverage c = new Coverage();
        	c.setCoverageCode("1095");
        	c.setSumInsuredAmn("0");
        	reqcoverages.add(c);
        	
        	c = new Coverage();
        	c.setCoverageCode("1001");
        	c.setSumInsuredAmn("0");
        	reqcoverages.add(c);
        }

        for (Coverage coberturas : request.getCoverage()) {
            Map<String, String> mapCoberturas = new LinkedHashMap<>();
            mapCoberturas.put("COD_COB", coberturas.getCoverageCode()); //+"|"
            mapCoberturas.put("SUMA_ASEG", coberturas.getSumInsuredAmn()); //+"|"
            mapCoberturas.put("SUMA_ASEG_SPTO", coberturas.getSumInsuredAmn()); //+"|"
            mapCoberturas.put("NUM_SECU", Integer.toString(strNumSecu)); //+"|"
            mapCoberturas.put("MCA_BAJA_COB", "N");
            mapCoberturas.put("COD_RAMO", strRamo);
            listCoberturas.add(mapCoberturas);
            
            if(coberturas.getCoverageCode().equals("1000")) {
            	cob1000 = coberturas.getSumInsuredAmn();
            }
            
            strNumSecu++;
        }
        
        
        
        

        /** ************************************************************************************************************
         * Se forma el arreglo de <DATOS_CONTRATANTE>
         *     Se cambia contractorData por insuredData para la Cotizacion:
         *     Cambio solicitado por Hector Ramirez 13-10-2021
         */
        Map<String, String> mapContratante = request.getInsuredData().dameLista(request.getInsuredData());
        /** 'Datos Fijos*/
        
        String name = request.getInsuredData().getContractingFistName().orElse("");
        String middleName =  request.getInsuredData().getContractingMiddleName().orElse("");
        String lastName =  request.getInsuredData().getContractingLastName().orElse("");
        String rfc = request.getInsuredData().getDocumCode().orElse("XAXX010101000");
        String address1 = request.getInsuredData().getAddressName1().orElse("");
        String address2 = request.getInsuredData().getAddressName2().orElse("");
        String address3 = request.getInsuredData().getAddressName3().orElse("");
        String postalCode = request.getInsuredData().getPostalCode().orElse("");;
        String stateCode = request.getInsuredData().getStateCode().orElse("");;
        String provinceCode = request.getInsuredData().getProvinceCode().orElse("");
        String countryCode = request.getInsuredData().getCountryCode().orElse("");
        String telephone = request.getInsuredData().getTelephoneNumber().orElse("");
        
        String birthDay = request.getInsuredData().getDateBirth();
        
        request.getInsuredData().setRealAge(Optional.of(getRealYears(fechaNac)));
        
        mapContratante.put("TIP_DOCUM", "RFC");
        mapContratante.put("RFC", rfc);
        mapContratante.put("COD_IDENTIFICADOR", USER_CODE + "_" + getClientHost1().replace(".", "_"));
        mapContratante.put("NOM_RIESGO", name + " " + middleName + " " + lastName);
        mapContratante.put("NOM_DOMICIO1_COM", address1);
        mapContratante.put("NOM_DOMICIO2_COM", address2);
        mapContratante.put("NOM_DOMICIO3_COM", address3);
        mapContratante.put("COD_POSTAL_COM", postalCode);
        mapContratante.put("COD_ESTADO_COM", stateCode);
        mapContratante.put("COD_PROV_COM", provinceCode);
        mapContratante.put("COD_PAIS_COM", countryCode);
        mapContratante.put("TLF_NUMERO_COM", telephone);
        mapContratante.put("TXT_ETIQUETA2", address3);
        mapContratante.put("NOM_LOCALIDAD", address3);
        mapContratante.put("NOM_LOCALIDAD_COM", address3);
        mapContratante.put("TXT_ETIQUETA4", address3 + address2);
        mapContratante.put("COD_LOCALIDAD", provinceCode);
        mapContratante.put("NUM_SECU", "1");
        mapContratante.put("FEC_EFEC_RIESGO", request.getPolicyData().getPolicyEffectiveDate());
        mapContratante.put("FEC_VCTO_RIESGO", request.getPolicyData().getPolicyEndDate());

        if (BANDERA_CLM.equals("True")) {
            /**Se obtiene la clave de CLM para el contratante*/
            String tip_benef = request.getInsuredData().getBeneficiaryType();
            LOGGER.info("Alta de tercero - mapContratante: ");
            drContratante = clm.altaTercero(mapContratante, tip_benef, strRamo);
            mapContratante.clear();
            mapContratante.put("TIP_BENEF", tip_benef);
            mapContratante.put("TIP_DOCUM", drContratante[0]);
            mapContratante.put("COD_DOCUM", drContratante[1]);
            mapContratante.put("NUM_SECU", "1");
            mapContratante.put("NOM_RIESGO", request.getInsuredData().getContractingFistName().orElse("") + " " + request.getInsuredData().getContractingMiddleName().orElse("") + " " + request.getInsuredData().getContractingLastName().orElse(""));
            mapContratante.put("FEC_EFEC_RIESGO", request.getPolicyData().getPolicyEffectiveDate());
            mapContratante.put("FEC_VCTO_RIESGO", request.getPolicyData().getPolicyEndDate());
            /**'Se modifica el valor del COD_DOCUM en los datos de la póliza*/
            mapPoliza.put("TIP_DOCUM", drContratante[0]);
            mapPoliza.replace("COD_DOCUM", drContratante[1]);
        }
        /** ************************************************************************************************************
         * Se forma el arreglo de <DATOS_ASEGURADO>
         */
        Map<String, String> mapAsegurado = request.getInsuredData().dameLista(request.getInsuredData());

        /** 'Datos Fijos*/
        mapAsegurado.put("TIP_DOCUM", "RFC");
        mapAsegurado.put("RFC", rfc);
        mapAsegurado.put("COD_IDENTIFICADOR", USER_CODE + "_" + getClientHost1().replace(".", "_"));
        mapAsegurado.put("PAIS", "MEX");
        mapAsegurado.put("NOM_RIESGO", request.getInsuredData().getContractingFistName().orElse("") + " " + request.getInsuredData().getContractingMiddleName().orElse("") + " " + request.getInsuredData().getContractingLastName().orElse(""));
        mapAsegurado.put("NOM_DOMICILIO1_COM", address1);
        mapAsegurado.put("NOM_DOMICILIO2_COM", address2);
        mapAsegurado.put("NOM_DOMICILIO3_COM", address3);
        mapAsegurado.put("COD_POSTAL_COM", postalCode);
        mapAsegurado.put("COD_ESTADO_COM", stateCode);
        mapAsegurado.put("COD_PAIS_COM", countryCode);
        mapAsegurado.put("COD_LOCALIDAD", provinceCode);
        mapAsegurado.put("NUM_SECU", "1");
        mapAsegurado.put("FEC_EFEC_RIESGO", request.getPolicyData().getPolicyEffectiveDate());
        mapAsegurado.put("FEC_VCTO_RIESGO", request.getPolicyData().getPolicyEndDate());
        /**----- HRR*/
        mapAsegurado.put("COD_POSTAL_ETIQUETA", postalCode); //'HRR
        mapAsegurado.put("COD_ESTADO_ETIQUETA", stateCode); //'HRR
        mapAsegurado.put("COD_LOCALIDAD_COM", provinceCode);     //'HRR
        mapAsegurado.put("COD_LOCALIDAD_ETIQUETA", provinceCode);     //'HRR
        mapAsegurado.put("COD_OFICINA", "");     //'HRR
        mapAsegurado.put("COD_PAIS_ETIQUETA", countryCode);     //'HRR
        mapAsegurado.put("COD_USR", "AGENTETW");     //'HRR
        mapAsegurado.put("PCT_PARTICIPACION", "100");     //'HRR

        if (BANDERA_CLM.equals("True")) {
            /** Se obtiene la clave de CLM para el asegurado */
            String[] drAsegurado = clm.altaTercero(mapAsegurado, "1", strRamo);
            mapAsegurado.clear();
            mapAsegurado.put("TIP_BENEF", "2");
            mapAsegurado.put("TIP_DOCUM", drAsegurado[0]);
            mapAsegurado.put("COD_DOCUM", drAsegurado[1]);
            mapAsegurado.put("NOM_RIESGO", request.getInsuredData().getContractingFistName().orElse("") + " " + request.getInsuredData().getContractingMiddleName().orElse("") + " " + request.getInsuredData().getContractingLastName().orElse(""));
            mapAsegurado.put("FEC_EFEC_RIESGO", request.getPolicyData().getPolicyEffectiveDate());
            mapAsegurado.put("FEC_VCTO_RIESGO", request.getPolicyData().getPolicyEndDate());
            mapAsegurado.put("COD_MODALIDAD", sCOD_MODALIDAD);
            LOGGER.info("COD_MODALIDAD - mapAsegurado: "+ sCOD_MODALIDAD);
            
        }
        /** Se forma el arreglo de <BENEFICIARIOS> ********************************************************************/
        List<Map<String, String>> listaBeneficiarios = new ArrayList<>();
        String strCodDocum = "";
        String strTipDocum = "";
        //if (request.getBeneficiary() != null) {
        if(request.getBeneficiary().isPresent()) {
            for (Beneficiaries beneficiario : request.getBeneficiary().get()) {
                Map<String, String> mapBeneficiarios = new LinkedHashMap<>();
                if (BANDERA_CLM.equals("True")) {
                    /** Se crea la clave BEN del Beneficiario */
                    String[] drBeneficiario = clm.altaBeneficiario(
                            beneficiario.getPersonType(), beneficiario.getContractingFistName(),
                            beneficiario.getContractingMiddleName(), beneficiario.getContractingLastName(),
                            beneficiario.getRelationshipCode(), beneficiario.getOccupationCode(),
                            beneficiario.getDateBirth());
                    strCodDocum = drBeneficiario[0];
                    strTipDocum = drBeneficiario[1];
                    mapBeneficiarios.put("TIP_DOCUM", strTipDocum); //+"|"
                    mapBeneficiarios.put("COD_DOCUM", strCodDocum);//+"|"
                    mapBeneficiarios.put("PCT_PARTICIPACION", beneficiario.getPercentageParticipation());//+"|"
                    mapBeneficiarios.put("TIP_BENEF", beneficiario.getBeneficiaryType());//+"|"
                    mapBeneficiarios.put("NUM_SECU", beneficiario.getSequenceNumber());//+"|"
                    listaBeneficiarios.add(mapBeneficiarios);
                    LOGGER.info("ListaBeneficiarios: "+ listaBeneficiarios.size());
                } else {
                    strCodDocum = beneficiario.getContractingMiddleName().substring(0, 2) + beneficiario.getContractingLastName().substring(0, 1) + beneficiario.getContractingFistName().substring(0, 1);
                    mapBeneficiarios.put("TIP_DOCUM", "CLI"); //+"|"
                    mapBeneficiarios.put("COD_DOCUM", strCodDocum); //+"|"
                    mapBeneficiarios.put("MCA_FISICO", beneficiario.getPersonType());//+"|"
                    mapBeneficiarios.put("NOM_TERCERO", beneficiario.getContractingFistName());//+"|"
                    mapBeneficiarios.put("APE1_TERCERO", beneficiario.getContractingMiddleName());//+"|"
                    mapBeneficiarios.put("APE2_TERCERO", beneficiario.getContractingLastName());//+"|"
                    mapBeneficiarios.put("COD_OCUPACION", beneficiario.getOccupationCode());//+"|"
                    mapBeneficiarios.put("PCT_PARTICIPACION", beneficiario.getPercentageParticipation());//+"|"
                    mapBeneficiarios.put("TIP_BENEF", beneficiario.getBeneficiaryType());//+"|"
                    mapBeneficiarios.put("NUM_SECU", beneficiario.getSequenceNumber());//+"|"
                    listaBeneficiarios.add(mapBeneficiarios);
                }
            }
        }
        /** Se forma el arreglo de <DATOS_BANCARIOS> ******************************************************************/
        String tipo_pago;
        Date fchaVto;
        Map<String, String> mapDatosBancarios = new LinkedHashMap<>();
        /** Sept 19 : Se cambia la estructura del XML para Datos Bancarios */
        if (validaExist(request, DatosMinimos.D_BANCARIOS)) {
        	LOGGER.info("Existen datos bancarios: ");
            tipo_pago = request.getBankData().getPaymentType(); //Obtiene Valor de tipo pago
            BankData datosBancariosDTO = request.getBankData();

            /** Case "APE1_TERCERO"*/
            mapDatosBancarios.put("APE1_TITULAR", datosBancariosDTO.getContractingMiddleName());

            /** Case "NOM_TERCERO" */
            mapDatosBancarios.put("NOM_TITULAR", datosBancariosDTO.getContractingFistName());
            /** Case "CTA_CTE" */
            if (datosBancariosDTO.getPaymentType().equals(DatosBancarios.DEBITO) || datosBancariosDTO.getPaymentType().equals(DatosBancarios.CLABE)) {
                mapDatosBancarios.put("NUM_CUENTA", datosBancariosDTO.getAccountNumber());
            }
            /** Case "NUM_TARJETA" */
            else if (datosBancariosDTO.getPaymentType().equals(DatosBancarios.TC)) {
                mapDatosBancarios.put("NUM_CUENTA", datosBancariosDTO.getCardNumber());
            }
            /** Case "ANO_VCTO_TARJETA" */
            mapDatosBancarios.put("ANO_VCTO_TARJETA", "20" + datosBancariosDTO.getCardExpirationYear());
            /** Case "APE2_TERCERO" */
            if (datosBancariosDTO.getPaymentType().equals(DatosBancarios.DEBITO) || datosBancariosDTO.getPaymentType().equals(DatosBancarios.TC)) {
                mapDatosBancarios.put("APE2_TITULAR", datosBancariosDTO.getContractingLastName());
            }
            /** Case Else */
            mapDatosBancarios.put("COD_ENTIDAD", datosBancariosDTO.getEntityCode());
            mapDatosBancarios.put("FEC_VCTO_TARJETA", datosBancariosDTO.getCardExpirationDate());
            mapDatosBancarios.put("MES_VCTO_TARJETA", datosBancariosDTO.getCardExpirationMonth());
            mapDatosBancarios.put("EMAIL", datosBancariosDTO.getEmail());
            mapDatosBancarios.put("COD_USR", datosBancariosDTO.getUserCode());
            mapDatosBancarios.put("COD_TARJETA", datosBancariosDTO.getCardCode());
            mapDatosBancarios.put("TIP_TARJETA", datosBancariosDTO.getCardType());
            mapDatosBancarios.put("FEC_CORTE", datosBancariosDTO.getCutOffDate());
            mapDatosBancarios.put("NUM_PLAZA", String.valueOf(datosBancariosDTO.getPlaceNumber()));

            /** Select Case Tipo_pago */
            switch (tipo_pago) {
                case DatosBancarios.DEBITO: {
                    mapDatosBancarios.put("TIP_CUENTA", "A");
                }
                break;
                case DatosBancarios.TC: {
                    mapDatosBancarios.put("TIP_CUENTA", "C");
                }
                break;
                case DatosBancarios.CLABE: {
                    mapDatosBancarios.put("TIP_CUENTA", "D");
                }
                break;
            }
            mapDatosBancarios.put("COD_DOCUM", drContratante[1]);
        }
        LOGGER.info("mapDatosBancarios: " + mapDatosBancarios.size() + " valores: " + mapDatosBancarios.values());
        /** Se convierten los ArrayList a arreglos de cadenas sencillos *******************************************/

        int contract = 0;
        if(datosPolizaDTO.getContractNumber().isEmpty())
        	contract = 99999;
        else
        	contract = Integer.parseInt(datosPolizaDTO.getContractNumber());
        double riskPrime = getRiskPrime(1, 
        		Integer.parseInt(datosPolizaDTO.getBranchCode())
				,Integer.parseInt(sCOD_MODALIDAD), 
				contract,
				1, 
				"1000|" + cob1000 + "|", 
				Integer.parseInt(mapPoliza.get("ANIOS_MAX_DURACION")), 
				Integer.parseInt(request.getInsuredData().getRealAge().orElse("20"))
				);
        Map<String,String> mapCob1000 = new LinkedHashMap<String, String>();
        
        mapCob1000.put("COD_CAMPO", "PRIMA_COB_1000"); //+"|"
        mapCob1000.put("VAL_CAMPO", String.valueOf(riskPrime)); //+"|"
        mapCob1000.put("VAL_COR_CAMPO", String.valueOf(riskPrime)); //+"|"
        mapCob1000.put("TIP_NIVEL", "2"); //+"|"
        mapCob1000.put("NUM_SECU", "1"); //+"|"
        mapCob1000.put("NUM_RIESGO", "1"); //+"|"
        mapCob1000.put("COD_RAMO", strRamo);
        listaDatosVariables.add(mapCob1000);
        
        /** Se corre el proceso de emision ************************************************************************/
        String strNumeroCotizacion;
        EmisionVidaFM objEmisionVida = new EmisionVidaFM(iMifelVidaULRepository, iThirdRepository);
        strNumeroCotizacion = objEmisionVida.getEmision(mapPoliza, listaDatosVariables, listaDatosVariablesCoverage, listCoberturas, mapContratante,
                mapAsegurado, listaBeneficiarios, mapDatosBancarios, "C");
        LOGGER.info("strNumeroCotizacion: " + strNumeroCotizacion);
        /** Se obtienen los resultados de la Cotización */
        Map<String, Object> dsResultados = new HashMap<>();
        VidaULCotizacionResponse response = new VidaULCotizacionResponse();
        if (strNumeroCotizacion != null) {
            /**
             * Se crea el XML de salida
             * Dim elementoXML As Xml.XmlNodeType
             * Dim nodo As Xml.XmlNode
             * Dim attNumeroPoliza As Xml.XmlAttribute
             * dataXML.LoadXml(sHeader & "</data>" & vbCrLf & "</xml>")
             * Se crea el nodo del numero de cotizacion
             * nodo = dataXML.CreateNode(elementoXML.Element, "cotizacion", "")
             * dataXML.SelectSingleNode("//data").AppendChild(nodo)
             * Se ingresa el numero de cotizacion como atributo
             * attNumeroPoliza = dataXML.CreateNode(elementoXML.Attribute, "NUM_COTIZACION", "")
             * attNumeroPoliza.Value = strNumeroCotizacion
             * nodo.Attributes.Append(attNumeroPoliza)
             * */
        	LOGGER.info("strNumeroCotizacion - obtenResultadosCotizacion: " + strNumeroCotizacion);
            response = obtenResultadosCotizacion(strNumeroCotizacion);

        }
        return response;
    }

    private VidaULCotizacionResponse obtenResultadosCotizacion(String id){
        VidaULCotizacionResponse response = new VidaULCotizacionResponse();
        try {
            Connection conn= dataSource.getConnection();
            String mcommand = "{ call EM_K_GEN_WS.P_REGRESA_COTIZACION(?,?,?,?,?) }";

            CallableStatement call = conn.prepareCall(mcommand);
            call.setString(1, id);
            call.registerOutParameter(2, OracleTypes.CURSOR);
            call.registerOutParameter(3, OracleTypes.CURSOR);
            call.registerOutParameter(4, OracleTypes.CURSOR);
            call.registerOutParameter(5, OracleTypes.INTEGER);
            call.executeQuery();

            ResultSet listCoveragesRs = (ResultSet)call.getObject(2);
            ResultSet listPrimasRs = (ResultSet)call.getObject(3);
            ResultSet listPaymentsAmnRs = (ResultSet)call.getObject(4);
            Integer numPagos = (Integer)call.getObject(5);

            response.setSimulationQuoteId(id);
            response.setPaymentsNumber(String.valueOf(numPagos));
            if (listCoveragesRs != null) {
                List<Coverages> listCoverages = new ArrayList<>();
                while (listCoveragesRs.next()) {
                    Coverages coverages = new Coverages();
                    coverages.setCoverageId(String.valueOf(listCoveragesRs.getObject("COD_COB")));
                    String cobertura = String.valueOf(listCoveragesRs.getObject("COBERTURA"));
                    coverages.setCoverageDesc(getCoverageDesc(cobertura));
                    coverages.setSumInsuredAmn(String.valueOf(listCoveragesRs.getObject("LIMMAXRESP")));
                    coverages.setDeductible(listCoveragesRs.getObject("DEDUCIBLE")==null? null:String.valueOf(listCoveragesRs.getObject("DEDUCIBLE")));
                    coverages.setPrimeAmn(String.valueOf(listCoveragesRs.getObject("PRIMAS")));
                    listCoverages.add(coverages);
                }
                response.setListCoverages(listCoverages);
            }
            if (listPrimasRs != null) {
                while (listPrimasRs.next()) {
                    response.setTotAmn(String.valueOf(listPrimasRs.getObject("IMP_PRIMA_TOTAL")));
                    response.setTotNetAmn(String.valueOf(listPrimasRs.getObject("IMP_PRIMA")));
                    response.setSurcharges(String.valueOf(listPrimasRs.getObject("IMP_RECARGOS")));
                    response.setRights(String.valueOf(listPrimasRs.getObject("IMP_DERECHOS")));
                    response.setTaxAmount(String.valueOf(listPrimasRs.getObject("IMP_IMPUESTOS")));
                }
            }

            List<String> paymentList = new ArrayList<>();
            if (listPaymentsAmnRs != null) {
                while (listPaymentsAmnRs.next()) {
                    String importeRecibo = (String.valueOf(listPaymentsAmnRs.getObject("IMP_RECIBO")));
                    paymentList.add(importeRecibo);
                }
                response.setPayments(paymentList);
            }
            response.setCode(MifelVidaMensajesEnum.SUCCESS_OPERATION.getCodigo());
            listCoveragesRs.close();
            listPaymentsAmnRs.close();
            listPrimasRs.close();
            call.close();
            conn.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            LOGGER.error("Error en stored Obtener Calculos Cotizacion:" + throwables.getMessage());
            response.setCode(MifelVidaMensajesEnum.GENERATE_POLICY_ERROR.getCodigo());
        }
        return response;
    }
    
    private String getRealYears(String birthDay) {
    	LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate dateBirthday = LocalDate.parse(birthDay,formatter);
        Long dif = ChronoUnit.YEARS.between(dateBirthday, now);
        return dif.toString();
    }
    
    
    @Override
	public String getXmlForPrint(String ramo, String num_cotizacion, String edad, String sexo, String plazo,
			String prima_ini, String prima_adi, String periodo, String nomcliente,
			String moneda, List<Coverages> coverages, List<Coverage> coverageList, String modality, String contract) {
		Document xmlDoc = null;

		StringWriter stringWriter = new StringWriter();
		DecimalFormat df = new DecimalFormat("###,###.##");
		df.setMinimumFractionDigits(2);
		
		StringBuilder sbCoverage = new StringBuilder();
		String coverageInd = null;
		double totalPrimeRisk = 0.0d;
		Map<String, Double> coveragePrime = new HashedMap<String, Double> ();
		for(Coverage c:coverageList) {
			sbCoverage.append(c.getCoverageCode());
			sbCoverage.append("|");
			sbCoverage.append(c.getSumInsuredAmn());
			sbCoverage.append("|");
			coverageInd = new StringBuilder().append(c.getCoverageCode()).append("|").append(c.getSumInsuredAmn()).append("|").toString();
			
			if(!c.getSumInsuredAmn().equals("0")) {
				LOGGER.debug("*** {}",coverageInd);
				double prime  = getRiskPrime(1, Integer.parseInt(ramo),Integer.parseInt(modality),Integer.parseInt(contract),1,coverageInd,Integer.parseInt(plazo),Integer.parseInt(edad));
				c.setPrime(prime);
				coveragePrime.put(c.getCoverageCode(), prime);
				totalPrimeRisk += prime;
			}
			
		}
		LOGGER.debug("coverages {}",sbCoverage.toString());
		//(int company, int branch, int mod, int contract, int currency, String coverages, int duration, int age)
		
		//double totalPrimeRisk = getRiskPrime(1, Integer.parseInt(ramo),Integer.parseInt("11204"),99999,1,
		//		sbCoverage.toString(),2,Integer.parseInt(edad));
		double pFrac = totalPrimeRisk / getPeriodDivisor(Integer.parseInt(periodo));
		
		String title = getModalityName(modality, contract);

		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			xmlDoc = documentBuilderFactory.newDocumentBuilder().newDocument();

			Element xmlRoot = xmlDoc.createElement("ROOT");
			xmlDoc.appendChild(xmlRoot);

			xmlRoot.appendChild(createNode(xmlDoc, "COMPANIA", "1"));
			xmlRoot.appendChild(createNode(xmlDoc, "RAMO", ramo));
			xmlRoot.appendChild(createNode(xmlDoc, "ACTIVIDAD", "COTIZACION"));
			xmlRoot.appendChild(createNode(xmlDoc, "IDIOMA", "mx_ES"));

			Element elementDist = createNode(xmlDoc, "DISTRIBUCION", null);

			Element element = createNode(xmlDoc, "DUPLEX_MODE", "false");
			elementDist.appendChild(element);

			element = createNode(xmlDoc, "LOCAL", "true");
			elementDist.appendChild(element);

			xmlRoot.appendChild(elementDist);

			Element elementCabecera = createNode(xmlDoc, "CABECERA", null);

			element = createNode(xmlDoc, "PRODUCTO", title);
			elementCabecera.appendChild(element);

			element = createNode(xmlDoc, "NUMERO_COTIZACION", num_cotizacion);
			elementCabecera.appendChild(element);

			String dt = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

			element = createNode(xmlDoc, "FECHA_COTIZACION", dt);
			elementCabecera.appendChild(element);

			xmlRoot.appendChild(elementCabecera);

			xmlRoot.appendChild(createNode(xmlDoc, "PIE", "CV_PIE_COTIZACION_UNIT_LINKED.rtf"));

			xmlRoot.appendChild(createNode(xmlDoc, "VI_ESTADO", null));

			xmlRoot.appendChild(createNode(xmlDoc, "VI_POBLACION", null));

			dt = DateTimeFormatter.ofPattern("EEEE dd MMMM 'de' yyyy", Locale.forLanguageTag("es-Mx"))
					.format(LocalDate.now());

			xmlRoot.appendChild(createNode(xmlDoc, "VI_FECHA", dt.toUpperCase()));

			xmlRoot.appendChild(createNode(xmlDoc, "VI_DIAS_VALIDEZ", "30"));

			xmlRoot.appendChild(createNode(xmlDoc, "REG_MERCANTIL", "CV_REGISTRO_MERCANTIL_MAPFRE_MEXICO.rtf"));

			Element elementInsured = createNode(xmlDoc, "ASEGURADO", null);

			element = createNode(xmlDoc, "NOMBRE", nomcliente);
			elementInsured.appendChild(element);

			element = createNode(xmlDoc, "EDAD", edad);
			elementInsured.appendChild(element);

			if (sexo.equals("1"))
				sexo = "MASCULINO";
			else
				sexo = "FEMENINO";

			element = createNode(xmlDoc, "SEXO", sexo); // 1 Masculino
			elementInsured.appendChild(element);

			element = createNode(xmlDoc, "CORREO", null);
			elementInsured.appendChild(element);

			xmlRoot.appendChild(elementInsured);

			// Convert to double and format pfrac, prima_ini and prima_adi

			
			double dPrima_ini = Double.parseDouble(prima_ini);
			double dPrima_adi = Double.parseDouble(prima_adi);
			
			Double primainicial = dPrima_ini + pFrac;
			Double primaadicional = dPrima_adi + pFrac;

			Element elementQuote = createNode(xmlDoc, "DATOS_COTIZACION", null);

			element = createNode(xmlDoc, "MODALIDAD", title); // 11204
			elementQuote.appendChild(element);

			element = createNode(xmlDoc, "MONEDA", "PESOS"); //1 pesos, 2 dolares, 6 udis
			elementQuote.appendChild(element);

			element = createNode(xmlDoc, "PLAZO_SEGURO", plazo + " AÑOS");
			elementQuote.appendChild(element);

			element = createNode(xmlDoc, "FRECUENCIA_PRIMA_ADICIONAL", Periodicity.toString(Integer.parseInt(periodo)));
			elementQuote.appendChild(element);

			xmlRoot.appendChild(elementQuote);

			/*
			 * element = createNode(xmlDoc, "PRIMA_INICIAL", prima_ini); //format
			 * elementQuote.appendChild(element);
			 * 
			 * 
			 * 
			 * if(!prima_adi.equals("0")) { element = createNode(xmlDoc, "PRIMA_ADICIONAL",
			 * prima_adi); //1 Masculino elementQuote.appendChild(element);
			 * 
			 * element = createNode(xmlDoc, "FRECUENCIA_PRIMA_ADICIONAL", periodo);
			 * elementQuote.appendChild(element);
			 * 
			 * 
			 * }
			 */

			Element elementCoverage = createNode(xmlDoc, "COBERTURAS", null);

			element = createNode(xmlDoc, "ENCABEZADO_PRIMA", "PRIMA ANUAL"); // 11204
			elementCoverage.appendChild(element);

			Double total = 0.0d;
			for(Coverages c: coverages) {
				if(!c.getCoverageId().equals("1042")){
					Element elementSingleCoverage = createNode(xmlDoc, "COBERTURA", null);
					LOGGER.debug("id {} amount {}",c.getCoverageId(), c.getPrimeAmn());
		
					element = createNode(xmlDoc, "DESCRIPCION", c.getCoverageDesc());
					elementSingleCoverage.appendChild(element);
		
					element = createNode(xmlDoc, "SUMA_ASEGURADA", df.format(Double.parseDouble(c.getSumInsuredAmn())));
					elementSingleCoverage.appendChild(element);
		
					element = createNode(xmlDoc, "PRIMA",df.format(coveragePrime.get(c.getCoverageId())));
					elementSingleCoverage.appendChild(element);
		
					elementCoverage.appendChild(elementSingleCoverage);
					
					total =+ Double.parseDouble(c.getPrimeAmn());
				}
			}
			
			/*for(Coverage c:coverageList) {
				Element elementSingleCoverage = createNode(xmlDoc, "COBERTURA", null);
				LOGGER.debug("id {} amount {}",c.getCoverageCode(), c.getSumInsuredAmn()));
	
				element = createNode(xmlDoc, "DESCRIPCION", c.getCoverageDesc());
				elementSingleCoverage.appendChild(element);
	
				element = createNode(xmlDoc, "SUMA_ASEGURADA", df.format(Double.parseDouble(c.getSumInsuredAmn())));
				elementSingleCoverage.appendChild(element);
	
				element = createNode(xmlDoc, "PRIMA",df.format(Double.parseDouble(c.getPrimeAmn())));
				elementSingleCoverage.appendChild(element);
	
				elementCoverage.appendChild(elementSingleCoverage);
				
			}*/
			
			xmlRoot.appendChild(elementCoverage);
			
			element = createNode(xmlDoc, "TOTAL_PRIMA", "PRIMA TOTAL:    " + df.format(totalPrimeRisk)); // 11204
			xmlRoot.appendChild(element);

			xmlRoot.appendChild(createNode(xmlDoc, "VI_SUMA_MUERTE_ACCIDENTAL", null));

			// aportaciones

			Element elementContribution = createNode(xmlDoc, "APORTACIONES", null);

			Element elementIni = createNode(xmlDoc, "APORTACION_INICIAL", null);

			element = createNode(xmlDoc, "ENCABEZADO", "APORTACION INICIAL:");
			elementIni.appendChild(element);

			element = createNode(xmlDoc, "PRIMA_DE_AHORRO", df.format(Double.parseDouble(prima_ini)));
			elementIni.appendChild(element);

			element = createNode(xmlDoc, "PRIMA_DE_RIESGO", df.format(pFrac));
			elementIni.appendChild(element);

			element = createNode(xmlDoc, "PRIMA_TOTAL", df.format(primainicial));
			elementIni.appendChild(element);

			elementContribution.appendChild(elementIni);

			Element elementAdi = createNode(xmlDoc, "APORTACION_ADICIONAL", null);

			element = createNode(xmlDoc, "ENCABEZADO", "APORTACIONES ADICIONALES:");
			elementAdi.appendChild(element);

			element = createNode(xmlDoc, "PRIMA_DE_AHORRO", df.format(Double.parseDouble(prima_adi)));
			elementAdi.appendChild(element);

			element = createNode(xmlDoc, "PRIMA_DE_RIESGO", df.format(pFrac));
			elementAdi.appendChild(element);

			element = createNode(xmlDoc, "PRIMA_TOTAL",  df.format(primaadicional));
			elementAdi.appendChild(element);

			elementContribution.appendChild(elementAdi);

			xmlRoot.appendChild(elementContribution);

			// fondo

			Element elementDistri = createNode(xmlDoc, "TASAS_INTERES", null);
			
			List<FundDistribution> fundDistribution = getFundsQuote(num_cotizacion, contract);

			for (FundDistribution f : fundDistribution) {

				Element elementInv = createNode(xmlDoc, "INVERSION", null);

				element = createNode(xmlDoc, "PERFIL", f.getProfile());
				elementInv.appendChild(element);

				element = createNode(xmlDoc, "TIPO_INVERSION", f.getInvestmentType());
				elementInv.appendChild(element);

				element = createNode(xmlDoc, "PCT_INVERSION", f.getPercentage() + " %");
				elementInv.appendChild(element);

				String amount = df.format(Double.parseDouble(f.getInitialBalance()));

				element = createNode(xmlDoc, "MONTO_INICIAL", amount);
				elementInv.appendChild(element);

				elementDistri.appendChild(elementInv);
			}

			xmlRoot.appendChild(elementDistri);

			Element elementPrivacy = createNode(xmlDoc, "AVISO_PRIVACIDAD", null);

			element = createNode(xmlDoc, "CONTENIDO", "CV_ANEXO_AVISO_PRIVACIDAD.rtf");
			elementPrivacy.appendChild(element);

			xmlRoot.appendChild(elementPrivacy);

			Element elementImages = createNode(xmlDoc, "IMAGENES", null);

			element = createNode(xmlDoc, "PUBLICIDAD", "CV_PUBLICIDAD_UNIT.jpg");
			elementImages.appendChild(element);

			element = createNode(xmlDoc, "VENTA_CRUZADA", "CV_VENTA_UNIT.jpg");
			elementImages.appendChild(element);

			xmlRoot.appendChild(elementImages);

			// xmlDoc.appendChild(xmlRoot);
			// System.out.println(xmlRoot);
			// ... Resto del código ...

			// return System.Web.HttpUtility.HtmlDecode(xmlDoc.toString());
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			transformer.transform(new DOMSource(xmlDoc), new StreamResult(stringWriter));

		} catch (Exception e) { // TransformerFactoryConfigurationError, ParserConfigurationException,
								// TransformerException
			e.printStackTrace();
		}
		
		LOGGER.debug("Xml print: " + stringWriter.toString());
		
		//getFundsCatalog();
		

		return stringWriter.toString();
	}
    
    public static Element createNode(Document docXml, String tagName, String innerText) {
		Element childXml = docXml.createElement(tagName);
		if (innerText != null)
			childXml.appendChild(docXml.createTextNode(innerText));
		;
		return childXml;
    }
    
    public String getModalityName(String modality, String contract) {
    	String name = null;
    	switch(modality){
	    	case "11204":
	    		name = "CONTIGO EN TU INVERSIÓN";
	    		break;
	    	case "11205":
	    		name = "CONTIGO EN TU JUBILACIÓN";
	    		break;
	    	case "11206":
	    		name = "CONTIGO EN TU RETIRO";;
	    		break;
    	}
    	if(contract.equals("11228") || contract.equals("11229")) {
    		name = "UNIT LINKED INVERSIÓN";
    	}
    	return name;
    }
    
    public double getRiskPrime(int company, int branch, int mod, int contract, int currency, String coverages, int duration, int age) {
    	LOGGER.debug("company: {} branch: {} mod: {} contract: {} currency: {} duration: {} age: {} coverages {} ",company, branch, mod, contract, currency, duration, age, coverages);
		double riskPrime = 0.0d;
    	try {
            Connection conn= dataSource.getConnection();
            String mcommand = "{ call tron2000.ev_k_productos_vida_mmx.p_ul_ind_prima_riesgo(?,?,?,?,?,?,?,?,?,?) }";

            CallableStatement call = conn.prepareCall(mcommand);
            call.setInt(1, company);
            call.setInt(2, branch);
            call.setInt(3, mod);
            call.setInt(4, contract);
            call.setInt(5, currency);
            call.setString(6, coverages);
            call.setInt(7, duration);
            call.setInt(8, age);
            
            
            call.setDate(9, java.sql.Date.valueOf(LocalDate.now()) );
            
            call.registerOutParameter(10, OracleTypes.CURSOR);

            call.executeQuery();

            ResultSet rs = (ResultSet) call.getObject(10);
            
            if (rs != null) {
                while (rs.next()) {
                    String codCob = String.valueOf(rs.getObject("COD_COB"));
                    String strRiskPrime = String.valueOf(rs.getObject("PRIMA"));
                    LOGGER.debug(" Cob {}  prima  {}",codCob, riskPrime);
                    if(codCob.equals("9999")) {
                    	riskPrime = Double.parseDouble(strRiskPrime);
                    	break;
                    }
                }
            }
            
            call.close();
            conn.close();

        } catch (Exception throwables) {        	
            throwables.printStackTrace();
            LOGGER.debug("Error en stored poliza:: {}", throwables.getMessage());
        }
    	LOGGER.debug("Risk prime: {}", riskPrime);
    	return riskPrime;				
	}
    
    
    public int getPeriodDivisor(int period) {
    	int divisor = 1;
    	switch(period) {
    		case 2: //semestral
    			divisor = 2;
    			break;
    		case 3: //trimestral
    			divisor = 4;
    			break;
    		case 4: //mensual
    			divisor = 12;
    			break;
    		case 10: //anual
    			divisor = 1;
    			break;
    	}
    	return divisor;
    }
    
    public void getFundsCatalog() {
    	try {
	    	Connection conn= dataSource.getConnection();
	    	String command = "{ ? = call TRON2000.EM_K_GEN_WS_MMX.GETLOV(?,?,?,?,?,?) }";
	    	CallableStatement call = conn.prepareCall(command);
	    	call.registerOutParameter(1, OracleTypes.CURSOR);
	    	call.setInt(2, 1);
	    	call.setInt(3, 112);
	    	call.setString(4,"");
	    	call.setString(5,"TA899039");
	    	call.setInt(6,3);
	    	call.setString(7, "[cod_cia = 1][cod_ramo = 112][dvcod_modalidad = 11204][num_contrato = 99999][dvtip_perfil_inv  = 'MOD'][cod_mon = 1]");
	    	call.execute();
	    	
	    	ResultSet rs = (ResultSet) call.getObject(1);
	    	
	    	if (rs != null) {
                while (rs.next()) {
                	String name = rs.getString(1);
                	String id = rs.getString(2);
                	LOGGER.debug("name: {}  id: {}",name,id);
                }
	    	}
	    	
	    	
    	} catch (Exception throwables) {        	
            throwables.printStackTrace();
            LOGGER.debug("Error en stored poliza:: {}", throwables.getMessage());
        }
    }
    
    public List<FundDistribution> getFundsQuote(String quoteId, String contract) {
    	List<FundDistribution> fundDistribution = new ArrayList<>();
    	try {
    		
    		ObjectMapper mapper = new ObjectMapper();
    		
    		//LOGGER.debug(FundConstants.funds);
    		Map<String, Fund> fundsMap = mapper.readValue(FundConstants.funds, new TypeReference<Map<String,Fund>>() {});
    		
    		//Fund fund = fundsMap.get("0104");
    		//LOGGER.debug(fund.getProfile());
    		
	    	Connection conn= dataSource.getConnection();
	    	String command = "{ ? = call TRON2000.EV_K_112_UNIT_MMX.GET_DISTRIBUCION_FONDOS(?) }";
	    	CallableStatement call = conn.prepareCall(command);
	    	call.registerOutParameter(1, OracleTypes.CURSOR);
	    	call.setString(2, quoteId);
	    	call.execute();
	    	
	    	ResultSet rs = (ResultSet) call.getObject(1);
	    	
	    	if (rs != null) {
                while (rs.next()) {
                	String id = rs.getString(1);
                	String percentage = rs.getString(2);
                	String initialBalance = rs.getString(3);
                	Fund fund = fundsMap.get(id);
                	String profile = null;
                	String strFund = null;
                	
                	LOGGER.debug("Codigo:: {}  Porcentaje:: {}  MontoIni:: {}",id,percentage,initialBalance);
                	
                	if(fund != null) {
	                	profile = fund.getProfile();
	                	if(contract.equals("11228") || contract.equals("11229"))
	                		strFund = "MIFEL";
	                	else
	                		strFund = fund.getFund();
                	}
                	FundDistribution fd = new FundDistribution();
                	fd.setProfile(profile);
                	fd.setInvestmentType(strFund);
                	fd.setPercentage(percentage);
                	fd.setInitialBalance(initialBalance);
                	fundDistribution.add(fd);
                			
                }
	    	}
	    	
	    	
    	} catch (Exception throwables) {        	
            throwables.printStackTrace();
            LOGGER.debug("Error en stored poliza:: {}", throwables.getMessage());
        }
    	return fundDistribution;
    }
    
    @Override
    public Map<String,String> getVariableData(List<VariableData> variableData) {
    	Map<String, String> mapData = new HashMap<String, String>();
    	for (VariableData datosV : variableData) {

            for (FieldRisk campo : datosV.getFields()) {
                //if(campo.getFieldCode().equals("ANIOS_DURACION_POLIZA")) {
                //	years = (String) campo.getFieldValue();
                //}
            	mapData.put(campo.getFieldCode(), campo.getFieldValue());
            }
    	}
    	return mapData;
    }
    
    public String getCoverageDesc(String coverage) {
    	switch(coverage) {
    		case "BASICA":
    			return "FALLECIMIENTO";
    		case "FALLECIMIENTO ACCIDENTAL":
    			return "MUERTE ACCIDENTAL";
    		case "BIT":
    			return "EXENCIÓN DEL COSTO DE COBERTURA (BIT)";
    		case "BIPA":
    			return "INVALIDEZ TOTAL Y PERMANENTE (BIPA)";
    		case "EG":
    			return "ENFERMEDADES GRAVES";
    		default:
    			return coverage;
    	}
    }

	@Override
	public Map<String, String> getVariableDataCoverages(List<VariableDataCoverage> variableData) {
		Map<String, String> mapData = new HashMap<String, String>();
    	for (VariableDataCoverage datosV : variableData) {

            for (FieldRisk campo : datosV.getFields()) {
                //if(campo.getFieldCode().equals("ANIOS_DURACION_POLIZA")) {
                //	years = (String) campo.getFieldValue();
                //}
            	mapData.put(campo.getFieldCode(), campo.getFieldValue());
            }
    	}
    	return mapData;
	}

}
