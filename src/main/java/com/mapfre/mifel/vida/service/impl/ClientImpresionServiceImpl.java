package com.mapfre.mifel.vida
.service.impl;

import static com.mapfre.mifel.vida.utils.ClienteImpresionConstants.AFTER_VALUE;
import static com.mapfre.mifel.vida.utils.ClienteImpresionConstants.BEFORE_VALUE;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.mapfre.mifel.vida.exception.NoContentException;
import com.mapfre.mifel.vida.exception.WSClientException;
import com.mapfre.mifel.vida.model.request.EmailRequest;
import com.mapfre.mifel.vida.model.response.EmisionResponse;
import com.mapfre.mifel.vida.model.response.MifelResponse;
import com.mapfre.mifel.vida.service.ClientImpresionService;
import com.mapfre.mifel.vida.service.EmailService;
import com.mapfre.mifel.vida.utils.VidaULContants;

@Service
public class ClientImpresionServiceImpl implements ClientImpresionService {
	
	private static final Logger LOGGER = LogManager.getLogger(ClientImpresionServiceImpl.class);

	@Value("${comunes.impresion.generate.url}")
	private String generateUrl;

	@Value("${comunes.impresion.generate.parametros}")
	private String generateParametros;

	@Value("${comunes.impresion.generate.fullurl}")
	private String generateFullUrl;

	@Value("${comunes.impresion.generate.useget}")
	private boolean generateUseGet;

	@Value("${comunes.impresion.download.url}")
	private String downloadUrl;

	@Value("${comunes.impresion.download.parametros}")
	private String downloadParametros;

	@Value("${comunes.impresion.download.fullurl}")
	private String downloadFullUrl;

	@Value("${comunes.impresion.download.useget}")
	private boolean downloadUseGet;
	
	@Value("${emision.pdf.endpoint}")
	private String endpointGetEmisionPdf;
	
	@Value("${emision.pdf.endpoint.params}")
	private String endpointGetEmisionPdfParams;

	@Autowired
	private RestTemplate restTemplate;
	
	 @Autowired
	 private EmailService emailService;

