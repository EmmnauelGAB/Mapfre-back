package com.mapfre.mifel.vida.repository.bancos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.mapfre.mifel.vida.entity.banco.Bancos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javassist.expr.NewArray;

@Primary
public class BancosRepositoryCustomImpl implements BancosRepositoryCustom {

	@Autowired
	private EntityManager em;
	
	@Override
	public List<Bancos> getBancos(Integer companyCode, Integer branchCode, String codCampo, String nameTable,
			Integer codeVersion, String parametros) {
		// TODO Auto-generated method stub
		List<Object[]> queryResult = em.createNamedQuery("getBancos")
				.setParameter(1, companyCode)
				.setParameter(2, branchCode)
				.setParameter(3, codCampo)
				.setParameter(4, nameTable)
				.setParameter(5, codeVersion)
				.setParameter(6, parametros)
				.getResultList();

				List<Bancos> bancosList = new ArrayList<>();
				for (Object[] o : queryResult) {
					Bancos b = new Bancos();
					b.setCodEntidad((String)o[0]);
					b.setNomEntidad((String)o[1]);
					bancosList.add(b);
				}
				return bancosList;
	}

}
