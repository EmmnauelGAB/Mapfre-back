package com.mapfre.mifel.vida.model.request;

import java.util.List;

public class EmailRequest {

	private String recipient;
	 /**
	  *  En caso de necesitar estos atributos descomentarlos, colocar los metodos setters y getters y adecuar la lógica en 
	  *  package com.mapfre.mifel.vida.service.impl Method: sendEmail
	  */
	// private String msgBody;
    //private String subject;
    private Integer typeDoc; // 1 = Cotizacion, 2 = Emision
    private List<String> attachment;
    
    public EmailRequest() {
    	
    }
    
	public EmailRequest(String recipient, Integer typeDoc, List<String> attachment) {
		super();
		this.recipient = recipient;
		this.typeDoc = typeDoc;
		this.attachment = attachment;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public List<String> getAttachment() {
		return attachment;
	}
	public void setAttachment(List<String> attachment) {
		this.attachment = attachment;
	}
	public Integer getTypeDoc() {
		return typeDoc;
	}
	public void setTypeDoc(Integer typeDoc) {
		this.typeDoc = typeDoc;
	}
	@Override
	public String toString() {
		return "EmailRequest [recipient=" + recipient + ", typeDoc=" + typeDoc + ", attachment=" + attachment + "]";
	}
	
    
}