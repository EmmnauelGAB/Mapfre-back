package com.mapfre.mifel.vida.model.response;

public class PaymentResponse {

    private String authorizationNumber; 
	private String referenceNumber; 
	private String descriptionError;
	
	public PaymentResponse() {
		
	}

	public String getAuthorizationNumber() {
		return authorizationNumber;
	}

	public void setAuthorizationNumber(String authorizationNumber) {
		this.authorizationNumber = authorizationNumber;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getDescriptionError() {
		return descriptionError;
	}

	public void setDescriptionError(String descriptionError) {
		this.descriptionError = descriptionError;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authorizationNumber == null) ? 0 : authorizationNumber.hashCode());
		result = prime * result + ((descriptionError == null) ? 0 : descriptionError.hashCode());
		result = prime * result + ((referenceNumber == null) ? 0 : referenceNumber.hashCode());
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
		PaymentResponse other = (PaymentResponse) obj;
		if (authorizationNumber == null) {
			if (other.authorizationNumber != null)
				return false;
		} else if (!authorizationNumber.equals(other.authorizationNumber))
			return false;
		if (descriptionError == null) {
			if (other.descriptionError != null)
				return false;
		} else if (!descriptionError.equals(other.descriptionError))
			return false;
		if (referenceNumber == null) {
			if (other.referenceNumber != null)
				return false;
		} else if (!referenceNumber.equals(other.referenceNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PaymentResponse [authorizationNumber=" + authorizationNumber + ", referenceNumber=" + referenceNumber
				+ ", descriptionError=" + descriptionError + "]";
	}
	
	
}
