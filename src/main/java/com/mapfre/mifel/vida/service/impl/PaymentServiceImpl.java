package com.mapfre.mifel.vida.service.impl;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mapfre.mifel.vida.mapper.PaymentMapper;
import com.mapfre.mifel.vida.model.BanamexResult;
import com.mapfre.mifel.vida.model.PaymentXML;
import com.mapfre.mifel.vida.model.request.PaymentRequest;
import com.mapfre.mifel.vida.model.response.PaymentCobranzaResponse;
import com.mapfre.mifel.vida.model.response.PaymentResponse;
import com.mapfre.mifel.vida.service.IPaymentService;

@Service
public class PaymentServiceImpl implements IPaymentService {
	
	private static final Logger LOGGER = LogManager.getLogger(PaymentServiceImpl.class);
	

	private static final String MXP_DESC = "MXN";
	private static final String USD_DESC = "USD";
	private static final String AMEX_CARD = "AMEX";
	private static final String AMEX_CODE = "3";
	private static final String VMC_CARD = "V/MC";
	private static final String AMEX_6M = "10203";
	private static final String AMEX_12M = "30120";
	private static final String AMEX_DEFAULT = "01045";
	private static final String VMC_6M = "11693";
	private static final String VMC_12M = "16058";
	private static final String VMC_DEFAULT = "01041";
	private static final String USD_PLANMONTHS = "236009";

	@Value("${usuarioVida}")
	private String usuarioVida;
	@Value("${codigoBanamex}")
	private String codigoBanamex;
	@Value("${ActinvarBanamex}")
	private String actinvarBanamex;
	@Value("${IdPlanMSI}")
	private String idPlanMSI;
	@Value("${Debug}")
	private String debug;
	@Value("${UseSSL}")
	private String useSSL;
	@Value("${IgnoreSslErrors}")
	private String ignoreSslErrors;
	@Value("${GatewayHost}")
	private String gatewayHost;
	@Value("${Version}")
	private String version;
	@Value("${MerchantId}")
	private String merchantId;
	@Value("${Username.banamex}")
	private String username;

	@Value("${Password.banamex}")
	private String password;

	@Value("${URLBanco}")
	private String urlBanco;
	
	@Value("${URLCobranza}")
	private String urlCobranza;

	private SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
	
