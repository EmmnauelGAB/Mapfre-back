package com.mapfre.mifel.vida.utils;

public class VidaULContants {
	
public static class DataDummy {
		
		public static final String INFO_PDF_QUOT = "{\r\n"
				+ "  \"datosCotizacion\": {\r\n"
				+ "    \"numCotizacion\": \"2311200001052\",\r\n"
				+ "    \"fechaCotizacion\": \"08/11/2023\"\r\n"
				+ "  },\r\n"
				+ "  \"datosSolicitante\": {\r\n"
				+ "    \"nombre\": \"John Doe\",\r\n"
				+ "    \"edad\": 30,\r\n"
				+ "    \"email\": \"pxadn2906@gmail.com\",\r\n"
				+ "    \"sexo\": \"Masculino\"\r\n"
				+ "  },\r\n"
				+ "  \"informacionDelProducto\": {\r\n"
				+ "    \"modalidad\": \"Seguro de Vida\",\r\n"
				+ "    \"moneda\": \"PESOS\",\r\n"
				+ "    \"plazoSeguro\": \"10 años\",\r\n"
				+ "    \"peridiocidadPrimaAdicional\": \"Mensual\"\r\n"
				+ "  },\r\n"
				+ "  \"detalleCoberturas\": {\r\n"
				+ "    \"coberturas\": [\r\n"
				+ "      {\r\n"
				+ "        \"coberturaDescripcion\": \"Cobertura de Vida\",\r\n"
				+ "        \"sumaAsegurada\": 100000,\r\n"
				+ "        \"primaAnual\": 500.88\r\n"
				+ "      },\r\n"
				+ "      {\r\n"
				+ "        \"coberturaDescripcion\": \"Cobertura de Salud\",\r\n"
				+ "        \"sumaAsegurada\": 50000,\r\n"
				+ "        \"primaAnual\": 200.34\r\n"
				+ "      }\r\n"
				+ "    ],\r\n"
				+ "    \"primaTotal\": 700\r\n"
				+ "  },\r\n"
				+ "  \"aportaciones\": {\r\n"
				+ "    \"aportacionesIniciales\": \r\n"
				+ "      {\r\n"
				+ "        \"primaAhorro\": 2300.44,\r\n"
				+ "        \"primaRiesgo\": 300.11\r\n"
				+ "      }\r\n"
				+ "    ,\r\n"
				+ "    \"aportacionesAdicionales\": \r\n"
				+ "      {\r\n"
				+ "        \"primaAhorro\": 5000.99,\r\n"
				+ "        \"primaRiesgo\": 100.98\r\n"
				+ "      }\r\n"
				+ "    ,\r\n"
				+ "    \"primaTotalInicial\": 5000,\r\n"
				+ "    \"primaTotalAdicional\": 150\r\n"
				+ "  },\r\n"
				+ "  \"distribucionFondos\": [\r\n"
				+ "    {\r\n"
				+ "      \"perfil\": \"Conservador\",\r\n"
				+ "      \"tipoInversion\": \"Rentabilidad Fija\",\r\n"
				+ "      \"porcentaje\": 60,\r\n"
				+ "      \"montoInicial\": 1000\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "      \"perfil\": \"Conservador 2\",\r\n"
				+ "      \"tipoInversion\": \"ActiMed\",\r\n"
				+ "      \"porcentaje\": 30,\r\n"
				+ "      \"montoInicial\": 858.44\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "      \"perfil\": \"Decidido1\",\r\n"
				+ "      \"tipoInversion\": \"MAYA\",\r\n"
				+ "      \"porcentaje\": 30,\r\n"
				+ "      \"montoInicial\": 858.31\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "      \"perfil\": \"Moderado\",\r\n"
				+ "      \"tipoInversion\": \"Renta Variable\",\r\n"
				+ "      \"porcentaje\": 40,\r\n"
				+ "      \"montoInicial\": 500\r\n"
				+ "    }\r\n"
				+ "  ]\r\n"
				+ "}\r\n"
				+ "";;

	}
	
public static class HeadersResponse {
		
		public static final Integer ESTATUS_EXITOSO = 1;
		public static final Integer ESTATUS_FALLIDO = -1;
		
