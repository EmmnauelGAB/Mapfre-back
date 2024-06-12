package com.mapfre.mifel.vida.entity.banco;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NamedNativeQuery;

@Entity
@Table(name = "A5020900")
@NamedNativeQuery(
	name = "getBancos",
	query = "{ ? = call em_k_gen_ws.getlov(?, ?, ?, ?, ?, ?) }",
	callable = true
)
public class Bancos {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long  codeBanco;
	
	@Column(name = "COD_ENTIDAD")
	private String codEntidad;
	
	@Column(name = "NOM_ENTIDAD")
	private String nomEntidad;
	
	public Bancos()
	{
		
	}

	public Bancos(Long codeBanco, String codEntidad, String nomEntidad) {
		super();
		this.codeBanco = codeBanco;
		this.codEntidad = codEntidad;
		this.nomEntidad = nomEntidad;
	}

	public Long getCodeBanco() {
		return codeBanco;
	}

	public void setCodeBanco(Long codeBanco) {
		this.codeBanco = codeBanco;
	}

	public String getCodEntidad() {
		return codEntidad;
	}

	public void setCodEntidad(String codEntidad) {
		this.codEntidad = codEntidad;
	}

	public String getNomEntidad() {
		return nomEntidad;
	}

	public void setNomEntidad(String nomEntidad) {
		this.nomEntidad = nomEntidad;
	}

	@Override
	public String toString() {
		return "Bancos [codeBanco=" + codeBanco + ", codEntidad=" + codEntidad + ", nomEntidad=" + nomEntidad + "]";
	}
	
	

}
