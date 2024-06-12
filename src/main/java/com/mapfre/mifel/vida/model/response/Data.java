package com.mapfre.mifel.vida.model.response;

import com.sun.xml.txw2.annotation.XmlElement;


public class Data {

	private Cobro cobro;
	
	public Data() {
		
	}
	
	

	public Data(Cobro cobro) {
		super();
		this.cobro = cobro;
	}



	public Cobro getCobro() {
		return cobro;
	}
	@XmlElement
	public void setCobro(Cobro cobro) {
		this.cobro = cobro;
	}



	@Override
	public String toString() {
		return "Data [cobro=" + cobro + "]";
	}
	
	
	
}
