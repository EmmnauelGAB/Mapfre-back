package com.mapfre.mifel.vida.model.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DocumentType {
	POLIZA_CARATULA("P"),
	CONSTANCIA_PAGO("CP"),
	CONTRATO_PPR("PPR"),
	SOLICITUD("ST"),
	FORMATO_FATCA("CF"),
	FORMATO_CRS("CRS"),
	//FORMATO_ARTICULO_492("KYC"),
	FOLLETO_INFORMATIVO("FI");
	
	private String documentType;
	
	DocumentType(String documentType){
		this.documentType = documentType;
	}

	public String getDocumentType() {
		return documentType;
	}
	
	@Override
	public String toString() {
		return documentType;
	}
	
	@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
	public static DocumentType fromText(String documentType) {
		for(DocumentType r: DocumentType.values()) {
			if(r.getDocumentType().equals(documentType)) {
				return r;
			}
		}
		throw new IllegalArgumentException();
	}
}
