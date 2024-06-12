package com.mapfre.mifel.vida.service;

import com.mapfre.mifel.vida.model.enumeration.DocumentType;

public interface ClientImpresionServiceHP {

    byte[] getPdfHpExstream(String policy, DocumentType documentType);

}
