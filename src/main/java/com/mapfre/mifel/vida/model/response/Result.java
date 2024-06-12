package com.mapfre.mifel.vida.model.response;

import com.sun.xml.txw2.annotation.XmlElement;

public class Result {
	
	private Boolean result;
	
	public Result()
	{
		
	}

	public Result(Boolean result) {
		super();
		this.result = result;
	}

	public Boolean getResult() {
		return result;
	}
	
	public void setResult(Boolean result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "Result [result=" + result + "]";
	}
	
	

}
