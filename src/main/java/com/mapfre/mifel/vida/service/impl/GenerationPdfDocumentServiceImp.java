package com.mapfre.mifel.vida.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64.OutputStream;
import com.mapfre.mifel.vida.model.Contributions;
import com.mapfre.mifel.vida.model.Coverages;
import com.mapfre.mifel.vida.model.CoveragesQuot;
import com.mapfre.mifel.vida.model.DataClient;
import com.mapfre.mifel.vida.model.DataProduct;
import com.mapfre.mifel.vida.model.DetailCoverages;
import com.mapfre.mifel.vida.model.DistributionFunds;
import com.mapfre.mifel.vida.model.HeadersInfoQuotation;
import com.mapfre.mifel.vida.model.InformationPdfQuotation;
import com.mapfre.mifel.vida.model.request.EmailRequest;
import com.mapfre.mifel.vida.model.request.SimulationRequest;
import com.mapfre.mifel.vida.model.response.VidaULCotizacionResponse;
import com.mapfre.mifel.vida.model.ul.FieldRisk;
import com.mapfre.mifel.vida.model.ul.VariableData;
import com.mapfre.mifel.vida.service.EmailService;
import com.mapfre.mifel.vida.service.GenerationPdfDocumentService;
import com.mapfre.mifel.vida.utils.VidaULContants;

@Service
public class GenerationPdfDocumentServiceImp implements GenerationPdfDocumentService{
	
	private static final Logger LOGGER = LogManager.getLogger(GenerationPdfDocumentServiceImp.class);
	
	private final BaseColor COLOR_RED_MAPFRE = BaseColor.RED;
	
	@Value("${URLWSHpExtream}")
	private String sURLWSHpExtream;
	
	@Value("${userHP}")
	private String sUserHP;
    @Value("${passHP}")
	private String sPassHP;
    
    @Value("${PubFileCotizacion}")
	private String sPubFileCotizacion;
	
	@Autowired
	private EmailService emailService;

