package com.mapfre.mifel.vida.service;

import com.mapfre.mifel.vida.model.request.PaymentRequest;
import com.mapfre.mifel.vida.model.response.PaymentCobranzaResponse;


public interface IPaymentService {
	
	PaymentCobranzaResponse payment(PaymentRequest request);

}
