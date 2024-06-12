package com.mapfre.mifel.vida.model.request;

import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Data to download the policy")
public class ImpresionRequest {

	@ApiModelProperty(name = "Policy number", required = true, example = "1082100000099")
	@NotBlank
	private String numPoliza;

	@ApiModelProperty(name = "spto", example = "0")
	@NotBlank
	private String numSpto;

	@ApiModelProperty(name = "Company code", example = "1")
	@NotBlank
	private String codCia;

	public String getNumPoliza() {
		return numPoliza;
	}

	public void setNumPoliza(String numPoliza) {
		this.numPoliza = numPoliza;
	}

	public String getNumSpto() {
		return numSpto;
	}

	public void setNumSpto(String numSpto) {
		this.numSpto = numSpto;
	}

	public String getCodCia() {
		return codCia;
	}

	public void setCodCia(String codCia) {
		this.codCia = codCia;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this, new MultilineRecursiveToStringStyle()).toString();
	}

}
