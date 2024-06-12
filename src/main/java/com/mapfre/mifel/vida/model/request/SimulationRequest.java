package com.mapfre.mifel.vida.model.request;

import com.mapfre.mifel.vida.model.ul.*;

import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class SimulationRequest {
    private PolicyData policyData;
    private List<VariableData> variableData;
    private List<VariableDataCoverage> variableDataCoverage;
    private List<Coverage> coverage;
    private ContractorData insuredData;
    private Optional<List<Beneficiaries>> beneficiary = Optional.empty();
    private BankData bankData;
    private PremiumData premiumData;    
}
