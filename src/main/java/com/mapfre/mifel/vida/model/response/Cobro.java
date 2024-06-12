package com.mapfre.mifel.vida.model.response;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


public class Cobro {
	
	private String numAutorizacion;
	private String numReferencia;
	
	public Cobro()
	{
		
	}
	
	

	public Cobro(String numAutorizacion, String numReferencia) {
		super();
		this.numAutorizacion = numAutorizacion;
		this.numReferencia = numReferencia;
	}



	public String getNumAutorizacion() {
		return numAutorizacion;
	}

	@XmlAttribute(name = "NUM_AUTORIZACION")
	public void setNumAutorizacion(String numAutorizacion) {
		this.numAutorizacion = numAutorizacion;
	}

	public String getNumReferencia() {
		return numReferencia;
	}
	
	@XmlAttribute(name = "NUM_REFERENCIA")
	public void setNumReferencia(String numReferencia) {
		this.numReferencia = numReferencia;
	}



	@Override
	public String toString() {
		return "Cobro [numAutorizacion=" + numAutorizacion + ", numReferencia=" + numReferencia + "]";
	}
	
	

}
