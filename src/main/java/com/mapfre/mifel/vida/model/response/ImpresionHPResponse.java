package com.mapfre.mifel.vida.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mapfre.mifel.vida.model.DocumentData;
import com.mapfre.mifel.vida.model.DocumentMetadata;

import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import io.swagger.annotations.ApiModel;

//@ApiModel(description = "Document info downloaded")
@JsonInclude(Include.NON_NULL)
public class ImpresionHPResponse {

	private String pError;
	private String pXml;

	public String getpError() {
		return pError;
	}

	public void setpError(String pError) {
		this.pError = pError;
	}

	public String getpXml() {
		return pXml;
	}

	public void setpXml(String pXml) {
		this.pXml = pXml;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this, new MultilineRecursiveToStringStyle()).toString();
	}

}
