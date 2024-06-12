package com.mapfre.mifel.vida.model.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

@ApiModel(description = " Respuesta API Emision")
public class VidaULEmisionResponse {

    private String policyId;
    private Integer code;

    public VidaULEmisionResponse() {

    }


    public String getPolicyId() {
        return policyId;
    }


    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }


    public Integer getCode() {
        return code;
    }


    public void setCode(Integer code) {
        this.code = code;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((policyId == null) ? 0 : policyId.hashCode());
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
        VidaULEmisionResponse other = (VidaULEmisionResponse) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (policyId == null) {
            if (other.policyId != null)
                return false;
        } else if (!policyId.equals(other.policyId))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "VidaEnteraEmisionResponse [policyId=" + policyId + ", code=" + code + "]";
    }
}
