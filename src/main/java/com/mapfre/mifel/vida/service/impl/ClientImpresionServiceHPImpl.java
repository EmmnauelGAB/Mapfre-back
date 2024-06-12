package com.mapfre.mifel.vida.service.impl;

import static com.mapfre.mifel.vida.utils.ClienteImpresionConstants.AFTER_VALUE;
import static com.mapfre.mifel.vida.utils.ClienteImpresionConstants.BEFORE_VALUE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.sound.midi.Patch;
import javax.sql.DataSource;

import com.mapfre.mifel.vida.controller.VidaULController;
import com.mapfre.mifel.vida.exception.NoContentException;
import com.mapfre.mifel.vida.exception.WSClientException;
import com.mapfre.mifel.vida.model.Coverages;
import com.mapfre.mifel.vida.model.enumeration.DocumentType;
import com.mapfre.mifel.vida.model.print.ResultXmlHpExstream;
import com.mapfre.mifel.vida.service.ClientImpresionService;
import com.mapfre.mifel.vida.service.ClientImpresionServiceHP;
import com.mapfre.mifel.vida.utils.MifelVidaMensajesEnum;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import oracle.jdbc.OracleTypes;

@Service
public class ClientImpresionServiceHPImpl implements ClientImpresionServiceHP {
	
	private static final Logger logger = LogManager.getLogger(ClientImpresionServiceHPImpl.class);
	
    @Autowired
    DataSource dataSource;
    
    @Value("${URLWSHpExtream}")
	private String sURLWSHpExtream;
    @Value("${EmisionSector}")
	private String sEmisionSector;

    @Value("${userHP}")
	private String sUserHP;
    @Value("${passHP}")
	private String sPassHP;
    
    @Value("${PubFilePolVida}")
	private String sPubFilePolVida;
    @Value("${PubFilePolVidaSolicitud}")
	private String sPubFilePolVidaSolicitud;
    @Value("${PubFilePolVidaFatca}")
	private String sPubFilePolVidaFatca;
    @Value("${PubFilePolVidaCRS}")
	private String sPubFilePolVidaCRS;
    @Value("${PubFilePolVidaPPR}")
	private String sPubFilePolVidaPPR;
    @Value("${documents_path}")
    private String documentsPath;

       
    @Override
	public byte[] getPdfHpExstream(String policy, DocumentType documentType) {
    	if(documentType == DocumentType.FOLLETO_INFORMATIVO)
    		return readPdf();
    		
		ResultXmlHpExstream res = getXmlHpExstream(policy, documentType);
		if(res.getStatus() == 1)
			return createPDFHPExstream(res.getContent(), documentType);
		return new byte[0];
	}
    
	public ResultXmlHpExstream getXmlHpExstream(String policy, DocumentType documentType) {
		logger.debug("Poliza:: {}", policy);
		int company = 1;
		int numSpto = 0;
		String error = "";
		String sXml = null;
		
		try {
            Connection conn= dataSource.getConnection();
            String mcommand = "{ call TRON2000.EM_K_IMP_GRAL_MMX.p_imprime_poliza(?,?,?,?,?,?) }";

            CallableStatement call = conn.prepareCall(mcommand);
            call.setInt(1, company);
            call.setString(2, policy);
            call.setInt(3, numSpto);
            call.setString(4, documentType.toString());
            
            call.registerOutParameter(5, OracleTypes.CLOB);
            call.registerOutParameter(6, OracleTypes.VARCHAR);

            call.executeQuery();

            sXml = call.getString(5);
            error = call.getString(6);
           
            logger.debug("Sclob:: {}",sXml);
            logger.debug("Error:: {}",error);
            
            call.close();
            conn.close();

        } catch (Exception throwables) {        	
            throwables.printStackTrace();
            logger.debug("Error en stored poliza:: {}", throwables.getMessage());
        }
		
		if(error != null)
			return ResultXmlHpExstream.builder()
					.status(-1)
					.content(error)
					.build();
		else
			return ResultXmlHpExstream.builder()
					.status(1)
					.content(sXml)
					.build();
					
	}
	
