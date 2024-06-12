package com.mapfre.mifel.vida.model.ul;

import java.util.List;

public class VariableData {
    private String riskNumber = "1";
    private String levelType = "1";
    private List<FieldRisk> fields;

    public String getRiskNumber() {
        return riskNumber;
    }

    public void setRiskNumber(String riskNumber) {
        this.riskNumber = riskNumber;
    }

    public String getLevelType() {
        return levelType;
    }

    public void setLevelType(String levelType) {
        this.levelType = levelType;
    }

    public List<FieldRisk> getFields() {
        return fields;
    }

    public void setFields(List<FieldRisk> fields) {
        this.fields = fields;
    }
}