	//private final String URL_COBRANZA="https://negociosuat.mapfre.com.mx/wslistasvalortw_temporal/wsvidatw.asmx";

	
	@Autowired
	RestTemplate restTemplate;

	

	
	@Override
	public PaymentCobranzaResponse payment(PaymentRequest request) {
		PaymentCobranzaResponse response = new PaymentCobranzaResponse();

		String respuestaCobranza = null;

		PaymentXML paymentXML = PaymentMapper.paymentJsonXML(request);
		String respuestaXML = null;
		String responseXML = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		

		try {
			
			String requestXML = marshall(paymentXML);
			
			HttpEntity<String> entity = new HttpEntity<>("xml="+requestXML, headers);

			responseXML = restTemplate.exchange(urlCobranza, HttpMethod.POST, entity, String.class).getBody();
			LOGGER.debug("La respuesta XML{}", responseXML);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PaymentCobranzaResponse respuestaCobranzaOBJ = null;
		try {
			respuestaCobranzaOBJ = unmarshall(responseXML);
			LOGGER.debug("La respuesta JSON{}", respuestaCobranzaOBJ);
		} catch (JAXBException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return PaymentMapper.paymentXMLJson(respuestaCobranzaOBJ);
		/**
		 * 
		 * boolean pagoPesos = false; boolean monedaValida = false;
		 * 
		 * if (request.getCurrencyCode() != null) { if
		 * (request.getCurrencyCode().equals(MXP_DESC)) { pagoPesos = true; monedaValida
		 * = true; } else { if (request.getCurrencyCode().equals(USD_DESC)) {
		 * monedaValida = true; } }
		 * 
		 * }
		 * 
		 * if (monedaValida) {
		 * 
		 * if (pagoPesos) { response = paymentMXP(request); } else { response =
		 * paymentUSD(request); }
		 * 
		 * }
		 * 
		 * return response;
		 * 
		 **/
	}

	private PaymentResponse paymentMXP(PaymentRequest request) {
		PaymentResponse response = new PaymentResponse();
		Integer meses = 1;
		String claveMeses = "";
		String cardTypeDesc = "";
		if (request.getNumberOfPayments() != null) {
			meses = Integer.valueOf(request.getNumberOfPayments());

			// evaluar si el pago es por medio de AMEX

			if (request.getCardType().equals(AMEX_CODE)) {
				cardTypeDesc = AMEX_CARD;
				claveMeses = determinaClaveMeses(meses, true, true);

			} else {
				cardTypeDesc = VMC_CARD;
				claveMeses = determinaClaveMeses(meses, false, true);
			}

			// Evalua si en entityBank es Banamex
			boolean actBanamex = Boolean.getBoolean(actinvarBanamex);

			if (request.getEntityBank().equals(codigoBanamex) && actBanamex) {
				// Llama pagoBanamex

			} else {
				// La cobranza se hace con cpmoto
				String usuario = "01" + usuarioVida + "01";
				// Llama a cpmoto
			}

		}

		return response;
	}

	private PaymentResponse paymentUSD(PaymentRequest request) {
		PaymentResponse response = new PaymentResponse();

		return response;
	}

	private String determinaClaveMeses(Integer meses, boolean amex, boolean pesos) {
		String claveMeses = "";

		if (pesos) {
			switch (meses) {
			case 1:
				claveMeses = VMC_DEFAULT;
				if (amex) {
					claveMeses = AMEX_DEFAULT;
				}
				break;
			case 6:
				claveMeses = VMC_6M;
				if (amex) {
					claveMeses = AMEX_6M;
				}

				break;

			case 12:
				claveMeses = VMC_12M;
				if (amex) {
					claveMeses = AMEX_12M;
				}

				break;
			default:
				claveMeses = VMC_DEFAULT;
				if (amex) {
					claveMeses = AMEX_DEFAULT;
				}

				break;
			}

		} else {

			claveMeses = USD_PLANMONTHS;
		}

		return claveMeses;
	}

	/**
	 * Pago con el servicio de banamex en pesos
	 * 
	 * @param request
	 * @param mesesSinInt
	 * @return
	 */

	private BanamexResult paymentByBanamex(PaymentRequest request, Integer mesesSinInt) {
		BanamexResult resul = new BanamexResult();
		String transationId = format.format(new Date());
		HashMap<String, String> banamexMap = new HashMap<>();
		banamexMap.put("order.id", request.getReferencePolicyNumber());
		banamexMap.put("order.reference", request.getReferencePolicyNumber());
		banamexMap.put("transaction.id", transationId);
		banamexMap.put("apiOperation", "PAY");
		banamexMap.put("sourceOfFunds.type", "CARD");
		banamexMap.put("sourceOfFunds.provided.card.number", request.getCardNumber());
		banamexMap.put("sourceOfFunds.provided.card.expiry.month", request.getMonthDueCard());
		banamexMap.put("sourceOfFunds.provided.card.expiry.year", request.getYearDueCard());
		banamexMap.put("sourceOfFunds.provided.card.securityCode", request.getCardCvvCode());
		banamexMap.put("order.amount", request.getAmount().trim());
		banamexMap.put("order.currency", MXP_DESC);

		if (mesesSinInt > 1) {
			banamexMap.put("paymentPlan.planId", idPlanMSI);
			banamexMap.put("paymentPlan.numberOfPayments", String.valueOf(mesesSinInt));

		}

		return resul;
	}

	public String marshall(PaymentXML xml) throws JAXBException, IOException {
		StringWriter sw = new StringWriter();
		JAXBContext context = JAXBContext.newInstance(PaymentXML.class);
		Marshaller mar = context.createMarshaller();
		mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		mar.marshal(xml, sw);
		return sw.toString();
	}

	public PaymentCobranzaResponse unmarshall(String xml) throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(PaymentCobranzaResponse.class);
		return (PaymentCobranzaResponse) context.createUnmarshaller().unmarshal(new StringReader(xml));
	}

}
