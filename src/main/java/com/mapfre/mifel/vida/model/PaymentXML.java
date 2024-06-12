package com.mapfre.mifel.vida.model;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "DATOS_BANCARIOS")
public class PaymentXML {
	
	
	private String entityBank;
	
	
	private String fullName;
	
	
	private String monthDueCard;
	
	
	private String yearDueCard;
	
	
	private String cardNumber;
	
	
	private String cardCvvCode;
	

	private String amount;
	

	private String referencePolicyNumber;
	

	private String cardType;
	

	private String numberOfPayments;
	
	private String currencyCode;
	
	public PaymentXML()
	{
		
	}

	public String getAmount() {
		return amount;
	}
	@XmlAttribute(name = "MONTO")
	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getEntityBank() {
		return entityBank;
	}
	
	@XmlAttribute(name = "COD_ENTIDAD")
	public void setEntityBank(String entityBank) {
		this.entityBank = entityBank;
	}

	public String getFullName() {
		return fullName;
	}
	
	@XmlAttribute(name = "NOM_TERCERO")
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMonthDueCard() {
		return monthDueCard;
	}

	@XmlAttribute(name = "MES_EXP_TARJETA")
	public void setMonthDueCard(String monthDueCard) {
		this.monthDueCard = monthDueCard;
	}

	public String getYearDueCard() {
		return yearDueCard;
	}
	
	@XmlAttribute(name = "ANIO_EXP_TARJETA")
	public void setYearDueCard(String yearDueCard) {
		this.yearDueCard = yearDueCard;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	@XmlAttribute(name = "NUM_TARJETA")
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardCvvCode() {
		return cardCvvCode;
	}
	
	@XmlAttribute(name = "COD_TARJETA")
	public void setCardCvvCode(String cardCvvCode) {
		this.cardCvvCode = cardCvvCode;
	}

	public String getCardType() {
		return cardType;
	}
	@XmlAttribute(name = "TIP_TARJETA")
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getReferencePolicyNumber() {
		return referencePolicyNumber;
	}

	@XmlAttribute(name = "NUM_REFERENCIA")
	public void setReferencePolicyNumber(String referencePolicyNumber) {
		this.referencePolicyNumber = referencePolicyNumber;
	}

	public String getNumberOfPayments() {
		return numberOfPayments;
	}

	public void setNumberOfPayments(String numberOfPayments) {
		this.numberOfPayments = numberOfPayments;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	@Override
	public String toString() {
		return "PaymentXML [entityBank=" + entityBank + ", fullName=" + fullName + ", monthDueCard=" + monthDueCard
				+ ", yearDueCard=" + yearDueCard + ", cardNumber=" + cardNumber + ", cardCvvCode=" + cardCvvCode
				+ ", amount=" + amount + ", referencePolicyNumber=" + referencePolicyNumber + ", cardType=" + cardType
				+ ", numberOfPayments=" + numberOfPayments + ", currencyCode=" + currencyCode + "]";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
