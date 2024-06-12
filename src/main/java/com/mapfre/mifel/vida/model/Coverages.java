package com.mapfre.mifel.vida.model;

public class Coverages {

    private String coverageId;
    private String sumInsuredAmn;
    private String coverageDesc;
    private String deductible;
    private String primeAmn;

    public String getCoverageId() {
        return coverageId;
    }

    public void setCoverageId(String coverageId) {
        this.coverageId = coverageId;
    }

    public String getSumInsuredAmn() {
        return sumInsuredAmn;
    }

    public void setSumInsuredAmn(String sumInsuredAmn) {
        this.sumInsuredAmn = sumInsuredAmn;
    }

    public String getCoverageDesc() {
        return coverageDesc;
    }

    public void setCoverageDesc(String coverageDesc) {
        this.coverageDesc = coverageDesc;
    }

    public String getDeductible() {
        return deductible;
    }

    public void setDeductible(String deductible) {
        this.deductible = deductible;
    }

    public String getPrimeAmn() {
        return primeAmn;
    }

    public void setPrimeAmn(String primeAmn) {
        this.primeAmn = primeAmn;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((coverageDesc == null) ? 0 : coverageDesc.hashCode());
        result = prime * result + ((coverageId == null) ? 0 : coverageId.hashCode());
        result = prime * result + ((primeAmn == null) ? 0 : primeAmn.hashCode());
        result = prime * result + ((sumInsuredAmn == null) ? 0 : sumInsuredAmn.hashCode());
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
        Coverages other = (Coverages) obj;
        if (coverageDesc == null) {
            if (other.coverageDesc != null)
                return false;
        } else if (!coverageDesc.equals(other.coverageDesc))
            return false;
        if (coverageId == null) {
            if (other.coverageId != null)
                return false;
        } else if (!coverageId.equals(other.coverageId))
            return false;
        if (primeAmn == null) {
            if (other.primeAmn != null)
                return false;
        } else if (!primeAmn.equals(other.primeAmn))
            return false;
        if (sumInsuredAmn == null) {
            if (other.sumInsuredAmn != null)
                return false;
        } else if (!sumInsuredAmn.equals(other.sumInsuredAmn))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Coverage [coverageId=" + coverageId + ", sumInsuredAmn=" + sumInsuredAmn + ", coverageDesc="
                + coverageDesc + ", primeAmn=" + primeAmn + "]";
    }
}
