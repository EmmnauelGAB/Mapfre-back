package com.mapfre.mifel.vida.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.net.HttpHeaders;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.mapfre.mifel.vida.model.CatalogsBanksResponse;
import com.mapfre.mifel.vida.model.Coverages;
import com.mapfre.mifel.vida.model.request.EmailRequest;
import com.mapfre.mifel.vida.model.request.ImpresionEmisionRequest;
import com.mapfre.mifel.vida.model.request.ImpresionHPRequest;
import com.mapfre.mifel.vida.model.request.IssueRequest;
import com.mapfre.mifel.vida.model.request.PaymentRequest;
import com.mapfre.mifel.vida.model.request.PaymentRequest.PaymentMethod;
import com.mapfre.mifel.vida.model.request.SimulationRequest;
import com.mapfre.mifel.vida.model.response.EmisionResponse;
import com.mapfre.mifel.vida.model.response.ImpresionHPResponse;
import com.mapfre.mifel.vida.model.response.ImpresionResponse;
import com.mapfre.mifel.vida.model.response.MifelResponse;
import com.mapfre.mifel.vida.model.response.PaymentCobranzaResponse;
import com.mapfre.mifel.vida.model.response.VidaULCotizacionResponse;
import com.mapfre.mifel.vida.model.response.VidaULEmisionResponse;
import com.mapfre.mifel.vida.service.CatalogoBancosService;
import com.mapfre.mifel.vida.service.ClientImpresionService;
import com.mapfre.mifel.vida.service.ClientImpresionServiceHP;
import com.mapfre.mifel.vida.service.EmailService;
import com.mapfre.mifel.vida.service.GenerationPdfDocumentService;
import com.mapfre.mifel.vida.service.IPaymentService;
import com.mapfre.mifel.vida.service.VidaULService;
import com.mapfre.mifel.vida.utils.VidaULContants;
import com.mapfre.mifel.vida.utils.VidaULContants.Impresion;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import static org.springframework.http.ResponseEntity.ok;

@Api(value = "Manejo de cotizacion y emision de polizas para Vida Mifel UnitLinked")
@CrossOrigin(origins ={"*", "http://localhost:8081"})
@RestController
@RequestMapping("/api/life/ul")
public class VidaULController {

    private static final Logger logger = LogManager.getLogger(VidaULController.class);

    @Autowired
    VidaULService service;
    
    @Autowired
	ClientImpresionService clientImpresionService;
    
	@Autowired
	IPaymentService IPaymentService;
	
	@Autowired
	CatalogoBancosService serviceBancos;
	
    @Autowired
    private EmailService emailService;

	@Autowired
	private GenerationPdfDocumentService generationPdfService;

	
	@Autowired
	ClientImpresionServiceHP clientImpresionServiceHP;