	@Override
	public String generatePdfQuotation(SimulationRequest request, VidaULCotizacionResponse responseQuotation) {
		LOGGER.info("Generando PDF...");
		InformationPdfQuotation information = new Gson().fromJson(VidaULContants.DataDummy.INFO_PDF_QUOT, InformationPdfQuotation.class);
		information.getDatosSolicitante().setEmail(request.getInsuredData().getEmail());
		String nombreCompleto = String.format("%s %s %s %s", 
				request.getInsuredData().getContractingFistName().orElse(""),
				request.getInsuredData().getContractingMiddleName().orElse(""),
				request.getInsuredData().getContractingLastName().orElse(""),
				"");
		information.getDatosSolicitante().setNombre(nombreCompleto);
		information.getDatosSolicitante().setEdad(Integer.parseInt(request.getInsuredData().getRealAge().orElse("0")));
		information.getDatosSolicitante().setSexo(request.getInsuredData().getGender() == "1" ? "MASCULINO" : "FEMENINO");
		information.getDatosCotizacion().setFechaCotizacion(request.getPolicyData().getPolicyEffectiveDate());
		information.getDatosCotizacion().setNumCotizacion(responseQuotation.getSimulationQuoteId());
		
		String modalidad = this.getModalidad(request.getVariableData());
		LOGGER.info("Modalidad: " + modalidad);
		String modalidadParaPDF = this.getModalidadPorCodigo(modalidad);
		LOGGER.info("ModalidadPDF: " + modalidadParaPDF);
		information.getInformacionDelProducto().setModalidad(modalidadParaPDF);
		information.getInformacionDelProducto().setMoneda("PESOS");
		
		String plazoSeguro = this.getPlazoSeguro(request.getVariableData());
		
		information.getInformacionDelProducto().setPlazoSeguro(plazoSeguro);
		information.getInformacionDelProducto().setPeridiocidadPrimaAdicional(" ");
		
		
		DetailCoverages coberturas = new DetailCoverages();
		List<CoveragesQuot> coberturasResponse = new ArrayList<>();
		Double primaTotal = 0.0;
		if(responseQuotation.getListCoverages() != null) {
			LOGGER.info("COBERTURAS size: " + responseQuotation.getListCoverages().size());
			coberturasResponse = this.getCoverages(responseQuotation.getListCoverages());
			primaTotal = coberturasResponse.stream().mapToDouble(CoveragesQuot::getPrimaAnual).sum();
			coberturas.setCoberturas(coberturasResponse);
			coberturas.setPrimaTotal(primaTotal);
		}else {
			coberturas.setCoberturas(coberturasResponse);
			coberturas.setPrimaTotal(primaTotal);
		}
		
		
		coberturas.setCoberturas(coberturasResponse);
		
		information.getDetalleCoberturas().setCoberturas(coberturasResponse);
		information.getDetalleCoberturas().setPrimaTotal(coberturas.getPrimaTotal());
		
		information.getAportaciones().getAportacionesAdicionales().setPrimaAhorro(0.0);
		information.getAportaciones().getAportacionesAdicionales().setPrimaRiesgo(0.0);
		
		information.getAportaciones().getAportacionesIniciales().setPrimaAhorro(0.0);
		information.getAportaciones().getAportacionesIniciales().setPrimaRiesgo(0.0);
		
		information.getAportaciones().setPrimaTotalAdicional(0.0);
		information.getAportaciones().setPrimaTotalInicial(0.0);
		
		List<DistributionFunds> distribucionFondos = new LinkedList<>();
		
		information.setDistribucionFondos(distribucionFondos);
		
		// Crear un nuevo documento
        Document document = new Document();
        LOGGER.info("Informacion de objeto para pdf: {}", information);
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
            
            document.open();

           /*AQUI EMPIEZA LA IMPLEMENTACION DEL PDF**/
            
            document.add(this.createSectionQuotationHeaderSquareRed());
            
            document.add(this.createSectionHeaderDataQuotation(information.getDatosCotizacion()));
            
            document.add(new Paragraph("DATOS DEL SOLICITANTE", this.getColorFont("red", false, 10)));
            document.add(this.generateSeparation(5f));
            document.add(this.createSectionDataClient(information.getDatosSolicitante()));
            
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("INFORMACIÓN DEL PRODUCTO", this.getColorFont("red", false, 10)));
            document.add(this.generateSeparation(5f));
            document.add(this.createSectionProductInformation(information.getInformacionDelProducto()));
            
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("DETALLE DE COBERTURAS", this.getColorFont("red", false, 10)));
            document.add(this.generateSeparation(5f));
            document.add(this.createSectionCoverages(information.getDetalleCoberturas()));
            
            document.add(new Paragraph("APORTACIONES", this.getColorFont("red", false, 10)));
            document.add(this.generateSeparation(5f));
            document.add(this.createSectionContributions(information.getAportaciones()));
            
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("DISTRIBUCIÓN DE FONDOS", this.getColorFont("red", false, 10)));
            document.add(this.generateSeparation(5f));
            document.add(this.createSectionDistributionFundsPart1());
            document.add(this.createSectionDistributionFundsPart2(information.getDistribucionFondos()));
            
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("AVISO DE PRIVACIDAD", this.getColorFont("red", false, 10)));
            document.add(this.generateSeparation(5f));
            document.add(this.createSectionPrivacyPolicy());
            
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph(String.format("MÉXICO A %s.", this.getDateFromText(information.getDatosCotizacion().getFechaCotizacion()).toUpperCase()), this.getColorFont("default", false, 10)));
           
            document.add(this.generateSeparation(5f));
            document.add(this.createSectionInfoQR(information));

            document.add(this.generateSeparation(5f));
            document.add(this.createSectionAddress());
            
            document.add(this.generateSeparation(5f));
            document.add(this.createFooter(writer));
            
          
           /*AQUI FINALIZA LA IMPLEMENTACION DEL PDF**/
 
            // Cerrar el documento
            document.close();

            // Obtener los bytes del ByteArrayOutputStream
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
           // String pdfBase64 = Base64.getEncoder().encodeToString(pdfBytes);
            String pdfBase64 = Base64.encodeBase64String(pdfBytes);
            //Una vez generado el pdf sin problemas enviamos la informacion por correo electrónico al cliente
            List<String> documentos = new LinkedList<>();
            documentos.add(pdfBase64);
            this.emailService.sendEmail(
            		new EmailRequest(
            				information.getDatosSolicitante().getEmail(),
            				VidaULContants.InformacionEmailEnvio.DOCUMENTO_COTIZACION, documentos));
            // Convertir los bytes a Base64
            return pdfBase64;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("Hubo un problema al generar el pdf y al enviarlo al cliente: " + e.getMessage());
            return null;
        } 
	}
	
	public String getPlazoSeguro(List<VariableData> variableData) {
		String plazoSeguro = "";
		
		// OBTENEMOS EL PLAZO DE SEGURO PARA PDF
		for(VariableData data : variableData) {
			LOGGER.info("PLAZO SEGURO RISK DATA: " + data.getRiskNumber() + ", " + data.getLevelType());
			if(data.getLevelType().equalsIgnoreCase("1") && data.getRiskNumber().equalsIgnoreCase("0")) {
				for(FieldRisk field : data.getFields()) {
					LOGGER.info("FIELDCODE: " + field.getFieldCode());
					if(field.getFieldCode().equalsIgnoreCase("ANIOS_DURACION_POLIZA")) {
						plazoSeguro = String.format("%s AÑOS", field.getFieldValue());
						return plazoSeguro;
					}
				}
			}
		}
		return plazoSeguro;
	}
	
	
	public String getModalidadPorCodigo(String modalidad) {
		String modalidadPDF = "";
		
		switch (modalidad) {
		case "11201":
			modalidadPDF = "UL Inversión";
			break;
		case "11202":
			modalidadPDF = "UL Jubilación";
			break;
		case "11203":
			modalidadPDF = "UL PPR";
			break;
		case "11204":
			modalidadPDF = "Contigo en Tu Inversión";
			break;
		case "11205":
			modalidadPDF = "Contigo En tu Jubilación";
			break;
		case "11206":
			modalidadPDF = "Contigo en tu Retiro";
			break;
		
		}
		return modalidadPDF;
	}
	
	public String getModalidad(List<VariableData> variableData) {
		String modalidad = "";
		
		// OBTENEMOS LA MODALIDAD MEDIANTE EL NIVEL DOS QUE CONTIENE ESA MODALIDAD
		for(VariableData data : variableData) {
			LOGGER.info("DataLevelType: " + data.getLevelType());
			if(data.getLevelType().equalsIgnoreCase("2")) {
				for(FieldRisk field : data.getFields()) {
					LOGGER.info("FIELDCODE: " + field.getFieldCode());
					if(field.getFieldCode().equalsIgnoreCase("COD_MODALIDAD")) {
						modalidad = field.getFieldValue();
						return modalidad;
					}
				}
			}
		}
		return null;
	}
	
	
	public List<CoveragesQuot> getCoverages(List<Coverages> coberturasResponse){
		List<CoveragesQuot> coberturasReturn = new ArrayList<>();
		Double primaTotal = 0.0;
		if(!coberturasResponse.isEmpty()) {
			for(Coverages cob : coberturasResponse) {
				CoveragesQuot coverage = new CoveragesQuot();
				coverage.setCoberturaDescripcion(cob.getCoverageDesc());
				coverage.setPrimaAnual(Double.parseDouble(cob.getPrimeAmn()));
				coverage.setSumaAsegurada(Double.parseDouble(cob.getSumInsuredAmn()));
				coberturasReturn.add(coverage);
			}
		}
		
		
		return coberturasReturn;
	}
	
	public PdfPTable createFooter(PdfWriter writer) throws DocumentException {
		PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        float[] columnWidths = {10f, 90f}; 
        table.setWidths(columnWidths);

        table.addCell(this.generateNewCell(String.format("PÁGINA %s", writer.getPageNumber()), this.getColorFont("default", false, 9), Element.ALIGN_LEFT));
        table.addCell(this.generateNewCell("ESTE DOCUMENTO ES DE CARACTER INFORMATIVO Y CARECE DE VALIDEZ CONTRACTUAL", this.getColorFont("default", false, 9), Element.ALIGN_RIGHT));
        
        return table;
	}
	
	public PdfPTable createSectionAddress() throws DocumentException {
		PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        float[] columnWidths = {75f}; 
        table.setWidths(columnWidths);
        
        String address = "Av. Revolución #507, Col. San Pedro de los Pinos, Del. Benito Juárez,  Ciudad de México, C.P. 03800 Tel.: 52-30-70-00 R.F.C. MTE440316E54";
        
        table.addCell(this.generateNewCell(address, this.getColorFont("red", false, 10), Element.ALIGN_LEFT));
        
        return table;
	}
	
	public String getDateFromText(String date) throws ParseException {
		Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(date);
		SimpleDateFormat formato = new SimpleDateFormat("EEEE dd 'de' MMMM 'del' yyyy", new Locale("es", "ES"));
		return formato.format(fecha);
	}
	
	public PdfPTable createSectionPrivacyPolicy() throws DocumentException {
		PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        float[] columnWidths = {100f}; 
        table.setWidths(columnWidths);
        
        String privacyPolicyText = "MAPFRE MÉXICO S.A. HACE SU CONOCIMIENTO QUE LOS DATOS PERSONALES RECABADOS, SE TRATARÁN PARA TODOS LOS FINES VINCULADS CON LA RELACIÓN JURÍDICA CELEBRADA, CONSULTE EL AVISO INTEGRO EN WWW.MAPFRE.COM.MX";
        
        table.addCell(this.generateNewCell(privacyPolicyText, this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
        
        return table;
	}
	
	public PdfPTable createSectionInfoQR(InformationPdfQuotation information) throws Exception {
		PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        float[] columnWidths = {90f, 10f}; 
        table.setWidths(columnWidths);
        
        table.addCell(this.generateNewCell("ESTUDIO VÁLIDO POR 30 DÍAS A PARTIR DE LA FECHA EN QUE SE REALIZÓ ESTA COTIZACIÓN.", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
        
        String dataQR = String.format("%s|%s|%s|%s", information.getDatosCotizacion().getNumCotizacion(),
        										  information.getDatosCotizacion().getFechaCotizacion(), 
        										  information.getDatosSolicitante().getNombre(), 
        										  information.getDatosSolicitante().getEdad());
        
        Image qrCodeImage = this.generateDynamicQRCode(dataQR, 100);
        qrCodeImage.setAbsolutePosition(0f, 00f);
        qrCodeImage.setPaddingTop(-40f);
        
        PdfPCell cellQR = new PdfPCell();
        cellQR.addElement(qrCodeImage);
        cellQR.setBorder(0);
        
        table.addCell(cellQR);
        
        return table;
	}
	
	private Image generateDynamicQRCode(String qrCodeData, int size) throws Exception {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, size, size);

        // Crear una imagen a partir de la matriz de bits
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        // Crear una instancia de Image con la imagen generada
        return Image.getInstance(outputStream.toByteArray());
    }

	public PdfPTable createSectionDistributionFundsPart2(List<DistributionFunds> data) throws DocumentException {
		PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        float[] columnWidths = {25f, 25f, 25f, 25f}; 
        table.setWidths(columnWidths);
        
        table.addCell(this.generateNewCell("PERFIL", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
        table.addCell(this.generateNewCell("TIPO DE INVERSIÓN", this.getColorFont("default", false, 10), Element.ALIGN_CENTER));
        table.addCell(this.generateNewCell("PORCENTAJE", this.getColorFont("default", false, 10), Element.ALIGN_CENTER));
        table.addCell(this.generateNewCell("MONTO INICIAL", this.getColorFont("default", false, 10), Element.ALIGN_CENTER));
        
        for(DistributionFunds found : data) {
        	table.addCell(this.generateNewCell(found.getPerfil(), this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
            table.addCell(this.generateNewCell(found.getTipoInversion(), this.getColorFont("default", false, 10), Element.ALIGN_CENTER));
            table.addCell(this.generateNewCell(found.getPorcentaje() + " %", this.getColorFont("default", false, 10), Element.ALIGN_CENTER));
            table.addCell(this.generateNewCell(this.formatMoney(found.getMontoInicial()), this.getColorFont("default", false, 10), Element.ALIGN_CENTER));
        } 
        
        return table;
       
	}
	
	public PdfPTable createSectionDistributionFundsPart1() throws DocumentException {
		PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        float[] columnWidths = {50f, 50f}; 
        table.setWidths(columnWidths);
        
        table.addCell(this.generateNewCell("", this.getColorFont("default", false, 10), Element.ALIGN_CENTER));
        table.addCell(this.generateNewCell("DISTRIBUCIÓN", this.getColorFont("default", false, 10), Element.ALIGN_CENTER));
        
        return table;
       
	}
	
	public PdfPTable createSectionContributions(Contributions data) throws DocumentException {
		 PdfPTable table = new PdfPTable(3);
	        table.setWidthPercentage(100);
	        table.setHorizontalAlignment(Element.ALIGN_LEFT);
	        float[] columnWidths = {45f, 10f, 45f}; 
	        table.setWidths(columnWidths);
	        
	        table.addCell(this.generateNewCell("APORTACION INICIAL:", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
	        table.addCell(this.generateNewCell("", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
	        table.addCell(this.generateNewCell("APORTACIONES ADICIONALES:", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
	       
	        PdfPTable tableLeftInitialContributions = new PdfPTable(2);
	        tableLeftInitialContributions.setWidthPercentage(100);
	        float[] columnWidthsinitial = {55f, 45f}; 
	        tableLeftInitialContributions.setWidths(columnWidthsinitial);
	       
	        tableLeftInitialContributions.addCell(this.generateNewCell("PRIMA DE AHORRO:", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
	        tableLeftInitialContributions.addCell(this.generateNewCell(this.formatMoney(data.getAportacionesIniciales().getPrimaAhorro()), this.getColorFont("default", false, 10), Element.ALIGN_RIGHT));
	        
	        tableLeftInitialContributions.addCell(this.generateNewCell("PRIMA DE RIESGO:", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
	        tableLeftInitialContributions.addCell(this.generateNewCell(this.formatMoney(data.getAportacionesIniciales().getPrimaRiesgo()), this.getColorFont("default", false, 10), Element.ALIGN_RIGHT));
	        
        	Double primaTotalInicial = data.getAportacionesIniciales().getPrimaRiesgo() + data.getAportacionesIniciales().getPrimaAhorro();
        	
        	tableLeftInitialContributions.addCell(this.generateNewCell("PRIMA TOTAL:", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
        	tableLeftInitialContributions.addCell(this.generateNewCell(this.formatMoney(primaTotalInicial), this.getColorFont("default", false, 10), Element.ALIGN_RIGHT));
	        
        	 PdfPCell cellLeftInitial = new PdfPCell();
        	 cellLeftInitial.setBorder(0); 
        	 
        	 cellLeftInitial.addElement(tableLeftInitialContributions);
        	 
        	 
        	 
        	 PdfPTable tableRightInitialContributions = new PdfPTable(2);
        	 tableRightInitialContributions.setWidthPercentage(100);
        	 float[] columnWidthsinitialRight = {55f, 45f}; 
        	 tableRightInitialContributions.setWidths(columnWidthsinitialRight);
 	       
        	 tableRightInitialContributions.addCell(this.generateNewCell("PRIMA DE AHORRO:", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
        	 tableRightInitialContributions.addCell(this.generateNewCell(this.formatMoney(data.getAportacionesAdicionales().getPrimaAhorro()), this.getColorFont("default", false, 10), Element.ALIGN_RIGHT));
 	        
        	 tableRightInitialContributions.addCell(this.generateNewCell("PRIMA DE RIESGO:", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
        	 tableRightInitialContributions.addCell(this.generateNewCell(this.formatMoney(data.getAportacionesAdicionales().getPrimaRiesgo()), this.getColorFont("default", false, 10), Element.ALIGN_RIGHT));
 	        
         	Double primaTotalAdditional = data.getAportacionesAdicionales().getPrimaRiesgo() + data.getAportacionesAdicionales().getPrimaAhorro();
         	
         	tableRightInitialContributions.addCell(this.generateNewCell("PRIMA TOTAL:", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
         	tableRightInitialContributions.addCell(this.generateNewCell(this.formatMoney(primaTotalAdditional), this.getColorFont("default", false, 10), Element.ALIGN_RIGHT));
 	        
         	PdfPCell cellRightAdditional = new PdfPCell();
         	cellRightAdditional.setBorder(0); 
         	 
         	cellRightAdditional.addElement(tableRightInitialContributions);
        	 
	    	 table.addCell(cellLeftInitial);
	    	 table.addCell(this.generateNewCell("", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
	    	 table.addCell(cellRightAdditional);
             
             

	        return table;
	}
	
	public PdfPTable createSectionCoverages(DetailCoverages data) throws DocumentException {
		 PdfPTable table = new PdfPTable(3);
	        table.setWidthPercentage(100);
	        table.setHorizontalAlignment(Element.ALIGN_LEFT);
	        float[] columnWidths = {40f, 35f, 25f}; // 50% de ancho para cada columna
	        table.setWidths(columnWidths);
	        
	        table.addCell(this.generateNewCell("COBERTURAS", this.getColorFont("default", false, 10), Element.ALIGN_CENTER));
	        table.addCell(this.generateNewCell("SUMA ASEGURADA:", this.getColorFont("default", false, 10), Element.ALIGN_CENTER));
	        table.addCell(this.generateNewCell("PRIMA ANUAL", this.getColorFont("default", false, 10), Element.ALIGN_CENTER));
	        
	        Double totalPrimaAnual = new Double(0);
	        for(CoveragesQuot cov : data.getCoberturas()) {
	        	table.addCell(this.generateNewCell(cov.getCoberturaDescripcion().toUpperCase(), this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
	        	table.addCell(this.generateNewCell(this.formatMoney(cov.getSumaAsegurada()), this.getColorFont("default", false, 10), Element.ALIGN_CENTER));
	        	table.addCell(this.generateNewCell(this.formatMoney(cov.getPrimaAnual()), this.getColorFont("default", false, 10), Element.ALIGN_CENTER));
	        	totalPrimaAnual += cov.getPrimaAnual();
	        }
	        
	        table.addCell(this.generateNewCell("", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
	        table.addCell(this.generateNewCell("", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
        	table.addCell(this.generateNewCell(String.format("PRIMA TOTAL: %s", this.formatMoney(totalPrimaAnual)), this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
	            
	        return table;
	}
	
	 public String formatMoney(Double cantidad) {
	        NumberFormat formatoCantidad = NumberFormat.getNumberInstance(Locale.getDefault());
	        return formatoCantidad.format(cantidad);
	    }
	
	public Paragraph generateSeparation(float value) {
		Paragraph emptyLine = new Paragraph("");
		emptyLine.setSpacingAfter(value);
		emptyLine.setPaddingTop(value * 2);
		return emptyLine;
	}
	
	public PdfPTable createSectionProductInformation(DataProduct data) throws DocumentException {
		 PdfPTable table = new PdfPTable(2);
	        table.setWidthPercentage(100);
	        table.setHorizontalAlignment(Element.ALIGN_LEFT);
	        float[] columnWidths = {40f, 60f}; // 50% de ancho para cada columna
	        table.setWidths(columnWidths);
	        
	        table.addCell(this.generateNewCell("MODALIDAD:", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
	        table.addCell(this.generateNewCell(data.getModalidad().toUpperCase(), this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
	        
	        table.addCell(this.generateNewCell("MONEDA:", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
	        table.addCell(this.generateNewCell(data.getMoneda().toString(), this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
	       
	        table.addCell(this.generateNewCell("PLAZO DEL SEGURO:", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
	        table.addCell(this.generateNewCell(data.getPlazoSeguro().toUpperCase(), this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
	        
	        table.addCell(this.generateNewCell("PERIDIOCIDAD DE LA PRIMA ADICIONAL:", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
	        table.addCell(this.generateNewCell(data.getPeridiocidadPrimaAdicional().toUpperCase(), this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
	
	        return table;
	}
	
	public PdfPTable createSectionDataClient(DataClient data) throws DocumentException {
		 // Crear una tabla con una celda que ocupa el 45% de la página
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        float[] columnWidths = {25f, 75f}; // 50% de ancho para cada columna
        table.setWidths(columnWidths);
        
        table.addCell(this.generateNewCell("NOMBRE:", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
        table.addCell(this.generateNewCell(data.getNombre().toUpperCase(), this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
        
        table.addCell(this.generateNewCell("EDAD:", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
        table.addCell(this.generateNewCell(data.getEdad().toString(), this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
       
        table.addCell(this.generateNewCell("SEXO:", this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
        table.addCell(this.generateNewCell(data.getSexo().toUpperCase(), this.getColorFont("default", false, 10), Element.ALIGN_LEFT));
       
        return table;
	}
	
	public PdfPCell generateNewCell(String valueTextCell, Font font,int alignment) {
		Phrase phrase = new Phrase(valueTextCell, font);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorder(0);
        cell.setHorizontalAlignment(alignment);
        return cell;
	}
	
	public PdfPTable createSectionQuotationHeaderSquareRed() {
		 // Crear una tabla con una celda que ocupa el 45% de la página
        PdfPTable tableHeaderRed = new PdfPTable(1);
        tableHeaderRed.setWidthPercentage(45);
        tableHeaderRed.getDefaultCell().setBorder(0); // Desactivar bordes de la tabla
        tableHeaderRed.setHorizontalAlignment(Element.ALIGN_LEFT);
        
        // Crear una celda con fondo rojo y texto blanco
        PdfPCell cell = new PdfPCell(new Paragraph("CONTIGO EN TU INVERSIÓN", this.getColorFont("white", true, 10)));
        
        Font font = new Font();
        font.setColor(BaseColor.WHITE);
        
        cell.setBackgroundColor(this.COLOR_RED_MAPFRE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(0);
        cell.setPadding(10);

        // Agregar la celda a la tabla
        tableHeaderRed.addCell(cell);
        
        return tableHeaderRed;
	}
	
	public PdfPTable createSectionHeaderDataQuotation(HeadersInfoQuotation data) throws MalformedURLException, IOException, DocumentException {
		//Generamos la tabla y colocamos los estilos para cada seccion
		 PdfPTable tableQuotHeader = new PdfPTable(2);
		 Font fontSize = new Font();
		 fontSize.setSize(10);
         tableQuotHeader.setWidthPercentage(100);
         tableQuotHeader.getDefaultCell().setBorder(Rectangle.NO_BORDER);
         //tableQuotHeader.getDefaultCell().setBorderWidth(1);
         tableQuotHeader.setTableEvent(null);  
         
         float[] columnWidths = {50f, 50f}; // 50% de ancho para cada columna
         tableQuotHeader.setWidths(columnWidths);
         
         //Colimna don de irá la imagen de mapfre
         Image image = this.getImageMapfreToBase64();
         
         PdfPCell imageCell = new PdfPCell(image, true);
         imageCell.setBorder(0);
         imageCell.setBorderWidth(1);
         imageCell.setBorderColorBottom(this.COLOR_RED_MAPFRE);
         imageCell.setBorderColorLeft(this.COLOR_RED_MAPFRE);
         imageCell.setBorderColorTop(this.COLOR_RED_MAPFRE);
         imageCell.setBorderColorRight(BaseColor.WHITE);
         imageCell.setHorizontalAlignment(Element.ALIGN_LEFT); 
         imageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
         imageCell.setPadding(10);
         
         PdfPCell cellRightDataQuot = new PdfPCell();
         cellRightDataQuot.setBorder(0);
         cellRightDataQuot.setBorderWidth(1);
         cellRightDataQuot.setBorderColorBottom(this.COLOR_RED_MAPFRE);
         cellRightDataQuot.setBorderColorRight(this.COLOR_RED_MAPFRE);
         cellRightDataQuot.setBorderColorLeft(BaseColor.WHITE);
         cellRightDataQuot.setBorderColorTop(this.COLOR_RED_MAPFRE);
         
	
         //Esta será la seccion derecha de la informacion del numero de cotizacion y fecha
         PdfPTable dataRightQuotTable = new PdfPTable(2);
         dataRightQuotTable.setWidthPercentage(100);
         Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD); 
         boldFont.setSize(10);
         
         Phrase phraseNumCot = new Phrase("NÚM. COTIZACIÓN", boldFont);
         PdfPCell cellNumCot = new PdfPCell(phraseNumCot);
         cellNumCot.setBorder(0);
         dataRightQuotTable.addCell(cellNumCot);
         Phrase phraseNumCotValue = new Phrase(data.getNumCotizacion(), fontSize);
         PdfPCell cellNumCotValue = new PdfPCell(phraseNumCotValue); 
         cellNumCotValue.setBorder(0);
         dataRightQuotTable.addCell(cellNumCotValue);
         
        
         Phrase phraseDateCot = new Phrase("FECHA DE COTIZACIÓN", boldFont);
         PdfPCell cellDateCot = new PdfPCell(phraseDateCot);
         cellDateCot.setBorder(0);
         dataRightQuotTable.addCell(cellDateCot);
         Phrase phraseDateCotValue = new Phrase(data.getFechaCotizacion(), fontSize);
         PdfPCell cellDateCotValue = new PdfPCell(phraseDateCotValue);
         cellDateCotValue.setBorder(0);
         dataRightQuotTable.addCell(cellDateCotValue);
         
         cellRightDataQuot.addElement(dataRightQuotTable);
         
         
         tableQuotHeader.addCell(imageCell);
         tableQuotHeader.addCell(cellRightDataQuot);
          
         return tableQuotHeader;
	}
	
	public Font getColorFont(String color, boolean requiredBold, Integer sizePxFont) {
        Font font = new Font();
        if(requiredBold) {
        	font.setStyle(Font.BOLD);
        }
        if(sizePxFont != null) {
        	font.setSize(sizePxFont);
        }
        switch (color) {
		case "white":
			font.setColor(BaseColor.WHITE);
			break;

		case "red":
			font.setColor(BaseColor.RED);
			break;
		case "default":
			font.setColor(BaseColor.BLACK);
			break;
        }
        
        return font;
    }
	
	public Image getImageMapfreToBase64() throws BadElementException, MalformedURLException, IOException {
		String base64Image = "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";
		//byte[] imageBytes = Base64.getDecoder().decode(base64Image);
		byte[] imageBytes = Base64.decodeBase64(base64Image);
        Image image = Image.getInstance(imageBytes);
        image.scaleToFit(10 * 0.4f, Float.MAX_VALUE);
        return image;
	}
	

	@Override
	public String createPDFHPExstream(String xml, String email) {
		//byte[] byteArray = null;
		String sFileOutput = null;
		try {
			byte[] bytesXml = xml.getBytes(StandardCharsets.UTF_8);
	        xml = java.util.Base64.getEncoder().encodeToString(bytesXml);
	        
	        LOGGER.debug("xml base64: {}",xml);
			URL url = new URL(sURLWSHpExtream);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
	        connection.setRequestProperty("Accept", "text/xml");
	        connection.setDoOutput(true);
	
	        String soapRequest = "<soapenv:Envelope xmlns:eng=\"urn:hpexstream-services/Engine\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" + 
	        		"<soapenv:Header>\n" + 
	        		"<wsse:Security soapenv:mustUnderstand=\"0\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" + 
	        		"<wsse:UsernameToken wsu:Id=\"UsernameToken-1\">\n" + 
	        		"<wsse:Username>" + sUserHP + "</wsse:Username>\n" + 
	        		"<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">" + sPassHP+ "</wsse:Password>\n" + 
	        		"<wsse:Nonce EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">9XbhOoet06M5XXD83sgM7Q==</wsse:Nonce>\n" + 
	        		"<wsu:Created>2015-07-21T08:23:30.207Z</wsu:Created>\n" + 
	        		"</wsse:UsernameToken>\n" + 
	        		"</wsse:Security>\n" + 
	        		"</soapenv:Header>\n" + 
	        		"<soapenv:Body>\n" + 
	        		"<eng:Compose>\n" + 
	        		"<EWSComposeRequest>\n" + 
	        		"<driver>\n" + 
	        		"<!--Fichero de datos en Base64-->\n" + 
	        		"<driver>" + xml + "</driver>\n" + 
	        		"<fileName>INPUT</fileName>\n" + 
	        		"</driver>\n" + 
	        		"<engineOptions>\n" + 
	        		"<name>IMPORTDIRECTORY</name>\n" + 
	        		"<value>/var/opt/exstream/pubs</value>\n" + 
	        		"</engineOptions>\n" + 
	        		"<engineOptions>\n" + 
	        		"<!--Ruta donde se encuentra fichero de referencias-->\n" + 
	        		"<!--A su vez, el fichero contiene ruta a recursos-->\n" + 
	        		"<name>FILEMAP</name>\n" + 
	        		"<value>REFERENCIAS,/var/opt/exstream/pubs/EMISION_VIDA/REFERENCIAS.ini</value>\n" + 
	        		"</engineOptions>\n" + 
	        		"<!--Optional:-->\n" + 
	        		"<pubFile>" + sPubFileCotizacion + "</pubFile>\n" + 
	        		"</EWSComposeRequest>\n" + 
	        		"</eng:Compose>\n" + 
	        		"</soapenv:Body>\n" + 
	        		"</soapenv:Envelope>";
	
	        try (java.io.OutputStream os = connection.getOutputStream()) {
	            os.write(soapRequest.getBytes(StandardCharsets.UTF_8));
	        }
	        
	        LOGGER.debug("soapRequest:: {}", soapRequest);
	
	        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
	        	LOGGER.debug("Escribiendo archivo:: {}");
	
	            StringBuilder soapResult = new StringBuilder();
	            String line;
	            while ((line = br.readLine()) != null) {
	                soapResult.append(line);
	            }
	
	            int iCadenaIni = soapResult.indexOf("<fileOutput>") + 12;
	            int iCadenaFin = soapResult.indexOf("</fileOutput>");
	
	            sFileOutput = soapResult.substring(iCadenaIni, iCadenaFin);
	
	            //byteArray = java.util.Base64.getDecoder().decode(sFileOutput);
	            
	            List<String> documentos = new LinkedList<>();
	            documentos.add(sFileOutput);
	            this.emailService.sendEmail(
	            		new EmailRequest(
	            				email,
	            				VidaULContants.InformacionEmailEnvio.DOCUMENTO_COTIZACION,
	            				documentos));
	            
	        }
	        
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	LOGGER.debug("Error en stored poliza:: {}", ex.getMessage());
	    
		}
		return sFileOutput;
	}
 


}
