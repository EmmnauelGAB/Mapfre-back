package com.mapfre.mifel.vida.repository.bancos;

import java.util.List;

import com.mapfre.mifel.vida.entity.banco.*;

import org.springframework.stereotype.Component;

public interface BancosRepositoryCustom {
	
	List<Bancos> getBancos(Integer companyCode, Integer branchCode, String codCampo, String nameTable,
			Integer codeVersion, String parametros);

}