		public static final String MENSAJE_EXITOSO = "Peticion procesada de manera exitosamente";
		public static final String MENSAJE_FALLIDO = "Hubo un problema al procesar la petición";
		
		public static final String CODIGO_CORTO_EXITO = "SUCCESS";
		public static final String CODIGO_CORTO_FALLIDO = "FAIL";

	}
	
	public static class InformacionEmailEnvio {
		
		public static final Integer DOCUMENTO_COTIZACION = 1;
		public static final Integer DOCUMENTO_EMISION = 2;
		
		public static final String ASUNTO_COTIZACION = "MAPFRE COTIZACIÓN UNIT LINKED";
		public static final String CUERPO_MENSAJE_COTIZACION = "Apreciable cliente, anexo encontrará la cotización referente al producto MAPFRE Unit Linked.\r\n\nLe recordamos que para la contratación del seguro es necesario acudir a cualquier sucursal de banco.";
		public static final String SUBJECT_COTIZACION = "MAPFRE COTIZACIÓN UNIT LINKED";
		
		public static final String ASUNTO_EMISION = "MAPFRE EMISIÓN UNIT LINKED";
		public static final String CUERPO_MENSAJE_EMISION = "Apreciable cliente, anexo encontrará la emisión referente al producto MAPFRE Unit Linked.\r\n\\nLe recordamos que para la contratación del seguro es necesario acudir a cualquier sucursal de banco.";
		public static final String SUBJECT_EMISION = "MAPFRE EMISIÓN UNIT LINKED";
	}

    public static class DatosMinimos {
        public static final String D_POLIZA = "POLICY_DATA";
        public static final String D_VARIABLES = "VARIABLE_DATA";
        public static final String D_CAMPO = "FIELDS";
        public static final String D_COBERTURA = "COVERAGE";
        public static final String D_CONTRATANTE = "CONTRACTOR_DATA";
        public static final String D_ASEGURADO = "INSURED_DATA";
        public static final String D_BENEFICIARIO = "BENEFICIARY_DATA";
        public static final String D_BANCARIOS = "BANK_DATA";

        /**
         * <Datos minimos para el nodo de DATOS_POLIZA de la cadena de Emision de Vida>
         */
        public static final String[] DATOS_POLIZA_EMI_VIDA = {"COD_RAMO", "COD_AGT", "FEC_EFEC_POLIZA", "FEC_VCTO_POLIZA", "COD_DOCUM", "TIP_GESTOR", "COD_FRACC_PAGO"};
        /**
         * <Datos minimos para el nodo de DATOS_VARIABLES de la cadena de Emision de Vida/>
         */
        public static final String[] DATOS_VARIABLES_EMI_VIDA = {"NUM_RIESGO", "TIP_NIVEL"};
        /**
         * <Datos minimos para el nodo de CAMPO de la cadena de Emision de Vida/>
         */
        public static final String[] CAMPO_VARIABLE_EMI_VIDA = {"COD_CAMPO", "VAL_CAMPO"};
        /**
         * <Datos minimos para el nodo de COBERTURA de la cadena de Emision Vida/>
         */

