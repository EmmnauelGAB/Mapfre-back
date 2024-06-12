package com.mapfre.mifel.vida.model.ul;

public class Beneficiaries {
    private String personType;//mca_fisico
    private String dateBirth;//fec_nacimiento
    private String realAge;//edad_real
    private String contractingFistName;//nom_tercero
    private String contractingMiddleName;//ape1_tercero
    private String contractingLastName;//ape2_tercero
    private String relationshipCode;//cod_parentesco
    private String occupationCode;//cod_ocupacion
    private String percentageParticipation;//pct_participacion
    private String beneficiaryType;//tip_benef
    private String sequenceNumber; //num_secu

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getRealAge() {
        return realAge;
    }

    public void setRealAge(String realAge) {
        this.realAge = realAge;
    }

    public String getContractingFistName() {
        return contractingFistName;
    }

    public void setContractingFistName(String contractingFistName) {
        this.contractingFistName = contractingFistName;
    }

    public String getContractingMiddleName() {
        return contractingMiddleName;
    }

    public void setContractingMiddleName(String contractingMiddleName) {
        this.contractingMiddleName = contractingMiddleName;
    }

    public String getContractingLastName() {
        return contractingLastName;
    }

    public void setContractingLastName(String contractingLastName) {
        this.contractingLastName = contractingLastName;
    }

    public String getRelationshipCode() {
        return relationshipCode;
    }

    public void setRelationshipCode(String relationshipCode) {
        this.relationshipCode = relationshipCode;
    }

    public String getOccupationCode() {
        return occupationCode;
    }

    public void setOccupationCode(String occupationCode) {
        this.occupationCode = occupationCode;
    }

    public String getPercentageParticipation() {
        return percentageParticipation;
    }

    public void setPercentageParticipation(String percentageParticipation) {
        this.percentageParticipation = percentageParticipation;
    }

    public String getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(String beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