	public byte[] createPDFHPExstream(String xmlHpExstream, DocumentType documentType) {
		logger.debug("creaPDFHPExstream:: {}", xmlHpExstream);

        byte[] byteArray = null;

        try {
            String sPubFile = "";
            
            byte[] byt = xmlHpExstream.getBytes(StandardCharsets.UTF_8);
            xmlHpExstream = Base64.getEncoder().encodeToString(byt);

            switch(documentType) {
	            case POLIZA_CARATULA:
	            	sPubFile = sPubFilePolVida;
	            	break;
	            case SOLICITUD:
	            	sPubFile = sPubFilePolVidaSolicitud;
	            	break;
	            case FORMATO_FATCA:
	            	sPubFile = sPubFilePolVidaFatca;
	            	break;
	            case FORMATO_CRS:
	            	sPubFile = sPubFilePolVidaCRS;
	            	break;
	            case CONTRATO_PPR:
	            	sPubFile = sPubFilePolVidaPPR;
	            	break;
            }
            
            String userHPExstream = sUserHP;
            String paswHPExstream = sPassHP;
            String emisionSector = sEmisionSector;
            String rutaURL = sURLWSHpExtream;

            URL url = new URL(rutaURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
            connection.setRequestProperty("Accept", "text/xml");
            connection.setDoOutput(true);

            String soapRequest = "<soapenv:Envelope xmlns:eng=\"urn:hpexstream-services/Engine\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "    <soapenv:Header>\n" +
                    "        <wsse:Security soapenv:mustUnderstand=\"0\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                    "            <wsse:UsernameToken wsu:Id=\"UsernameToken-1\">\n" +
                    "                <wsse:Username>" + userHPExstream + "</wsse:Username>\n" +
                    "                <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">" + paswHPExstream + "</wsse:Password>\n" +
                    "                <wsse:Nonce EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">9XbhOoet06M5XXD83sgM7Q==</wsse:Nonce>\n" +
                    "                <wsu:Created>2015-07-21T08:23:30.207Z</wsu:Created>\n" +
                    "            </wsse:UsernameToken>\n" +
                    "        </wsse:Security>\n" +
                    "    </soapenv:Header>\n" +
                    "    <soapenv:Body>\n" +
                    "        <eng:Compose>\n" +
                    "            <EWSComposeRequest>\n" +
                    "                <driver>\n" +
                    "                    <!-- Fichero de datos en Base64 -->\n" +
                    "                    <driver>" + xmlHpExstream + "</driver>\n" +
                    "                    <fileName>INPUT</fileName>\n" +
                    "                </driver>\n" +
                    "                <engineOptions>\n" +
                    "                    <name>IMPORTDIRECTORY</name>\n" +
                    "                    <value>/var/opt/exstream/pubs</value>\n" +
                    "                </engineOptions>\n" +
                    "                <engineOptions>\n" +
                    "                    <name>FILEMAP</name>\n" +
                    "                    <value>REFERENCIAS,/var/opt/exstream/pubs/"+ emisionSector + "/REFERENCIAS.ini</value>\n" +
                    "                </engineOptions>\n" +
                    "                <pubFile>" + sPubFile + "</pubFile>\n" +                        
                    "            </EWSComposeRequest>\n" +
                    "        </eng:Compose>\n" +
                    "    </soapenv:Body>\n" +
                    "</soapenv:Envelope>";

            try (OutputStream os = connection.getOutputStream()) {
                os.write(soapRequest.getBytes(StandardCharsets.UTF_8));
            }
            
            logger.debug("soapRequest:: {}", soapRequest);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            	 logger.debug("Escribiendo archivo:: {}");

                StringBuilder soapResult = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    soapResult.append(line);
                }

                int iCadenaIni = soapResult.indexOf("<fileOutput>") + 12;
                int iCadenaFin = soapResult.indexOf("</fileOutput>");

                String sFileOutput = soapResult.substring(iCadenaIni, iCadenaFin);

                byteArray = Base64.getDecoder().decode(sFileOutput);
            }
            return byteArray;
        } catch (Exception ex) {
        	ex.printStackTrace();
            logger.debug("Error en stored poliza:: {}", ex.getMessage());
        }
		return byteArray;
    }
	
	public byte[] readPdf() {
		Path path = Paths.get(documentsPath, "Folleto informativo.pdf");
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return new byte[0];
		}
	}

}