        public static final String[] COBERTURA_VIDA = {"COD_COB", "SUM_ASEG"};
        /**
         * <Datos minimos para el nodo de DATOS_CONTRATANTE de la cadena de Emision de Vida/>
         */
        public static final String[] CAMPO_CONTRATANTE_EMI_VIDA = {
                "COD_DOCUM", "NOM_TERCERO", "APE1_TERCERO", "APE2_TERCERO", "NOM_DOMICILIO1", "NOM_DOMICILIO2", "NOM_DOMICILIO3", "COD_POSTAL", "COD_ESTADO", "COD_PROV",
                "COD_PAIS", "TLF_NUMERO", 
                //"FEC_NACIMIENTO", 
                "MCA_SEXO", "COD_EST_CIVIL", "MCA_FISICO", "TIP_BENEF", "COD_OCUPACION", "EMAIL"};
        /**
         * <Datos minimos para el nodo de DATOS_BENEFICIARIO de la cadena de Emision de Vida/>
         */
        public static final String[] CAMPO_BENEFICIARIO_EMI_VIDA = {"MCA_FISICO", "NOM_TERCERO", "APE1_TERCERO", "APE2_TERCERO", "COD_OCUPACION", "PCT_PARTICIPACION",
                "TIP_BENEF", "NUM_SECU"};
        /**
         * <Datos minimos para el nodo de DATOS_BANCARIOS Debito de la cadena de Emision de Vida/>
         */
        public static final String[] CAMPO_DB_DEBITO_VIDA = {"NOM_TERCERO", "APE1_TERCERO", "APE2_TERCERO", "TIP_TARJETA", "COD_ENTIDAD", "CTA_CTE", "EMAIL", "MES_VCTO_TARJETA",
                "ANO_VCTO_TARJETA"};
        /**
         * <Datos minimos para el nodo de DATOS_BANCARIOS CL_BE de la cadena de Emision de Vida/>
         */
        public static final String[] CAMPO_DB_CLABE_VIDA = {"NOM_TERCERO", "APE1_TERCERO", "COD_ENTIDAD", "CTA_CTE", "EMAIL"};
        /**
         * <Datos minimos para el nodo de DATOS_BANCARIOS TC de la cadena de Emision de Vida/>
         */
        public static final String[] CAMPO_DB_TC_VIDA = {"NOM_TERCERO", "APE1_TERCERO", "APE2_TERCERO", "TIP_TARJETA", "COD_ENTIDAD", "NUM_TARJETA", "EMAIL", "MES_VCTO_TARJETA",
                "ANO_VCTO_TARJETA"};

        /**
         * <Datos minimos para el nodo de DATOS_ASEGURADO de la cadena de Emision de Vida/>
         */
        public static final String[] CAMPO_ASEGURADO_EMI_VIDA = {
                "COD_DOCUM", "NOM_TERCERO", "APE1_TERCERO", "APE2_TERCERO", "NOM_DOMICILIO1", "NOM_DOMICILIO2", "NOM_DOMICILIO3", "COD_POSTAL", "COD_ESTADO", "COD_PROV",
                "COD_PAIS", "TLF_NUMERO", "FEC_NACIMIENTO", "MCA_SEXO", "COD_EST_CIVIL", "MCA_FISICO", "COD_OCUPACION", "TIP_BENEF", "OBS_ASEGURADO"};


        public static final String  P_PROCEDURES_SECTOR= "SECTOR";
        public static final String  P_PROCEDURES_MAN= "MANAGEMENT";
        public static final String  P_PROCEDURES_AGENT_= "AGENT";
        public static final String  P_PROCEDURES_FOLIO= "FOLIO";
        public static final String  P_PROCEDURES_DATE = "DATE";
    }

    public static class Errors {
        public static final int ERROR_400 = 400;
        public static final String ERROR_NODO = "No existe el nodo: ";
        public static final String ERROR_ENTRY = "El formato de entrada no es correcto";
        public static final String ERROR_CAMPO = "No existe el campo: ";
        public static final String ERROR_CAMPO_G = "Existe error en algun campo: ";

    }
    public static class DatosBancarios {
        /**
         * Datos bancarios "TIPO_PAGO"
         */
        public static final String DEBITO = "DEBITO";
        public static final String CLABE = "CLABE";
        public static final String TC = "TC";

    }
    /**
     * Bandera para activar Cliente MAPFRE
     */
    public static final String BANDERA_CLM = "True";
    /**
     * Codigo de Usuario CLM
     */
    public static final String USER_CODE = "APPSEGA";

    public static class AltaTercero {
        public static final Integer ORIGIN_CODE = 1;
        public static final String USER_CODE_EMISION = "GMAC0100";

    }
    public static class Dates {
        public static final String DATE_FORMAT_1 = "dd/MM/yyyy";
        public static final String DATE_FORMAT_2 = "MM/dd/yyyy";
        public static final String DATE_FORMAT_3 = "dd-MM-yyyy";
        public static final String DATE_FORMAT_4 = "MM-dd-yyyy";
        public static final String DATE_FORMAT_5 = "dd,MM,yyyy";
        public static final String DATE_FORMAT_6 = "MM,dd,yyyy";
        public static final String DATE_FORMAT_7 = "yyyy-MM-dd";
        public static final String DATE_FORMAT_8 = "dd-MM-yy";
        public static final String DATE_FORMAT_9 = "yyyy-MM-dd HH:mm:ss";
        public static final String DATE_FORMAT_10 = "yyyy-MM-dd HH:mm:ss.SSS";
        public static final String DATE_FORMAT_11 = "yyyy-MM-dd HH:mm:ss.SSSZ";
        public static final String DATE_FORMAT_12 = "dd/MM/yyyy HH:mm:ss";
        public static final String DATE_FORMAT_13 = "yyyy,MM,dd";

    }


