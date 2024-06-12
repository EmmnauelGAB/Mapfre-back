package com.mapfre.mifel.vida.model.ul;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PolicyData {
    private String branchCode;
    private String agentCode;
    private String commCuadroCode;
    private String groupPolicyNumber;
    private String contractNumber;
    private String policyEffectiveDate;
    private String policyEndDate;
    private String documCode = "XAXX010101000";
    private String managerType;
    private String paymentMethodCode;
    private String durationType;
    private String prorationMark;

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getCommCuadroCode() {
        return commCuadroCode;
    }

    public void setCommCuadroCode(String commCuadroCode) {
        this.commCuadroCode = commCuadroCode;
    }

    public String getGroupPolicyNumber() {
        return groupPolicyNumber;
    }

    public void setGroupPolicyNumber(String groupPolicyNumber) {
        this.groupPolicyNumber = groupPolicyNumber;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getPolicyEffectiveDate() {
        return policyEffectiveDate;
    }

    public void setPolicyEffectiveDate(String policyEffectiveDate) {
        this.policyEffectiveDate = policyEffectiveDate;
    }

    public String getPolicyEndDate() {
        return policyEndDate;
    }

    public void setPolicyEndDate(String policyEndDate) {
        this.policyEndDate = policyEndDate;
    }

    public String getDocumCode() {
        return documCode;
    }

    public void setDocumCode(String documCode) {
        this.documCode = documCode;
    }

    public String getManagerType() {
        return managerType;
    }

    public void setManagerType(String managerType) {
        this.managerType = managerType;
    }

    public String getPaymentMethodCode() {
        return paymentMethodCode;
    }

    public void setPaymentMethodCode(String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }

    public String getDurationType() {
        return durationType;
    }

    public void setDurationType(String durationType) {
        this.durationType = durationType;
    }

    public String getProrationMark() {
        return prorationMark;
    }

    public void setProrationMark(String prorationMark) {
        this.prorationMark = prorationMark;
    }

	private String[] getAllValues(PolicyData dto){
        String[] values= {dto.getBranchCode(), dto.getAgentCode(), dto.getCommCuadroCode(), dto.getGroupPolicyNumber(), dto.getContractNumber(),
                dto.getPolicyEffectiveDate(), dto.getPolicyEndDate(), dto.getDocumCode(), dto.getManagerType(), dto.getPaymentMethodCode(), dto.getDurationType(),
                dto.getProrationMark()};
        return values;
    }

    private String[] getColumnNames() {
        //List<Field> parameters= Arrays.asList(getClass().getDeclaredFields());
        Field[] fs = getClass().getDeclaredFields();
        List<String> ll= new ArrayList<>();
        String columnValue="";
        for (Field f : fs) {
            switch(f.getName()){
                case "branchCode": columnValue="COD_RAMO";
                    break;
                case "agentCode": columnValue="COD_AGT";
                    break;
                case "commCuadroCode": columnValue="COD_CUADRO_COM";
                    break;
                case "groupPolicyNumber": columnValue="NUM_POLIZA_GRUPO";
                    break;
                case "contractNumber": columnValue="NUM_CONTRATO";
                    break;
                case "policyEffectiveDate": columnValue="FEC_EFEC_POLIZA";
                    break;
                case "policyEndDate": columnValue="FEC_VCTO_POLIZA";
                    break;
                case "documCode": columnValue="COD_DOCUM";
                    break;
                case "managerType": columnValue="TIP_GESTOR";
                    break;
                case "paymentMethodCode": columnValue="COD_FRACC_PAGO";
                    break;
                case "durationType": columnValue="TIP_DURACION";
                    break;
                case "prorationMark": columnValue="MCA_PRORRATA";
                    break;
                default: columnValue="INVALID_VALUE";
                    break;
            }
            ll.add(columnValue);
        }
        String[] names= new String[ll.size()];
        names=ll.toArray(names);
        return names;
    }

    public Map<String, String> dameLista(PolicyData listValue){
        String[] names= listValue.getColumnNames();
        String[] values= listValue.getAllValues(listValue);

        Map<String, String> myMap = new LinkedHashMap<>();
        for (int i = 0; i < names.length; i++) {
            for (String value : values) {
                String aux= values[i] !=null ? values[i]: "";
                if(aux.length() != 0){
                    myMap.put(names[i], values[i]);
                }
            }
        }
        return myMap;
    }
}
