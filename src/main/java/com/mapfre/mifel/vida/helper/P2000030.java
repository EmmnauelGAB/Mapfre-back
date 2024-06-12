package com.mapfre.mifel.vida.helper;

import com.mapfre.mifel.vida.utils.DateFormatUtil;

import java.util.Map;

import static com.mapfre.mifel.vida.utils.VidaULContants.*;

public class P2000030 {
    private String XML_CTE;
    private String XML_VAR;

    public P2000030(String cveAgente){

        DateFormatUtil dateUtil = new DateFormatUtil();
        StringBuffer cadenaXmlP2000030 = new StringBuffer();

        //cadenaXmlP2000030.append("<TABLE NAME=\"P2000030\"><ROWSET><ROW num=\"1\">");
        cadenaXmlP2000030.append("<COD_CIA>1</COD_CIA>");
        cadenaXmlP2000030.append("<COD_SECTOR>1</COD_SECTOR>"); //HRR
        cadenaXmlP2000030.append("<NUM_POLIZA></NUM_POLIZA>");
        cadenaXmlP2000030.append("<NUM_SPTO>0</NUM_SPTO>");
        cadenaXmlP2000030.append("<NUM_APLI>0</NUM_APLI>");
        cadenaXmlP2000030.append("<NUM_SPTO_APLI>0</NUM_SPTO_APLI>");
        cadenaXmlP2000030.append("<FEC_EMISION>" + dateUtil.getFechaActual(Dates.DATE_FORMAT_1) + "</FEC_EMISION>");
        cadenaXmlP2000030.append("<FEC_EMISION_SPTO>" + dateUtil.getFechaActual(Dates.DATE_FORMAT_1) + "</FEC_EMISION_SPTO>");
        cadenaXmlP2000030.append("<NUM_RIESGOS>1</NUM_RIESGOS>");
//        cadenaXmlP2000030.append("<COD_MON>1</COD_MON>");
        cadenaXmlP2000030.append("<CANT_RENOVACIONES>0</CANT_RENOVACIONES>");
        cadenaXmlP2000030.append("<NUM_RENOVACIONES>0</NUM_RENOVACIONES>");
        cadenaXmlP2000030.append("<TIP_COASEGURO>0</TIP_COASEGURO>");
        cadenaXmlP2000030.append("<NUM_SECU_GRUPO></NUM_SECU_GRUPO>");
        cadenaXmlP2000030.append("<TIP_SPTO>XX</TIP_SPTO>");
        cadenaXmlP2000030.append("<MCA_REGULARIZA>S</MCA_REGULARIZA>");
        cadenaXmlP2000030.append("<DURACION_PAGO_PRIMA>1</DURACION_PAGO_PRIMA>");
        cadenaXmlP2000030.append("<MCA_TOMADORES_ALT>N</MCA_TOMADORES_ALT>");
        cadenaXmlP2000030.append("<MCA_REASEGURO_MANUAL>N</MCA_REASEGURO_MANUAL>");
        cadenaXmlP2000030.append("<MCA_PRORRATA>S</MCA_PRORRATA>");
        cadenaXmlP2000030.append("<MCA_PRIMA_MANUAL>N</MCA_PRIMA_MANUAL>");
        cadenaXmlP2000030.append("<MCA_PROVISIONAL>N</MCA_PROVISIONAL>");
        cadenaXmlP2000030.append("<MCA_POLIZA_ANULADA>N</MCA_POLIZA_ANULADA>");
        cadenaXmlP2000030.append("<MCA_SPTO_ANULADO>N</MCA_SPTO_ANULADO>");
        cadenaXmlP2000030.append("<MCA_SPTO_TMP>N</MCA_SPTO_TMP>");
        cadenaXmlP2000030.append("<MCA_DATOS_MINIMOS>N</MCA_DATOS_MINIMOS>");
        cadenaXmlP2000030.append("<MCA_EXCLUSIVO>N</MCA_EXCLUSIVO>");
        cadenaXmlP2000030.append("<FEC_VALIDEZ>" + dateUtil.getFechaActual(Dates.DATE_FORMAT_1) + "</FEC_VALIDEZ>");
        cadenaXmlP2000030.append("<FEC_ACTU>" + dateUtil.getFechaActual(Dates.DATE_FORMAT_1) + "</FEC_ACTU>");
        cadenaXmlP2000030.append("<MCA_REASEGURO_MARCO>N</MCA_REASEGURO_MARCO>");
        cadenaXmlP2000030.append("<TIP_POLIZA_TR>F</TIP_POLIZA_TR>");
        cadenaXmlP2000030.append("<COD_NIVEL1></COD_NIVEL1>");
        cadenaXmlP2000030.append("<COD_NIVEL2></COD_NIVEL2>");
        cadenaXmlP2000030.append("<COD_NIVEL3></COD_NIVEL3>");
        cadenaXmlP2000030.append("<COD_NIVEL3_CAPTURA>4920</COD_NIVEL3_CAPTURA>"); //HRR
        cadenaXmlP2000030.append("<COD_COMPENSACION>1</COD_COMPENSACION>");
//        cadenaXmlP2000030.append("<TXT_MOTIVO_SPTO></TXT_MOTIVO_SPTO>");
//        cadenaXmlP2000030.append("<PCT_AGT2>0</PCT_AGT2>");
//        cadenaXmlP2000030.append("<PCT_AGT3>0</PCT_AGT3>");
//        cadenaXmlP2000030.append("<PCT_AGT4>0</PCT_AGT4>");
//        cadenaXmlP2000030.append("<PCT_REGULARIZA>0</PCT_REGULARIZA>"); //HRR
//        cadenaXmlP2000030.append("<PCT_AGT>100</PCT_AGT>");
//        cadenaXmlP2000030.append("<TIP_REGULARIZA>0</TIP_REGULARIZA>");
//        cadenaXmlP2000030.append("<ANIOS_MAX_DURACION>1</ANIOS_MAX_DURACION>");
//        cadenaXmlP2000030.append("<COD_ENVIO>1</COD_ENVIO>");
        //cadenaXmlP2000030.append("</ROW></ROWSET></TABLE>");

        this.XML_CTE= cadenaXmlP2000030.toString();
        //return cadenaXmlP2000030;
    }

    public void setDatos(Map<String, String> datos){
        StringBuffer xml = new StringBuffer();
        String[] arrCAMPOS;
        arrCAMPOS= TablasVida.DATOS_P2000030;
        for(int i= 0; i < arrCAMPOS.length; i++){
            xml.append(setDatosMap(arrCAMPOS[i],datos.get(arrCAMPOS[i])==null?"":datos.get(arrCAMPOS[i])));
        }
        /*for (Map.Entry<String, String> entry : datos.entrySet()) {
            xml.append(setDatosMap(entry.getKey(), entry.getValue()));
        }*/
        this.XML_VAR = xml.toString();
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

    public StringBuffer getXml(int row_num){
        StringBuffer objSB = new StringBuffer();
        objSB.append("<ROW num=\"");
        objSB.append(row_num);
        objSB.append("\">");
        objSB.append(this.XML_VAR);
        objSB.append(this.XML_CTE);
        objSB.append("</ROW>");
        return objSB;
    }
}
