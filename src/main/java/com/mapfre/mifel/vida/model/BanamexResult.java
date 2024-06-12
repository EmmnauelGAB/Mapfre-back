package com.mapfre.mifel.vida.model;

public class BanamexResult {
	
	String code;
	String authorizationNumber;
	String errorDesc;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAuthorizationNumber() {
		return authorizationNumber;
	}
	public void setAuthorizationNumber(String authorizationNumber) {
		this.authorizationNumber = authorizationNumber;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authorizationNumber == null) ? 0 : authorizationNumber.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((errorDesc == null) ? 0 : errorDesc.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BanamexResult other = (BanamexResult) obj;
		if (authorizationNumber == null) {
			if (other.authorizationNumber != null)
				return false;
		} else if (!authorizationNumber.equals(other.authorizationNumber))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (errorDesc == null) {
			if (other.errorDesc != null)
				return false;
		} else if (!errorDesc.equals(other.errorDesc))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "BanamexResult [code=" + code + ", authorizationNumber=" + authorizationNumber + ", errorDesc="
				+ errorDesc + "]";
	}
	

}
