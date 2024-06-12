package com.mapfre.mifel.vida.service;

import com.mapfre.mifel.vida.model.request.EmailRequest;
import com.mapfre.mifel.vida.model.response.MifelResponse;

public interface EmailService {
	
	public MifelResponse<Object> sendEmail(EmailRequest emailRequest);


}
