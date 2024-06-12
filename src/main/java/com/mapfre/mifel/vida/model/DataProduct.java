package com.mapfre.mifel.vida.model;

public class DataProduct {
	
	private String modalidad;
	private String moneda;
	private String plazoSeguro;
	private String peridiocidadPrimaAdicional;
	
	public DataProduct() {
		
	}
	
	public DataProduct(String modalidad, String moneda, String plazoSeguro, String peridiocidadPrimaAdicional) {
		super();
		this.modalidad = modalidad;
		this.moneda = moneda;
		this.plazoSeguro = plazoSeguro;
		this.peridiocidadPrimaAdicional = peridiocidadPrimaAdicional;
	}
	public String getModalidad() {
		return modalidad;
	}
	public void setModalidad(String modalidad) {
		this.modalidad = modalidad;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public String getPlazoSeguro() {
		return plazoSeguro;
	}
	public void setPlazoSeguro(String plazoSeguro) {
		this.plazoSeguro = plazoSeguro;
	}
	public String getPeridiocidadPrimaAdicional() {
		return peridiocidadPrimaAdicional;
	}
	public void setPeridiocidadPrimaAdicional(String peridiocidadPrimaAdicional) {
		this.peridiocidadPrimaAdicional = peridiocidadPrimaAdicional;
	}
	
	

}
