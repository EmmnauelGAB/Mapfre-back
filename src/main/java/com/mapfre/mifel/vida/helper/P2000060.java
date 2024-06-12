package com.mapfre.mifel.vida.helper;

import java.util.Map;
import static com.mapfre.mifel.vida.utils.VidaULContants.*;

public class P2000060 {
    private String XML_CTE;
    private String XML_VAR;

    public P2000060() {
        StringBuffer cadenaXmlP2000060 = new StringBuffer();
        //cadenaXmlP2000060.append("<TABLE NAME=\"P2000060\"><ROWSET><ROW num=\"1\">");
        cadenaXmlP2000060.append("<FEC_NACIMIENTO>01/01/1996</FEC_NACIMIENTO>");
        cadenaXmlP2000060.append("<NUM_POLIZA></NUM_POLIZA>");
        cadenaXmlP2000060.append("<COD_CIA>1</COD_CIA>");
        cadenaXmlP2000060.append("<NUM_SPTO>0</NUM_SPTO>");
        cadenaXmlP2000060.append("<NUM_APLI>0</NUM_APLI>");
        cadenaXmlP2000060.append("<NUM_SPTO_APLI>0</NUM_SPTO_APLI>");
        cadenaXmlP2000060.append("<NUM_RIESGO>1</NUM_RIESGO>");
        cadenaXmlP2000060.append("<MCA_PRINCIPAL>N</MCA_PRINCIPAL>");
        cadenaXmlP2000060.append("<MCA_CALCULO>N</MCA_CALCULO>");
        cadenaXmlP2000060.append("<MCA_BAJA>N</MCA_BAJA>");
        cadenaXmlP2000060.append("<MCA_VIGENTE>S</MCA_VIGENTE>");
        //cadenaXmlP2000060.append("</ROW></ROWSET></TABLE>");
        this.XML_CTE = cadenaXmlP2000060.toString();
        //return cadenaXmlP2000060;
    }

    public void setDatos(Map<String, String> datos) {
        StringBuffer xml = new StringBuffer();
        String[] arrCAMPOS;
        arrCAMPOS = TablasVida.DATOS_P2000060;
        for (int i = 0; i < arrCAMPOS.length; i++) {
            xml.append(setDatosMap(arrCAMPOS[i], datos.get(arrCAMPOS[i])==null?"":datos.get(arrCAMPOS[i])));
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

    public StringBuffer getXml(int row_num) {
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
