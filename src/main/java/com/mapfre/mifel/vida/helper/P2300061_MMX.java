package com.mapfre.mifel.vida.helper;

import com.mapfre.mifel.vida.utils.HostUtil;

import java.util.Map;
import static com.mapfre.mifel.vida.utils.VidaULContants.*;

public class P2300061_MMX {
    private String XML_CTE;
    private String XML_VAR;

    public P2300061_MMX(){
        StringBuffer cadenaXmlP2300061_MMX = new StringBuffer();
        cadenaXmlP2300061_MMX.append("<COD_CIA>1</COD_CIA>");
        cadenaXmlP2300061_MMX.append("<NUM_POLIZA></NUM_POLIZA>");
        cadenaXmlP2300061_MMX.append("<NUM_SPTO>0</NUM_SPTO>");
        cadenaXmlP2300061_MMX.append("<NUM_APLI>0</NUM_APLI>");
        cadenaXmlP2300061_MMX.append("<NUM_SPTO_APLI>0</NUM_SPTO_APLI>");
        cadenaXmlP2300061_MMX.append("<NUM_RIESGO>1</NUM_RIESGO>");
        //return cadenaXmlP2300061_MMX;
        this.XML_CTE = cadenaXmlP2300061_MMX.toString();
    }

    public void setDatos(Map<String, String> datos){
        StringBuffer xml = new StringBuffer();
        String[] arrCAMPOS;
        arrCAMPOS= TablasVida.DATOS_P2300061_MMX;
        for(int i= 0; i < arrCAMPOS.length; i++){
            xml.append(setDatosMap(arrCAMPOS[i],datos.get(arrCAMPOS[i])==null?"":datos.get(arrCAMPOS[i])));
        }
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

    /**
     * Retorna el hostname o address del servidor local
     * @return
     */
    private String getClientHost1() {
        HostUtil hostUtil= new HostUtil();
        return hostUtil.getAddress();
    }
}
