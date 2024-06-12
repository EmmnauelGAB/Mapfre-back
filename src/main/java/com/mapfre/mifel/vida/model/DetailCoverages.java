package com.mapfre.mifel.vida.model;

import java.util.List;

public class DetailCoverages {
	
	private List<CoveragesQuot> coberturas;
	private Double primaTotal;
	
	public DetailCoverages() {
		
	}
	
	public DetailCoverages(List<CoveragesQuot> coberturas, Double primaTotal) {
		super();
		this.coberturas = coberturas;
		this.primaTotal = primaTotal;
	}
	public List<CoveragesQuot> getCoberturas() {
		return coberturas;
	}
	public void setCoberturas(List<CoveragesQuot> coberturas) {
		this.coberturas = coberturas;
	}
	public Double getPrimaTotal() {
		return primaTotal;
	}
	public void setPrimaTotal(Double primaTotal) {
		this.primaTotal = primaTotal;
	}
	
	

}
