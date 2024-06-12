package com.mapfre.mifel.vida.service;

import com.mapfre.mifel.vida.model.response.EmisionResponse;
import com.mapfre.mifel.vida.model.response.MifelResponse;

public interface ClientImpresionService {

    /**
     * Call the service that generates the documentId and Downloads the file using the documentID
     * 
     * @param poliza
     * @param spto
     * @param codCia
     * @return file in byteArray
     */
    byte[] downloadFile(String poliza, String spto, String codCia);

    /**
     * Call the service that generates the documentId
     * 
     * @param poliza
     * @param spto
     * @param codCia
     * @return documentId
     */
    String getImpresionId(String poliza, String spto, String codCia);
    
    /**
     * Call service for get emision (poliza) document
     * @param poliza
     * @param strNegocio
     * @param strEndoso
     * @param nmi
     * @return
     */
    public MifelResponse<EmisionResponse> getImpresionEmision(String poliza, String strNegocio, String strEndoso, String nmi, String emailDestiny);

    /**
     * Downloads the file using the documentID
     * 
     * @param impresionId
     * @return file in byteArray
     */
    byte[] downloadFile(String impresionId);

}
