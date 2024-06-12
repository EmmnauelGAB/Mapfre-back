package com.mapfre.mifel.vida.helper;

import com.mapfre.mifel.vida.utils.DateFormatUtil;
import com.mapfre.mifel.vida.utils.HostUtil;

import java.util.Map;
import static com.mapfre.mifel.vida.utils.VidaULContants.*;

public class W2990708 {

    private String XML_CTE;
    private String XML_VAR;
    private String tipoPago;

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public W2990708(String cveAgente, String tipo_Pago) {
        DateFormatUtil dateUtil = new DateFormatUtil();
        StringBuffer cadenaXmlW2990708 = new StringBuffer();
        this.tipoPago = tipo_Pago;

        cadenaXmlW2990708.append("<COD_CIA>1</COD_CIA>");
        cadenaXmlW2990708.append("<MCA_INH>N</MCA_INH>");
        cadenaXmlW2990708.append("<DES_OBSERVACIONES></DES_OBSERVACIONES>");
        cadenaXmlW2990708.append("<TIP_MOVIMIENTO>A</TIP_MOVIMIENTO>");
        cadenaXmlW2990708.append("<TIP_DOCUM-1-2>CLM</TIP_DOCUM-1-2>");
        cadenaXmlW2990708.append("<TIP_DOCUM>CLM</TIP_DOCUM>");
        cadenaXmlW2990708.append("<DIA_APLICACION_COBRO>" + dateUtil.getDiaSiguiente("dd", 1) + "</DIA_APLICACION_COBRO>");
        cadenaXmlW2990708.append("<FEC_TRATAMIENTO>" + dateUtil.getFechaActual(Dates.DATE_FORMAT_1) + "</FEC_TRATAMIENTO>");
        cadenaXmlW2990708.append("<FEC_VALIDEZ>" + dateUtil.getFechaActual(Dates.DATE_FORMAT_1) + "</FEC_VALIDEZ>");
        cadenaXmlW2990708.append("<COD_IDENTIFICADOR>" + USER_CODE + "_" + getClientHost1().replace(".", "_") + "</COD_IDENTIFICADOR>");
        cadenaXmlW2990708.append("<NUM_CONSEC_CUENTA>1</NUM_CONSEC_CUENTA>");
        this.XML_CTE = cadenaXmlW2990708.toString();
        //return cadenaXmlW2990708;

    }

    public void setDatos(Map<String, String> datos) {
        StringBuffer xml = new StringBuffer();
        String[] arrCAMPOS;
        if (getTipoPago().equals("D")) {
            arrCAMPOS = Actinver.DATOS_W2990708_MMX_CLABE;
        } else {
            arrCAMPOS = Actinver.DATOS_W2990708_MMX;
        }
        for (int i = 0; i < arrCAMPOS.length; i++) {
            xml.append(setDatosMap(arrCAMPOS[i], datos.get(arrCAMPOS[i])==null?"":datos.get(arrCAMPOS[i])));
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

    /**
     * Retorna el hostname o address del servidor local
     *
     * @return
     */
    private String getClientHost1() {
        HostUtil hostUtil = new HostUtil();
        return hostUtil.getAddress();
    }
}
