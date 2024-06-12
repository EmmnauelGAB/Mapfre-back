package com.mapfre.mifel.vida.service;

import com.mapfre.mifel.vida.model.Coverages;
import com.mapfre.mifel.vida.model.enumeration.Periodicity;
import com.mapfre.mifel.vida.model.request.FundDistribution;
import com.mapfre.mifel.vida.model.request.IssueRequest;
import com.mapfre.mifel.vida.model.request.SimulationRequest;
import com.mapfre.mifel.vida.model.response.VidaULCotizacionResponse;
import com.mapfre.mifel.vida.model.response.VidaULEmisionResponse;
import com.mapfre.mifel.vida.model.ul.Coverage;
import com.mapfre.mifel.vida.model.ul.VariableData;
import com.mapfre.mifel.vida.model.ul.VariableDataCoverage;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

//@Service
public interface VidaULService {

    //public VidaULEmisionResponse generaEmisionMock(IssuanceRequest request);
    public VidaULEmisionResponse getEmision(IssueRequest request);

    public String validateEntry(IssueRequest request);

    /**
     * Valida Etiquetas o entradas necesarias para la cotizacion y emision
     */
    public String validaEtiquetas(SimulationRequest request);

    /**
     * Genera la cotizacion [Obtener Primas] en base a:
     *  1) Ingreso Mensual del cliente
     *  2) Suma asegurada de la coberturas a contratar
     */
    public VidaULCotizacionResponse getCotizacion(SimulationRequest request);
    
    public String getXmlForPrint(String ramo, String num_cotizacion, String edad, String sexo, String plazo,
			String prima_ini, String prima_adi, String periodo, String nomcliente,
			String moneda, List<Coverages> coverages, List<Coverage> coverageList, String modality, String contract);

    public Map<String,String> getVariableData(List<VariableData> variableData);
    
    public Map<String,String> getVariableDataCoverages(List<VariableDataCoverage> variableData);
    
    public double getRiskPrime(int company, int branch, int mod, int contract, int currency, String coverages, int duration, int age);
}
