package com.mapfre.mifel.vida.service.impl;

import java.util.Base64;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mapfre.mifel.vida.model.request.EmailRequest;
import com.mapfre.mifel.vida.model.response.MifelResponse;
import com.mapfre.mifel.vida.service.EmailService;
import com.mapfre.mifel.vida.utils.VidaULContants;

@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired
    private JavaMailSender emailSender;
	
	private static final Logger LOGGER = LogManager.getLogger(EmailServiceImpl.class);
	
	@Value("${spring.mail.username}")
	private String userMailHost;
	
	/**
	 * Metodo para el envio de correo elctrónico
	 */
    @Override
    public MifelResponse<Object> sendEmail(EmailRequest emailRequest) {
    	MifelResponse<Object> response = new MifelResponse<>();
  		try {
  			
  	    	MimeMessage message = emailSender.createMimeMessage();
  			LOGGER.info(String.format("Enviando correo con adjuntos de %s", 
  					(emailRequest.getTypeDoc() == VidaULContants.InformacionEmailEnvio.DOCUMENTO_COTIZACION ? "COTIZACION" : "EMISION")));
  			MimeMessageHelper helper = new MimeMessageHelper(message, true);
  	        helper.setFrom(this.userMailHost);
  	        helper.setTo(emailRequest.getRecipient());
  	        helper.setSubject(this.validateSubjectWithTypeDoc(emailRequest.getTypeDoc()));
  	        helper.setText(this.validateBodyMessageWithTypeDoc(emailRequest.getTypeDoc()));
  	        
            
  	        for(int i = 0; i < emailRequest.getAttachment().size(); i++) {
  	        	byte[] doc = Base64.getDecoder().decode(emailRequest.getAttachment().get(i));
  	        	try {
  	        		String fileName = this.generateFileNameForSendMail(i, emailRequest.getTypeDoc());
  	        		LOGGER.info(String.format("Adjuntando documento(s): %s", fileName));
					helper.addAttachment(fileName, new ByteArrayResource(doc));
				} catch (MessagingException e) {
					e.printStackTrace();
				}
  	        }

  	        emailSender.send((javax.mail.internet.MimeMessage) message);
  	        LOGGER.info("[OK] Enviando correo con adjuntos"); 
  	        response.setStatus(VidaULContants.HeadersResponse.ESTATUS_EXITOSO);
  	        response.setMessage(VidaULContants.HeadersResponse.MENSAJE_EXITOSO);
  	        response.setShortCode(VidaULContants.HeadersResponse.CODIGO_CORTO_EXITO);
  	        response.setData("Correo enviado exitosamente");
  	        return response;
  	     
  		} catch (MessagingException e) {
  			// TODO Auto-generated catch block
  			 LOGGER.info("[ERR] Enviando correo con adjuntos"); 
  			e.printStackTrace();
  			response.setStatus(VidaULContants.HeadersResponse.ESTATUS_FALLIDO);
   	        response.setMessage(VidaULContants.HeadersResponse.MENSAJE_FALLIDO);
   	        response.setShortCode(VidaULContants.HeadersResponse.CODIGO_CORTO_FALLIDO);
   	        response.setData(e.getMessage());
   	        return response;
  			
  		}
    }
    
    /**
     * Este metodo genera el nombre del archivo a ajuntar en el envio del correo
     * @param index
     * @param typeDoc
     * @return
     */
    public String generateFileNameForSendMail(int index, Integer typeDoc) {
    	if(typeDoc == VidaULContants.InformacionEmailEnvio.DOCUMENTO_COTIZACION ) {
    		return "COTIZACION_" + index + ".pdf";
    	}
    	return "EMISION_" + index + ".pdf";
    }
    
    /**
     * Este metodo devolverá segun el tipo de documetno que se requiera el asunto confome a las configuradas en las constantes
     * @return
     */
    public String validateSubjectWithTypeDoc(Integer typeDoc) {
    	if(typeDoc == VidaULContants.InformacionEmailEnvio.DOCUMENTO_COTIZACION ) {
    		return VidaULContants.InformacionEmailEnvio.ASUNTO_COTIZACION;
    	}
    	return VidaULContants.InformacionEmailEnvio.ASUNTO_EMISION;
    }
    /**
     * Este metodo devolverá segun el tipo de documetno que se requiera el cuerpo del correo confome a las configuradas en las constantes
     * @return
     */
    public String validateBodyMessageWithTypeDoc(Integer typeDoc) {
    	if(typeDoc == VidaULContants.InformacionEmailEnvio.DOCUMENTO_COTIZACION ) {
    		return VidaULContants.InformacionEmailEnvio.CUERPO_MENSAJE_COTIZACION;
    	}
    	return VidaULContants.InformacionEmailEnvio.CUERPO_MENSAJE_EMISION;
    }



}