	@Override
	public String getImpresionId(String poliza, String spto, String codCia) {
		String response;
		try {
			if (generateUseGet) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.TEXT_XML);
				HttpEntity<String> entity = new HttpEntity<>(headers);
				response = restTemplate.exchange(String.format(generateFullUrl, poliza, spto, codCia), HttpMethod.GET,
						entity, String.class).getBody();
			} else {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				HttpEntity<String> entity = new HttpEntity<>(String.format(generateParametros, poliza, spto, codCia),
						headers);
				response = restTemplate.exchange(generateUrl, HttpMethod.POST, entity, String.class).getBody();
			}
			return StringUtils.substringBetween(response, BEFORE_VALUE, AFTER_VALUE);
		} catch (HttpServerErrorException e) {
			throw new WSClientException(
					String.format("Server responded with error when generating doc by querying the url %s: %s",
							generateUrl, e.getMessage()),
					e);
		} catch (HttpClientErrorException e) {
			throw new WSClientException(String.format("Client fail when generating doc by querying the url %s: %s",
					generateUrl, e.getMessage()), e);
		} catch (RestClientException e) {
			throw new WSClientException(
					String.format("Error when generating doc by querying the url %s: %s", generateUrl, e.getMessage()),
					e);
		}
	}

	@Override
	public byte[] downloadFile(String poliza, String spto, String codCia) {
		return this.downloadFile(this.getImpresionId(poliza, spto, codCia));
	}

	@Override
	public byte[] downloadFile(String impresionId) {
		try {
			byte[] file;
			if (downloadUseGet) {
				ResponseEntity<byte[]> response = restTemplate.exchange(String.format(downloadFullUrl, impresionId),
						HttpMethod.GET, null, byte[].class);
				file = response.getBody();
			} else {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				HttpEntity<String> entity = new HttpEntity<>(String.format(downloadParametros, impresionId), headers);
				ResponseEntity<byte[]> response = restTemplate.exchange(downloadUrl, HttpMethod.POST, entity,
						byte[].class);
				file = response.getBody();
			}
			if (file != null && file.length > 0) {
				return file;
			} else {
				throw new NoContentException("Requested file is null or empty docId = " + impresionId);
			}
		} catch (HttpServerErrorException e) {
			throw new WSClientException(
					String.format("Server responded with error when download file by querying the url %s: %s",
							String.format(downloadFullUrl, impresionId), e.getMessage()),
					e);
		} catch (HttpClientErrorException e) {
			throw new WSClientException(String.format("Client fail when download file by querying the url %s: %s",
					String.format(downloadFullUrl, impresionId), e.getMessage()), e);
		} catch (RestClientException e) {
			throw new WSClientException(String.format("Error when download file by querying the url %s: %s",
					String.format(downloadFullUrl, impresionId), e.getMessage()), e);
		}
	}

	@Override
	public MifelResponse<EmisionResponse> getImpresionEmision(String poliza, String strNegocio, String strEndoso, String nmi, String emailDestiny)  {
		EmisionResponse responseEmision = new EmisionResponse();
		MifelResponse<EmisionResponse> responseMifel = new MifelResponse<>();
		try {
			
			String apiUrlBase = this.endpointGetEmisionPdf;
			String apiUrlParams = String.format(this.endpointGetEmisionPdfParams, poliza, strEndoso, strNegocio, nmi);
			LOGGER.info(String.format("Url creada con parametros de peticion: %s", (apiUrlBase + apiUrlParams)));
			ResponseEntity<byte[]> response = this.restTemplate.getForEntity((apiUrlBase + apiUrlParams), byte[].class);
			if(response.getStatusCode() != HttpStatus.OK) {
				throw new Exception("Ocurrió un problema al querer obtener el pdf de emisión");
			}
	        byte[] pdfBytes = response.getBody();
			// byte[] pdfBytes = this.createPdfWithText(); Descomentar si se quiere ejecutar de manera local
			
	        if (pdfBytes != null) {
	        	String base64 = Base64.getEncoder().encodeToString(pdfBytes);
	        	responseEmision.setFileContent(base64);
	        	responseEmision.setStatusDocument("Documento emisión obtenido y enviado por correo exitosamente");
	        	responseMifel.setData(responseEmision);
	        	responseMifel.setStatus(VidaULContants.HeadersResponse.ESTATUS_EXITOSO);
	        	responseMifel.setMessage(VidaULContants.HeadersResponse.MENSAJE_EXITOSO);
	        	responseMifel.setShortCode(VidaULContants.HeadersResponse.CODIGO_CORTO_EXITO);
	        	responseMifel.setData(responseEmision);
	        	MifelResponse<Object> responseEmail = this.sendEmailDocument(emailDestiny, responseEmision.getFileContent());
	        	if(responseEmail.getStatus() == VidaULContants.HeadersResponse.ESTATUS_FALLIDO) {
	        		throw new Exception("El envio del correo electrónico falló");
	        	}
	            return responseMifel;
	        } else {
	            responseEmision.setFileContent(null);
	        	responseEmision.setStatusDocument("El documento no se pudo descargar");
	        	responseMifel.setData(responseEmision);
	        	responseMifel.setStatus(VidaULContants.HeadersResponse.ESTATUS_FALLIDO);
	        	responseMifel.setMessage(VidaULContants.HeadersResponse.MENSAJE_FALLIDO);
	        	responseMifel.setShortCode(VidaULContants.HeadersResponse.CODIGO_CORTO_FALLIDO);
	            return responseMifel;
	        }
		}catch(Exception e) {
			e.printStackTrace();
			 	responseEmision.setFileContent(null);
	        	responseEmision.setStatusDocument(e.getMessage());
	        	responseMifel.setData(responseEmision);
	        	responseMifel.setStatus(VidaULContants.HeadersResponse.ESTATUS_FALLIDO);
	        	responseMifel.setMessage(VidaULContants.HeadersResponse.MENSAJE_FALLIDO);
	        	responseMifel.setShortCode(VidaULContants.HeadersResponse.CODIGO_CORTO_FALLIDO);
	        	return responseMifel;
		}
		
	}
	
	public MifelResponse<Object> sendEmailDocument(String emailDestiny, String docEmision){
		MifelResponse<Object> responseEmail = new MifelResponse<>();
		 EmailRequest emailRequest = new EmailRequest();
		 emailRequest.setRecipient(emailDestiny);
		 emailRequest.setTypeDoc(VidaULContants.InformacionEmailEnvio.DOCUMENTO_EMISION);
		 List<String> documents = new LinkedList<>();
		 documents.add(docEmision);
		 emailRequest.setAttachment(documents);
		 responseEmail = this.emailService.sendEmail(emailRequest);
		 return responseEmail;
	}
	
	/**
	 * Este metodo es unicamente para realizar pruebas con un pdf
	 * @return
	 */
	public byte[] createPdfWithText() {
        try {
            // Crear un documento PDF
            Document document = new Document();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, outputStream);

            // Abrir el documento para agregar contenido
            document.open();

            // Agregar un párrafo con el texto "Hola"
            document.add(new Paragraph("Hola"));

            // Cerrar el documento
            document.close();

            // Convertir el contenido del documento a bytes
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
