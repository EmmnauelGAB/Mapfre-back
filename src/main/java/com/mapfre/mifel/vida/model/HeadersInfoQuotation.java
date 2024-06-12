package com.mapfre.mifel.vida.model;

public class HeadersInfoQuotation {
	
	private String numCotizacion;
	private String fechaCotizacion;
	
	public HeadersInfoQuotation() {
		
	}
	
	public HeadersInfoQuotation(String numCotizacion, String fechaCotizacion) {
		super();
		this.numCotizacion = numCotizacion;
		this.fechaCotizacion = fechaCotizacion;
	}
	public String getNumCotizacion() {
		return numCotizacion;
	}
	public void setNumCotizacion(String numCotizacion) {
		this.numCotizacion = numCotizacion;
	}
	public String getFechaCotizacion() {
		return fechaCotizacion;
	}
	public void setFechaCotizacion(String fechaCotizacion) {
		this.fechaCotizacion = fechaCotizacion;
	}
	
	
	

}
