package com.mapfre.mifel.vida.model.request;

import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.mapfre.mifel.vida.model.enumeration.DocumentType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Data to download the policy")
public class ImpresionHPRequest {

	@ApiModelProperty(name = "Policy number", required = true, example = "1122300000309")
	@NotBlank
	private String Policy;

	@ApiModelProperty(name = "Document type", required = true, example = "P")
	private DocumentType documentType;

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this, new MultilineRecursiveToStringStyle()).toString();
	}

}
