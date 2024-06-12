package com.mapfre.mifel.vida.model.response;

import com.mapfre.mifel.vida.model.Coverages;
import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel(description = "Respuesta del servicio de generar cotizacion ")
public class VidaULCotizacionResponse {
    private String simulationQuoteId;
    private String totAmn;
    private String totNetAmn;
    private String surcharges;
    private String rights;
    private String taxAmount;
    private Integer code;

    private List<String> payments;
    private String paymentsNumber;
    private List<Coverages> listCoverages;
    
    private String pdfBase64;

    public String getSimulationQuoteId() {
        return simulationQuoteId;
    }

    public void setSimulationQuoteId(String simulationQuoteId) {
        this.simulationQuoteId = simulationQuoteId;
    }

    public String getTotAmn() {
        return totAmn;
    }

    public void setTotAmn(String totAmn) {
        this.totAmn = totAmn;
    }

    public String getTotNetAmn() {
        return totNetAmn;
    }

    public void setTotNetAmn(String totNetAmn) {
        this.totNetAmn = totNetAmn;
    }

    public String getSurcharges() {
        return surcharges;
    }

    public void setSurcharges(String surcharges) {
        this.surcharges = surcharges;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<String> getPayments() {
        return payments;
    }

    public void setPayments(List<String> payments) {
        this.payments = payments;
    }

    public String getPaymentsNumber() {
        return paymentsNumber;
    }

    public void setPaymentsNumber(String paymentsNumber) {
        this.paymentsNumber = paymentsNumber;
    }

    public List<Coverages> getListCoverages() {
        return listCoverages;
    }

    public void setListCoverages(List<Coverages> listCoverages) {
        this.listCoverages = listCoverages;
    }
    
    

    public String getPdfBase64() {
		return pdfBase64;
	}

	public void setPdfBase64(String pdfBase64) {
		this.pdfBase64 = pdfBase64;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((rights == null) ? 0 : rights.hashCode());
        result = prime * result + ((listCoverages == null) ? 0 : listCoverages.hashCode());
        result = prime * result + ((payments == null) ? 0 : payments.hashCode());
        result = prime * result + ((paymentsNumber == null) ? 0 : paymentsNumber.hashCode());
        result = prime * result + ((surcharges == null) ? 0 : surcharges.hashCode());
        result = prime * result + ((simulationQuoteId == null) ? 0 : simulationQuoteId.hashCode());
        result = prime * result + ((taxAmount == null) ? 0 : taxAmount.hashCode());
        result = prime * result + ((totAmn == null) ? 0 : totAmn.hashCode());
        result = prime * result + ((totNetAmn == null) ? 0 : totNetAmn.hashCode());
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
        VidaULCotizacionResponse other = (VidaULCotizacionResponse) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (rights == null) {
            if (other.rights != null)
                return false;
        } else if (!rights.equals(other.rights))
            return false;
        if (listCoverages == null) {
            if (other.listCoverages != null)
                return false;
        } else if (!listCoverages.equals(other.listCoverages))
            return false;
        if (payments == null) {
            if (other.payments != null)
                return false;
        } else if (!payments.equals(other.payments))
            return false;
        if (paymentsNumber == null) {
            if (other.paymentsNumber != null)
                return false;
        } else if (!paymentsNumber.equals(other.paymentsNumber))
            return false;
        if (surcharges == null) {
            if (other.surcharges != null)
                return false;
        } else if (!surcharges.equals(other.surcharges))
            return false;
        if (simulationQuoteId == null) {
            if (other.simulationQuoteId != null)
                return false;
        } else if (!simulationQuoteId.equals(other.simulationQuoteId))
            return false;
        if (taxAmount == null) {
            if (other.taxAmount != null)
                return false;
        } else if (!taxAmount.equals(other.taxAmount))
            return false;
        if (totAmn == null) {
            if (other.totAmn != null)
                return false;
        } else if (!totAmn.equals(other.totAmn))
            return false;
        if (totNetAmn == null) {
            if (other.totNetAmn != null)
                return false;
        } else if (!totNetAmn.equals(other.totNetAmn))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "VidaEnteraPLCotizacionResponse [simulationQuoteId=" + simulationQuoteId + ", totAmn=" + totAmn + ", totNetAmn="
                + totNetAmn + ", surcharges=" + surcharges + ", rights=" + rights + ", taxAmount=" + taxAmount
                + ", code=" + code + ", payments=" + payments + ", paymentsNumber=" + paymentsNumber
                + ", listCoverages=" + listCoverages + "]";
    }
}