    public static class TablasVida {
        /**
         * <!--TABLAS VIDA-->
         */
        public static final String[] DATOS_P2000031 = {"NOM_RIESGO", "FEC_EFEC_RIESGO", "FEC_VCTO_RIESGO", "COD_MODALIDAD"};
//        public static final String[] DATOS_P2000030 = {
//                "TIP_DURACION", "MCA_PRORRATA", "COD_FRACC_PAGO", "COD_USR", "COD_RAMO", "COD_AGT", "FEC_EFEC_POLIZA", "FEC_VCTO_POLIZA",
//                "FEC_EFEC_SPTO", "FEC_VCTO_SPTO", "TIP_DOCUM", "COD_DOCUM", "TIP_GESTOR", "COD_GESTOR", "MCA_IMPRESION", "COD_CUADRO_COM",
//                "NUM_CONTRATO", "NUM_POLIZA_GRUPO", "NUM_PRESUPUESTO", "NUM_POLIZA_CLIENTE"};
//        public static final String[] DATOS_P2000030 = {
//        		"COD_RAMO", "COD_AGT", "PCT_AGT", "TIP_GESTOR","COD_GESTOR", "FEC_EFEC_POLIZA", "FEC_VCTO_POLIZA",
//        		"FEC_EFEC_SPTO", "FEC_VCTO_SPTO", "TIP_DOCUM", "COD_DOCUM", "COD_CUADRO_COM", "MCA_IMPRESION", 
//        		"ANIOS_MAX_DURACION", "COD_ENVIO", "COD_FRACC_PAGO", "COD_MON", "TXT_MOTIVO_SPTO",
//        		"COD_USR", "NUM_POLIZA_GRUPO", "NUM_CONTRATO", "TIP_REGULARIZA", "PCT_REGULARIZA", 
//        		"COD_AGT2", "PCT_AGT2", "COD_AGT3", "PCT_AGT3", "COD_AGT4", "PCT_AGT4",	"NUM_CONTRATO", 
//        		"NUM_POLIZA_GRUPO", "TIP_DURACION", "COD_CIA", "COD_SECTOR", "NUM_POLIZA",
//        		"NUM_SPTO", "NUM_APLI", "NUM_SPTO_APLI", "FEC_EMISION", "FEC_EMISION_SPTO", "NUM_RIESGOS",
//        		"CANT_RENOVACIONES", "NUM_RENOVACIONES", "TIP_COASEGURO", "NUM_SECU_GRUPO", "TIP_SPTO",
//        		"MCA_REGULARIZA", "DURACION_PAGO_PRIMA", "MCA_TOMADORES_ALT", "MCA_REASEGURO_MANUAL",
//        		"MCA_PRORRATA", "MCA_PRIMA_MANUAL", "MCA_PROVISIONAL", "MCA_POLIZA_ANULADA",
//        		"MCA_SPTO_ANULADO", "MCA_SPTO_TMP", "MCA_DATOS_MINIMOS", "MCA_EXCLUSIVO", "FEC_VALIDEZ", 
//        		"FEC_ACTU", "MCA_REASEGURO_MARCO", "TIP_POLIZA_TR", "COD_NIVEL1", "COD_NIVEL2",
//        		"COD_NIVEL3", "COD_NIVEL3_CAPTURA", "COD_COMPENSACION"};
        public static final String[] DATOS_P2000030 = {
        		"COD_RAMO", "COD_AGT", "PCT_AGT", "TIP_GESTOR","COD_GESTOR", "FEC_EFEC_POLIZA", "FEC_VCTO_POLIZA",
        		"FEC_EFEC_SPTO", "FEC_VCTO_SPTO", "TIP_DOCUM", "COD_DOCUM", "COD_CUADRO_COM", "MCA_IMPRESION", 
        		"ANIOS_MAX_DURACION", "COD_ENVIO", "COD_FRACC_PAGO", "COD_MON", "TXT_MOTIVO_SPTO",
        		"COD_USR", "NUM_POLIZA_GRUPO", "NUM_CONTRATO", "TIP_REGULARIZA", "PCT_REGULARIZA", 
        		"COD_AGT2", "PCT_AGT2", "COD_AGT3", "PCT_AGT3", "COD_AGT4", "PCT_AGT4",	"NUM_CONTRATO", 
        		"NUM_POLIZA_GRUPO", "TIP_DURACION"};
        
