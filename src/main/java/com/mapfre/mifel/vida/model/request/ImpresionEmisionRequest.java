package com.mapfre.mifel.vida.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Data to download the policy")
public class ImpresionEmisionRequest {
	

	@ApiModelProperty(name = "Policy number", required = true, example = "1082100000099")
	@NotBlank
	private String numPoliza;

	@ApiModelProperty(name = "Number endoso", example = "0")
	@NotBlank
	private String numEndoso;

	@ApiModelProperty(name = "Name Business", example = "nissan")
	@NotBlank
	private String negocio;
	
	@ApiModelProperty(name = "Number nmi", example = "1")
	@NotBlank
	private String nmi;
	
	@ApiModelProperty(name = "Email", example = "correoElectronico@gmail.com")
	@NotBlank
	@Email(message="Put a email valid")
	private String emailDestino;



	public String getNumPoliza() {
		return numPoliza;
	}



	public void setNumPoliza(String numPoliza) {
		this.numPoliza = numPoliza;
	}



	public String getNumEndoso() {
		return numEndoso;
	}



	public void setNumEndoso(String numEndoso) {
		this.numEndoso = numEndoso;
	}





	public String getNegocio() {
		return negocio;
	}



	public void setNegocio(String negocio) {
		this.negocio = negocio;
	}



	public String getNmi() {
		return nmi;
	}



	public void setNmi(String nmi) {
		this.nmi = nmi;
	}



	public String getEmailDestino() {
		return emailDestino;
	}



	public void setEmailDestino(String emailDestino) {
		this.emailDestino = emailDestino;
	}
	
	

}
