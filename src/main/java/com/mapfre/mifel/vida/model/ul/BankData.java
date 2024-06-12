package com.mapfre.mifel.vida.model.ul;

public class BankData {

    private String paymentType;//tipo_pago
    private String entityCode;//cod_entidad
    private String contractingFistName;//nom_tercero
    private String contractingMiddleName;//ape1_tercero
    private String contractingLastName;//ape2_tercero
    private String cardExpirationDate;//fec_vcto_tarjeta
    private String cardExpirationMonth;//mes_vcto_tarjeta
    private String cardExpirationYear;//ano_vcto_tarjeta
    private String email;//email
    private String userCode;//cod_usr
    private String accountNumber;///cta_cte
    private String cardCode;//cod_tarjeta
    private String cardType;//tip_tarjeta
    private String cutOffDate;//fec_corte;
    private Integer placeNumber;//num_plaza;
    private String cardNumber;//num_tarjeta

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
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

    public String getCardExpirationDate() {
        return cardExpirationDate;
    }

    public void setCardExpirationDate(String cardExpirationDate) {
        this.cardExpirationDate = cardExpirationDate;
    }

    public String getCardExpirationMonth() {
        return cardExpirationMonth;
    }

    public void setCardExpirationMonth(String cardExpirationMonth) {
        this.cardExpirationMonth = cardExpirationMonth;
    }

    public String getCardExpirationYear() {
        return cardExpirationYear;
    }

    public void setCardExpirationYear(String cardExpirationYear) {
        this.cardExpirationYear = cardExpirationYear;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCutOffDate() {
        return cutOffDate;
    }

    public void setCutOffDate(String cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

    public Integer getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(Integer placeNumber) {
        this.placeNumber = placeNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