        public static final String[] DATOS_P2000020 = {"COD_CAMPO", "VAL_CAMPO", "VAL_COR_CAMPO", "NUM_RIESGO", "TIP_NIVEL", "COD_RAMO", "NUM_SECU"};
        
        public static final String[] DATOS_P2000025 = {"COD_CAMPO", "VAL_CAMPO", "TXT_CAMPO", "COD_LISTA", "NUM_RIESGO", "NUM_PERIODO", 
        												"NUM_OCURRENCIA", "NUM_SECU"};
        
        public static final String[] DATOS_P2000040 = {"COD_COB", "SUMA_ASEG", "SUMA_ASEG_SPTO", "NUM_SECU", "COD_RAMO", "MCA_BAJA_COB"};
        public static final String[] DATOS_P2000060 = {"TIP_BENEF", "NUM_SECU", "TIP_DOCUM", "COD_DOCUM", "PCT_PARTICIPACION"};
        public static final String[] DATOS_P1001331 = {
                "TIP_DOCUM", "COD_DOCUM", "MCA_FISICO", "APE1_TERCERO", "APE2_TERCERO", "NOM_TERCERO", "COD_OCUPACION", "NOM_DOMICILIO1",
                "NOM_DOMICILIO2", "NOM_DOMICILIO3", "COD_POSTAL", "COD_ESTADO", "COD_PROV", "COD_PAIS", "TLF_NUMERO", "FEC_NACIMIENTO",
                "MCA_SEXO", "COD_EST_CIVIL", "NOM_DOMICILIO1_COM", "NOM_DOMICILIO2_COM", "NOM_DOMICILIO3_COM", "COD_POSTAL_COM",
                "COD_ESTADO_COM", "COD_PAIS_COM", "COD_LOCALIDAD", "COD_POSTAL_ETIQUETA", "COD_ESTADO_ETIQUETA", "COD_LOCALIDAD_COM",
                "COD_LOCALIDAD_ETIQUETA", "COD_OFICINA", "COD_PAIS_ETIQUETA", "COD_USR,TXT_EMAIL"};
        public static final String[] DATOS_P2990706_MMX = {
                "APE1_TERCERO", "APE2_TERCERO", "NOM_TERCERO", "COD_ENTIDAD", "COD_OFICINA", "COD_TARJETA", "TIP_TARJETA",
                "CTA_CTE", "FEC_CORTE", "FEC_VCTO_TARJETA", "NUM_TARJETA", "TIP_GESTOR", "COD_USR", "NUM_PLAZA"};

        public static final String[] DATOS_P2300061_MMX= null;
    }
    public static class Actinver {
        public static final String[] DATOS_W2990708_MMX = {
                "NOM_TITULAR", "APE1_TITULAR", "APE2_TITULAR", "COD_ENTIDAD", "EMAIL", "TIP_TARJETA", "TIP_CUENTA",
                "NUM_CUENTA", "MES_VCTO_TARJETA", "ANO_VCTO_TARJETA", "COD_DOCUM"};

        public static final String[] DATOS_W2990708_MMX_CLABE = {
                "NOM_TITULAR", "APE1_TITULAR", "COD_ENTIDAD", "EMAIL", "TIP_CUENTA", "NUM_CUENTA", "COD_DOCUM"};

        public static final String[] DATOS_W2990709_MMX = {"EMAIL", "COD_DOCUM"};

    }
    
    public static class Impresion {
        // Nueva constante agregada
        public static final String CODIGO_IMPRESION_CARATULA_EXTREM = "P";
    }
    
}
