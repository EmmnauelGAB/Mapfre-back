package com.mapfre.mifel.vida.model.request;

import com.mapfre.mifel.vida.model.ul.*;

import java.util.List;

public class IssueRequest {

    private PolicyData policyData;
    private List<VariableData> variableData;
    private List<VariableDataCoverage> variableDataCoverage;
    private List<Coverage> coverage;
    private ContractorData contractorData;
    private InsuredData insuredData;
    private List<Beneficiaries> beneficiary;
    private BankData bankData;

    public PolicyData getPolicyData() {
        return policyData;
    }

    public void setPolicyData(PolicyData policyData) {
        this.policyData = policyData;
    }

    public List<VariableData> getVariableData() {
        return variableData;
    }

    public void setVariableData(List<VariableData> variableData) {
        this.variableData = variableData;
    }

    public List<Coverage> getCoverage() {
        return coverage;
    }

    public void setCoverage(List<Coverage> coverage) {
        this.coverage = coverage;
    }

    public ContractorData getContractorData() {
        return contractorData;
    }

    public void setContractorData(ContractorData contractorData) {
        this.contractorData = contractorData;
    }

    public InsuredData getInsuredData() {
        return insuredData;
    }

    public void setInsuredData(InsuredData insuredData) {
        this.insuredData = insuredData;
    }

    public List<Beneficiaries> getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(List<Beneficiaries> beneficiary) {
        this.beneficiary = beneficiary;
    }

    public BankData getBankData() {
        return bankData;
    }

    public void setBankData(BankData bankData) {
        this.bankData = bankData;
    }

	public List<VariableDataCoverage> getVariableDataCoverage() {
		return variableDataCoverage;
	}

	public void setVariableDataCoverage(List<VariableDataCoverage> variableDataCoverage) {
		this.variableDataCoverage = variableDataCoverage;
	}
}
