package com.mapfre.mifel.vida.model.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
public class PaymentCobranzaResponse {
	
	private Param param;
	
	private Data data;
	
	public PaymentCobranzaResponse()
	{
		
	}

	public PaymentCobranzaResponse(Param param, Data data) {
		super();
		this.param = param;
		this.data = data;
	}

	public Param getParam() {
		return param;
	}
	
	@XmlElement
	public void setParam(Param param) {
		this.param = param;
	}

	public Data getData() {
		return data;
	}

	@XmlElement
	public void setData(Data data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "PaymentCobranzaResponse [param=" + param + ", data=" + data + "]";
	}
	
	
	

}
