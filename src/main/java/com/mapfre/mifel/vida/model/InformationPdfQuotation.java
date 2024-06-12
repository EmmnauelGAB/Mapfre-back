package com.mapfre.mifel.vida.model;

import java.util.List;

public class InformationPdfQuotation {
	
	private HeadersInfoQuotation datosCotizacion;
	private DataClient datosSolicitante;
	private DataProduct informacionDelProducto;
	private DetailCoverages detalleCoberturas;
	private Contributions aportaciones;
	private List<DistributionFunds> distribucionFondos;
	
	public HeadersInfoQuotation getDatosCotizacion() {
		return datosCotizacion;
	}
	public void setDatosCotizacion(HeadersInfoQuotation datosCotizacion) {
		this.datosCotizacion = datosCotizacion;
	}
	public DataClient getDatosSolicitante() {
		return datosSolicitante;
	}
	public void setDatosSolicitante(DataClient datosSolicitante) {
		this.datosSolicitante = datosSolicitante;
	}
	public DataProduct getInformacionDelProducto() {
		return informacionDelProducto;
	}
	public void setInformacionDelProducto(DataProduct informacionDelProducto) {
		this.informacionDelProducto = informacionDelProducto;
	}
	public DetailCoverages getDetalleCoberturas() {
		return detalleCoberturas;
	}
	public void setDetalleCoberturas(DetailCoverages detalleCoberturas) {
		this.detalleCoberturas = detalleCoberturas;
	}
	public Contributions getAportaciones() {
		return aportaciones;
	}
	public void setAportaciones(Contributions aportaciones) {
		this.aportaciones = aportaciones;
	}
	public List<DistributionFunds> getDistribucionFondos() {
		return distribucionFondos;
	}
	public void setDistribucionFondos(List<DistributionFunds> distribucionFondos) {
		this.distribucionFondos = distribucionFondos;
	}
	
	

}
