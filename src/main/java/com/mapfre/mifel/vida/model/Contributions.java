package com.mapfre.mifel.vida.model;


public class Contributions {
	
	private InitalContribution aportacionesIniciales;
	private AdditionalContribution aportacionesAdicionales;
	private Double primaTotalInicial;
	private Double primaTotalAdicional;
	
	public InitalContribution getAportacionesIniciales() {
		return aportacionesIniciales;
	}
	public void setAportacionesIniciales(InitalContribution aportacionesIniciales) {
		this.aportacionesIniciales = aportacionesIniciales;
	}
	public AdditionalContribution getAportacionesAdicionales() {
		return aportacionesAdicionales;
	}
	public void setAportacionesAdicionales(AdditionalContribution aportacionesAdicionales) {
		this.aportacionesAdicionales = aportacionesAdicionales;
	}
	public Double getPrimaTotalInicial() {
		return primaTotalInicial;
	}
	public void setPrimaTotalInicial(Double primaTotalInicial) {
		this.primaTotalInicial = primaTotalInicial;
	}
	public Double getPrimaTotalAdicional() {
		return primaTotalAdicional;
	}
	public void setPrimaTotalAdicional(Double primaTotalAdicional) {
		this.primaTotalAdicional = primaTotalAdicional;
	}
	

	

}
