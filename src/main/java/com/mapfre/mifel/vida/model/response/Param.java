package com.mapfre.mifel.vida.model.response;

import com.sun.xml.txw2.annotation.XmlElement;


public class Param {
	
	private Boolean result;
	
	public Param()
	{
		
	}

	public Boolean getResult() {
		return result;
	}
	@XmlElement
	public void setResult(Boolean result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "Param [result=" + result + "]";
	}
	
	

}
