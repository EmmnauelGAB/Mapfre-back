package com.mapfre.mifel.vida.service;

import com.mapfre.mifel.vida.model.request.SimulationRequest;
import com.mapfre.mifel.vida.model.response.VidaULCotizacionResponse;

public interface GenerationPdfDocumentService {
	
	public String generatePdfQuotation(SimulationRequest information, VidaULCotizacionResponse responseQuotation);
	
	public String createPDFHPExstream(String xml, String email);
}
