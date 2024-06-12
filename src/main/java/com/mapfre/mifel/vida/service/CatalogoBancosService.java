package com.mapfre.mifel.vida.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.mapfre.mifel.vida.entity.banco.Bancos;
import com.mapfre.mifel.vida.model.CatalogsBanksResponse;
import com.mapfre.mifel.vida.repository.bancos.BancosRepository;
import com.mapfre.mifel.vida.repository.bancos.BancosRepositoryCustomImpl;


@Service
public class CatalogoBancosService {
	
	private static final Logger LOGGER = LogManager.getLogger(CatalogoBancosService.class);
	
	@Autowired
	BancosRepositoryCustomImpl repoBancos;
		
	//Metodo que obtiene el catalogo de bancos.
	public List<CatalogsBanksResponse> obtenerBancos(Integer codRamo, Integer version) {
		// TODO Auto-generated method stub
		List<CatalogsBanksResponse> catalogsBancos = new ArrayList<CatalogsBanksResponse>();
		CatalogsBanksResponse catalogo = null;

		List<Bancos> bancosList = repoBancos.getBancos(1, codRamo, "", "A5020900", version,"");
		bancosList.forEach(e -> LOGGER.debug("Bancos(cod: %s, nom:%s)\n", e.getCodEntidad(), e.getNomEntidad()));
		
		if (bancosList != null) {
			for(Bancos banco : bancosList)
			{
				LOGGER.debug("El nombre del Banco  es " + banco.getNomEntidad() );
				catalogo = new CatalogsBanksResponse();
				catalogo.setParameterId(banco.getCodEntidad());
				catalogo.setParameterName(banco.getNomEntidad());
				catalogo.setParameterDesc(banco.getNomEntidad());
				
				catalogsBancos.add(catalogo);
			}

		}
		
		return catalogsBancos;

	}

}
