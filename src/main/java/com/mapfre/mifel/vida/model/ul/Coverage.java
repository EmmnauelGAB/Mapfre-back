package com.mapfre.mifel.vida.model.ul;

public class Coverage {
    private String  coverageCode;
    private String 	sumInsuredAmn;
    private double prime;

    public String getCoverageCode() {
        return coverageCode;
    }

    public void setCoverageCode(String coverageCode) {
        this.coverageCode = coverageCode;
    }

    public String getSumInsuredAmn() {
        return sumInsuredAmn;
    }

    public void setSumInsuredAmn(String sumInsuredAmn) {
        this.sumInsuredAmn = sumInsuredAmn;
    }

	public double getPrime() {
		return prime;
	}

	public void setPrime(double prime) {
		this.prime = prime;
	}
    
    
}
