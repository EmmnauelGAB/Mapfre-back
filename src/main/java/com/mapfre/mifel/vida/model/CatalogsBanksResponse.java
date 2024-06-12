package com.mapfre.mifel.vida.model;




public class CatalogsBanksResponse {
	
	private String parameterId;
	private String parameterName;
	private String parameterDesc;
	
	public CatalogsBanksResponse()
	{
		
	}

	public CatalogsBanksResponse(String parameterId, String parameterName, String parameterDesc) {
		super();
		this.parameterId = parameterId;
		this.parameterName = parameterName;
		this.parameterDesc = parameterDesc;
	}

	public String getParameterId() {
		return parameterId;
	}

	public void setParameterId(String parameterId) {
		this.parameterId = parameterId;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterDesc() {
		return parameterDesc;
	}

	public void setParameterDesc(String parameterDesc) {
		this.parameterDesc = parameterDesc;
	}

	@Override
	public String toString() {
		return "CatalogsBanksResponse [parameterId=" + parameterId + ", parameterName=" + parameterName
				+ ", parameterDesc=" + parameterDesc + "]";
	}
	
	

}
