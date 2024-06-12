package com.mapfre.mifel.vida.model;

public class DataClient {

	private String nombre;
	private Integer edad;
	private String sexo;
	private String email;
	
	public DataClient() {
		
	}
	
	
	
	public DataClient(String nombre, Integer edad, String sexo, String email) {
		super();
		this.nombre = nombre;
		this.edad = edad;
		this.sexo = sexo;
		this.email = email;
	}



	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getEdad() {
		return edad;
	}
	public void setEdad(Integer edad) {
		this.edad = edad;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	
}