    @ApiOperation(value = "Operación para generar emision Mifel")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Respuesta correcta regresa el numero de poliza"),
            @ApiResponse(code = 204, message = "La poliza no puede generarse"),
            @ApiResponse(code = 401, message = "No está autorizado para ver este recurso"),
            @ApiResponse(code = 403, message = "Está prohibido acceder al recurso"),
            @ApiResponse(code = 404, message = "No se encuentra el recurso")
    })
    @RequestMapping(path = "/issue", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VidaULEmisionResponse> generaEmision(@RequestBody IssueRequest request) {

        VidaULEmisionResponse responseULEmision = new VidaULEmisionResponse();
        logger.debug("request Issue getBankData: {}", request.getBankData());
        logger.debug("request Issue getBeneficiary: {}", request.getBeneficiary());
        logger.debug("request Issue getContractorData: {}", request.getContractorData());
        logger.debug("request Issue getCoverage: {}", request.getCoverage());
        logger.debug("request Issue getInsuredData: {}", request.getInsuredData());
        logger.debug("request Issue getPolicyData: {}", request.getPolicyData());
        logger.debug("request Issue getVariableData: {}", request.getVariableData());
        logger.debug("request Issue getVariableDataCoverage: {}", request.getVariableDataCoverage());
        String response = service.validateEntry(request);
        if(response.equals("success")){
            responseULEmision = service.getEmision(request);
            return new ResponseEntity<>(responseULEmision,HttpStatus.OK);
        }else{
            responseULEmision.setCode(400);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseULEmision);
        }
    }

    @ApiOperation(value = "Generar una cotizacion para Mifel")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Respuesta correcta regresa el numero de cotizacion y la informacion"),
            @ApiResponse(code = 400, message = "Existe un error en alguno de los campos"),
            @ApiResponse(code = 401, message = "No está autorizado para ver este recurso"),
            @ApiResponse(code = 403, message = "Está prohibido acceder al recurso"),
            @ApiResponse(code = 404, message = "No se encuentra el recurso"),
            @ApiResponse(code = 500, message = "Error interno")
    })
    @PostMapping("/quote")
    public ResponseEntity<MifelResponse<VidaULCotizacionResponse>> getCotizacion(@RequestBody SimulationRequest request) {
    	MifelResponse<VidaULCotizacionResponse> responseM = new MifelResponse<>();
        VidaULCotizacionResponse response1 = new VidaULCotizacionResponse();
        //VidaULCotizacionResponse responseTest = new VidaULCotizacionResponse();
        //Se valida que existan las etiquetas necesarias para la cotizacion y emision
        String response = service.validaEtiquetas(request);
        logger.debug("response = {}",response);
        if(response.equals("success")){
        	logger.debug("***1");
            response1 = service.getCotizacion(request);
            logger.debug("***1END");
        	//responseTest = new VidaULCotizacionResponse();
            //responseTest.setSimulationQuoteId("12345678910");
            //Coverages coverages =  new Coverages();
            //coverages.setCoverageDesc("F");
            //coverages.setCoverageId("1");
            //coverages.setDeductible("1");
            //coverages.setPrimeAmn("1000");
            //coverages.setSumInsuredAmn("50000");
            //List<Coverages> listCoverages = new ArrayList<Coverages>();
            //listCoverages.add(coverages);
            //responseTest.setListCoverages(listCoverages);
            if(!response1.getSimulationQuoteId().isEmpty()) { 
	            String nombreCompleto = String.format("%s %s %s %s", 
	    				request.getInsuredData().getContractingFistName().orElse(""),
	    				request.getInsuredData().getContractingMiddleName().orElse(""),
	    				request.getInsuredData().getContractingLastName().orElse(""),
	    				"");
	            
	            Map<String, String> mapVarData = service.getVariableData(request.getVariableData());
	            
	            Map<String, String> mapVarDataCoverage = service.getVariableDataCoverages(request.getVariableDataCoverage());
	             
	            String xml = service.getXmlForPrint(request.getPolicyData().getBranchCode(), 
	             		response1.getSimulationQuoteId(), 
	             		request.getInsuredData().getRealAge().orElse("20"), 
	             		request.getInsuredData().getGender(), 
	             		mapVarData.getOrDefault("ANIOS_DURACION_POLIZA", "1"), 
	             		mapVarDataCoverage.getOrDefault("IMP_PREMIO_VIDA", "50000"), 
	             		mapVarDataCoverage.getOrDefault("IMP_PREMIO_VIDA_AD", "50000"), 
	             		mapVarDataCoverage.getOrDefault("FORMA_PAGO_AD", "4"), 
	             		nombreCompleto,
	             		"1", 
	             		response1.getListCoverages(),
	             		request.getCoverage(),
	             		mapVarData.getOrDefault("COD_MODALIDAD", "11204"),
	             		request.getPolicyData().getContractNumber());
	            //response1.setPdfBase64(this.generationPdfService.generatePdfQuotation(request, response1));
	            response1.setPdfBase64(this.generationPdfService.createPDFHPExstream(xml,request.getInsuredData().getEmail()));
	            responseM.setMessage(VidaULContants.HeadersResponse.MENSAJE_EXITOSO);
	            responseM.setShortCode(VidaULContants.HeadersResponse.CODIGO_CORTO_EXITO);
	            responseM.setStatus(VidaULContants.HeadersResponse.ESTATUS_EXITOSO);
	            responseM.setData(response1);
	            //responseM.setData(responseTest);
	            return  ResponseEntity.status(HttpStatus.OK).body(responseM);
            }
            else {
            	response1.setCode(400);
                responseM.setMessage(VidaULContants.HeadersResponse.MENSAJE_FALLIDO);
                responseM.setShortCode(VidaULContants.HeadersResponse.CODIGO_CORTO_FALLIDO);
                responseM.setStatus(VidaULContants.HeadersResponse.ESTATUS_FALLIDO);
                responseM.setData(null);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseM);
            }
            
        }else{
            response1.setCode(400);
            responseM.setMessage(VidaULContants.HeadersResponse.MENSAJE_FALLIDO);
            responseM.setShortCode(VidaULContants.HeadersResponse.CODIGO_CORTO_FALLIDO);
            responseM.setStatus(VidaULContants.HeadersResponse.ESTATUS_FALLIDO);
            responseM.setData(null);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseM);
        }
        //response1 = service.getCotizacion(request);
        
    }


	@ApiOperation(value = "Impresion", response = ImpresionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Correct answer Returns the file in a bytearray"),
			@ApiResponse(code = 400, message = "There is an error in any of the fields"),
			@ApiResponse(code = 401, message = "Not authorized to see this resource"),
			@ApiResponse(code = 403, message = "It is forbidden to access the resource"),
			@ApiResponse(code = 404, message = "The resource is not found"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@PostMapping(value = "/print", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<MifelResponse<EmisionResponse>> getDocument(@Valid @RequestBody ImpresionEmisionRequest request) {
		logger.debug("Comenzando a descargar pdf emision: {}", request);
		 MifelResponse<EmisionResponse> response = new MifelResponse<>();
		response = this.clientImpresionService.getImpresionEmision(request.getNumPoliza(), 
				request.getNegocio(), request.getNumEndoso(), request.getNmi(), request.getEmailDestino(), Impresion.CODIGO_IMPRESION_CARATULA_EXTREM);
		return new ResponseEntity<>(response, response.getStatus() != VidaULContants.HeadersResponse.ESTATUS_FALLIDO ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "ImpresionHP", response = ImpresionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Correct answer Returns the file in a bytearray"),
			@ApiResponse(code = 400, message = "There is an error in any of the fields"),
			@ApiResponse(code = 401, message = "Not authorized to see this resource"),
			@ApiResponse(code = 403, message = "It is forbidden to access the resource"),
			@ApiResponse(code = 404, message = "The resource is not found"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@PostMapping(value = "/printHP")
	public ResponseEntity<byte[]> getDocumentHP(@Valid @RequestBody ImpresionHPRequest requestHP) {
		logger.debug("Printing: {}", requestHP);
		byte[] response = clientImpresionServiceHP.getPdfHpExstream(requestHP.getPolicy(), requestHP.getDocumentType());
		//ImpresionHPResponse response = new ImpresionHPResponse();
		//String sResponse = clientImpresionServiceHP.obtenXmlHpExstream(requestHP.getNumPoliza(),requestHP.getCodCia(),requestHP.getNumSpto());
//		response.setDocumentId(clientImpresionService.getImpresionId(request.getNumPoliza(), request.getNumSpto(),
//				request.getCodCia()));
//		response.getDocumentData().setDocumentContent(clientImpresionService.downloadFile(response.getDocumentId()));
		return ok()
				.contentType(MediaType.APPLICATION_PDF)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ requestHP.getPolicy() + "_" + requestHP.getDocumentType() +".pdf")
				.body(response);
	}
	
	/*@ApiOperation(value = "Get file", response = ImpresionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Correct answer Returns the file in a bytearray"),
			@ApiResponse(code = 400, message = "There is an error in any of the fields"),
			@ApiResponse(code = 401, message = "Not authorized to see this resource"),
			@ApiResponse(code = 403, message = "It is forbidden to access the resource"),
			@ApiResponse(code = 404, message = "The resource is not found"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@GetMapping(value = "/printdocu", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ImpresionResponse getDocument(
			@ApiParam(value = "Policy number", type = "String", example = "1082100000099", required = true) @RequestParam(name = "numPoliza") String numPoliza,
			@ApiParam(value = "numSpto", type = "String", example = "0", required = true) @RequestParam(name = "numSpto") String numSpto,
			@ApiParam(value = "Company code", type = "String", example = "1", required = true) @RequestParam(name = "codCia") String codCia) {
		logger.debug("Printing: numPoliza: {}, numSpto: {}, codCia: {}", numPoliza, numSpto, codCia);
		ImpresionResponse response = new ImpresionResponse();
		response.setDocumentId(clientImpresionService.getImpresionId(numPoliza, numSpto, codCia));
		response.getDocumentData().setDocumentContent(clientImpresionService.downloadFile(response.getDocumentId()));
		return response;
	}*/
	

	@ApiOperation( value = "Realizar un pago en dolares o pesos mexicanos")
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Respuesta correcta regresa el numero de autorizacion"),
			@ApiResponse( code = 204, message = "El pago no puede realizarse"),
			@ApiResponse( code = 401, message = "No está autorizado para ver este recurso"),
			@ApiResponse( code = 403, message = "Está prohibido acceder al recurso"),
			@ApiResponse( code = 404, message = "No se encuentra el recurso")
	})
	@PostMapping("/payment")
	public ResponseEntity<PaymentCobranzaResponse> payment(@Validated(value= {PaymentMethod.class}) @RequestBody PaymentRequest request) {
		try {
			

			PaymentCobranzaResponse response = IPaymentService.payment(request);
			if (response != null) {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {

			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@ApiOperation( value = "Obtener todos los catalogos de bancos", response = CatalogsBanksResponse.class)
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Respuesta correcta regresa toda la lista de usuarios"),
			@ApiResponse( code = 204, message = "La consulta no tiene resultados"),
			@ApiResponse( code = 401, message = "No está autorizado para ver este recurso"),
			@ApiResponse( code = 403, message = "Está prohibido acceder al recurso"),
			@ApiResponse( code = 404, message = "No se encuentra el recurso")
	})
	@GetMapping("/catalogs/banks/{codRamo}/{version}")
	public ResponseEntity<List<CatalogsBanksResponse>> getBancos(@PathVariable("codRamo") Integer codRamo, @PathVariable("version") Integer version, @RequestHeader("Accept-Language") String language) throws SQLException
	{
		List<CatalogsBanksResponse> response = serviceBancos.obtenerBancos(codRamo, version);
		try
		{
			if(response!=null)
			{
				return new ResponseEntity<List<CatalogsBanksResponse>>(response, HttpStatus.OK);
			}
			else {
				return new ResponseEntity<List<CatalogsBanksResponse>>(HttpStatus.NOT_FOUND);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<List<CatalogsBanksResponse>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation( value = "Realiza el envio de correo electrónico de documentos para emision o cotización")
    @PostMapping("/sendmail")
    public @ResponseBody ResponseEntity<MifelResponse<Object>> sendEMail(@RequestBody EmailRequest emailRequest) {
        System.out.println("Comenzando a enviar correo a cliente: " + emailRequest.toString());
       MifelResponse<Object> response = this.emailService.sendEmail(emailRequest);
        return new ResponseEntity<>(response, response.getStatus() != VidaULContants.HeadersResponse.ESTATUS_FALLIDO ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
