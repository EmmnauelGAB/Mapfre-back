package com.mapfre.mifel.vida.helper;

import com.mapfre.mifel.vida.utils.DateFormatUtil;
import com.mapfre.mifel.vida.utils.HostUtil;

import java.util.Map;

import static com.mapfre.mifel.vida.utils.VidaULContants.*;

public class P1001331 {

    private String XML_CTE;
    private String XML_VAR;

    public P1001331(){
        DateFormatUtil dateUtil = new DateFormatUtil();
        StringBuffer objSB = new StringBuffer();

        objSB.append("<COD_CIA>1</COD_CIA>");
        objSB.append("<TIP_MVTO_BATCH>3</TIP_MVTO_BATCH>");
        objSB.append("<COD_ACT_TERCERO>1</COD_ACT_TERCERO>");
        objSB.append("<COD_IDIOMA>ES</COD_IDIOMA>");
        objSB.append("<TXT_AUX1>NR</TXT_AUX1>");
        objSB.append("<FEC_TRATAMIENTO>" + dateUtil.getFechaActual(Dates.DATE_FORMAT_1) + "</FEC_TRATAMIENTO>");
        objSB.append("<FEC_ACTU>" + dateUtil.getFechaActual(Dates.DATE_FORMAT_1) + "</FEC_ACTU>");
        //objSB.append("<COD_CALIDAD>1</COD_CALIDAD>") 'HRR
        //objSB.append("<TIP_ETIQUETA>1</TIP_ETIQUETA>") 'HRR
        //objSB.append("<TLF_PAIS></TLF_PAIS>") 'HRR
        //objSB.append("<TLF_ZONA></TLF_ZONA>") 'HRR

        this.XML_CTE = objSB.toString();
    }
    public void setDatos(Map<String, String> datos){
        StringBuffer xml = new StringBuffer();
        String[] arrCAMPOS;
        arrCAMPOS= TablasVida.DATOS_P1001331;
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
