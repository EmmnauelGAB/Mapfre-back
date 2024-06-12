package com.mapfre.mifel.vida.mapper;

import com.mapfre.mifel.vida.model.PaymentXML;
import com.mapfre.mifel.vida.model.request.PaymentRequest;
import com.mapfre.mifel.vida.model.response.Cobro;
import com.mapfre.mifel.vida.model.response.Data;
import com.mapfre.mifel.vida.model.response.Param;
import com.mapfre.mifel.vida.model.response.PaymentCobranzaResponse;
import com.mapfre.mifel.vida.model.response.PaymentResponse;
import com.mapfre.mifel.vida.model.response.Result;

public class PaymentMapper {
	
	
	public static PaymentXML paymentJsonXML(PaymentRequest payment) {
		
		PaymentXML payment2 = new PaymentXML();
		
		payment2.setEntityBank(payment.getEntityBank());
		payment2.setFullName(payment.getFullName());
		payment2.setMonthDueCard(payment.getMonthDueCard());
		payment2.setYearDueCard(payment.getYearDueCard());
		payment2.setCardNumber(payment.getCardNumber());
		payment2.setCardCvvCode(payment.getCardCvvCode());
		payment2.setAmount(payment.getAmount());
		payment2.setReferencePolicyNumber(payment.getReferencePolicyNumber());
		payment2.setCardType(payment.getCardType());
		
		return payment2;
	}
	
	public static PaymentCobranzaResponse paymentXMLJson(PaymentCobranzaResponse payment) {
		
		PaymentCobranzaResponse payment2 = new PaymentCobranzaResponse();
		Param param = new Param();
		param.setResult(payment.getParam().getResult());
		Data data = new Data();
		Cobro cobro = new Cobro();
		cobro.setNumAutorizacion(payment.getData().getCobro().getNumAutorizacion());
		cobro.setNumReferencia(payment.getData().getCobro().getNumReferencia());
		data.setCobro(cobro);
		payment2.setParam(param);
		payment2.setData(data);
		
		return payment2;
	}

}
