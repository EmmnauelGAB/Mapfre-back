package com.mapfre.mifel.vida.helper;

import com.mapfre.mifel.vida.utils.DateFormatUtil;

import java.util.Map;
import static com.mapfre.mifel.vida.utils.VidaULContants.*;

public class P2000031 {
    private String XML_CTE;
    private String XML_VAR;

    public P2000031(){
        DateFormatUtil dateUtil = new DateFormatUtil();
        StringBuffer cadenaXmlP2000031 = new StringBuffer();
        //cadenaXmlP2000031.append("<TABLE NAME=\"P2000031\"><ROWSET><ROW num=\"1\">");
        cadenaXmlP2000031.append("<COD_CIA>1</COD_CIA>");
        cadenaXmlP2000031.append("<NUM_POLIZA></NUM_POLIZA>");
        cadenaXmlP2000031.append("<NUM_SPTO>0</NUM_SPTO>");
        cadenaXmlP2000031.append("<NUM_APLI>0</NUM_APLI>");
        cadenaXmlP2000031.append("<NUM_SPTO_APLI>0</NUM_SPTO_APLI>");
        cadenaXmlP2000031.append("<NUM_RIESGO>1</NUM_RIESGO>");
        cadenaXmlP2000031.append("<TIP_SPTO>XX</TIP_SPTO>");
//        cadenaXmlP2000031.append("<COD_MODALIDAD>99999</COD_MODALIDAD>");
        cadenaXmlP2000031.append("<MCA_BAJA_RIESGO>N</MCA_BAJA_RIESGO>");
        cadenaXmlP2000031.append("<MCA_VIGENTE>S</MCA_VIGENTE>");
        //cadenaXmlP2000031.append("</ROW></ROWSET></TABLE>");
        this.XML_CTE = cadenaXmlP2000031.toString();
        //return cadenaXmlP2000031;
    }

    public void setDatos(Map<String, String> datos){
        StringBuffer xml = new StringBuffer();
        String[] arrCAMPOS;
        arrCAMPOS= TablasVida.DATOS_P2000031;
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
