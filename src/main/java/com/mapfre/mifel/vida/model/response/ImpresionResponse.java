package com.mapfre.mifel.vida.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mapfre.mifel.vida.model.DocumentData;
import com.mapfre.mifel.vida.model.DocumentMetadata;

import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Document info downloaded")
@JsonInclude(Include.NON_NULL)
public class ImpresionResponse {

	private String documentId;

	private DocumentData documentData = new DocumentData();

	private DocumentMetadata documentMetadata = new DocumentMetadata();

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public DocumentData getDocumentData() {
		return documentData;
	}

	public void setDocumentData(DocumentData documentData) {
		this.documentData = documentData;
	}

	public DocumentMetadata getDocumentMetadata() {
		return documentMetadata;
	}

	public void setDocumentMetadata(DocumentMetadata documentMetadata) {
		this.documentMetadata = documentMetadata;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this, new MultilineRecursiveToStringStyle()).toString();
	}

}
