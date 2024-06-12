package com.mapfre.mifel.vida.model.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


import io.swagger.annotations.ApiModel;

@ApiModel(description = "Informacion para realizar un pago en USD o MXP")
public class PaymentRequest {
	
	public static interface PaymentMethod { }
	@NotNull(groups = {PaymentMethod.class}, message = "El entityBank es obligatorio")
	@NotEmpty(groups = {PaymentMethod.class}, message = "El entityBank no puede estar en blanco")
	private String entityBank;
	
	@NotNull(groups = {PaymentMethod.class}, message = "El fullName es obligatorio")
	@NotEmpty(groups = {PaymentMethod.class}, message = "El fullName no puede estar en blanco")
	private String fullName;
	
	@NotNull(groups = {PaymentMethod.class}, message = "El monthDueCard es obligatorio")
	@NotEmpty(groups = {PaymentMethod.class}, message = "El monthDueCard no puede estar en blanco")
	private String monthDueCard;
	
	@NotNull(groups = {PaymentMethod.class}, message = "El yearDueCard es obligatorio")
	@NotEmpty(groups = {PaymentMethod.class}, message = "El yearDueCard no puede estar en blanco")
	private String yearDueCard;
	
	@NotNull(groups = {PaymentMethod.class}, message = "El cardNumber es obligatorio")
	@NotEmpty(groups = {PaymentMethod.class}, message = "El cardNumber no puede estar en blanco")
	private String cardNumber;
	
	@NotNull(groups = {PaymentMethod.class}, message = "El cardCvvCode es obligatorio")
	@NotEmpty(groups = {PaymentMethod.class}, message = "El cardCvvCode no puede estar en blanco")
	private String cardCvvCode;
	
	@NotNull(groups = {PaymentMethod.class}, message = "El amount es obligatorio")
	@NotEmpty(groups = {PaymentMethod.class}, message = "El amount no puede estar en blanco")
	private String amount;
	
	@NotNull(groups = {PaymentMethod.class}, message = "El referencePolicyNumber es obligatorio")
	@NotEmpty(groups = {PaymentMethod.class}, message = "El referencePolicyNumber no puede estar en blanco")
	private String referencePolicyNumber;
	
	@NotNull(groups = {PaymentMethod.class}, message = "El cardType es obligatorio")
	@NotEmpty(groups = {PaymentMethod.class}, message = "El cardType no puede estar en blanco")
	@Pattern(groups= {PaymentMethod.class}, message="cardType solo acepta valores del 1 al 3", regexp="[1|2|3]")
	private String cardType;
	
	
	private String numberOfPayments;
	
	
	private String currencyCode;
	
	public PaymentRequest() {
		
	}
	public String getEntityBank() {
		return entityBank;
	}
	public void setEntityBank(String entityBank) {
		this.entityBank = entityBank;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getMonthDueCard() {
		return monthDueCard;
	}
	public void setMonthDueCard(String monthDueCard) {
		this.monthDueCard = monthDueCard;
	}
	public String getYearDueCard() {
		return yearDueCard;
	}
	public void setYearDueCard(String yearDueCard) {
		this.yearDueCard = yearDueCard;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardCvvCode() {
		return cardCvvCode;
	}
	public void setCardCvvCode(String cardCvvCode) {
		this.cardCvvCode = cardCvvCode;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getReferencePolicyNumber() {
		return referencePolicyNumber;
	}
	public void setReferencePolicyNumber(String referencePolicyNumber) {
		this.referencePolicyNumber = referencePolicyNumber;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((cardCvvCode == null) ? 0 : cardCvvCode.hashCode());
		result = prime * result + ((cardNumber == null) ? 0 : cardNumber.hashCode());
		result = prime * result + ((cardType == null) ? 0 : cardType.hashCode());
		result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
		result = prime * result + ((entityBank == null) ? 0 : entityBank.hashCode());
		result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((numberOfPayments == null) ? 0 : numberOfPayments.hashCode());
		result = prime * result + ((monthDueCard == null) ? 0 : monthDueCard.hashCode());
		result = prime * result + ((referencePolicyNumber == null) ? 0 : referencePolicyNumber.hashCode());
		result = prime * result + ((yearDueCard == null) ? 0 : yearDueCard.hashCode());
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
		PaymentRequest other = (PaymentRequest) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (cardCvvCode == null) {
			if (other.cardCvvCode != null)
				return false;
		} else if (!cardCvvCode.equals(other.cardCvvCode))
			return false;
		if (cardNumber == null) {
			if (other.cardNumber != null)
				return false;
		} else if (!cardNumber.equals(other.cardNumber))
			return false;
		if (cardType == null) {
			if (other.cardType != null)
				return false;
		} else if (!cardType.equals(other.cardType))
			return false;
		if (currencyCode == null) {
			if (other.currencyCode != null)
				return false;
		} else if (!currencyCode.equals(other.currencyCode))
			return false;
		if (entityBank == null) {
			if (other.entityBank != null)
				return false;
		} else if (!entityBank.equals(other.entityBank))
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (numberOfPayments == null) {
			if (other.numberOfPayments != null)
				return false;
		} else if (!numberOfPayments.equals(other.numberOfPayments))
			return false;
		if (monthDueCard == null) {
			if (other.monthDueCard != null)
				return false;
		} else if (!monthDueCard.equals(other.monthDueCard))
			return false;
		if (referencePolicyNumber == null) {
			if (other.referencePolicyNumber != null)
				return false;
		} else if (!referencePolicyNumber.equals(other.referencePolicyNumber))
			return false;
		if (yearDueCard == null) {
			if (other.yearDueCard != null)
				return false;
		} else if (!yearDueCard.equals(other.yearDueCard))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "PaymentReponse [entityBank=" + entityBank + ", fullName=" + fullName + ", monthDueCard=" + monthDueCard
				+ ", yearDueCard=" + yearDueCard + ", cardNumber=" + cardNumber + ", cardCvvCode=" + cardCvvCode
				+ ", amount=" + amount + ", referencePolicyNumber=" + referencePolicyNumber + ", cardType=" + cardType
				+ ", numberOfPayments=" + numberOfPayments + ", currencyCode=" + currencyCode + "]";
	}
	
	
	
	  

}
