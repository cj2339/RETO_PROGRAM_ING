package clases;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Staff implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String cod_s;
	protected String nom_s;
	protected int edad;
	protected LocalDate fechaIncor;
	protected String pais;
	protected double sueldo;
	protected String cod_e;
	
	public Staff(String cod_s, String nom_s, int edad, LocalDate fechaIncor, String pais, double sueldo, String cod_e) {
		this.cod_s = cod_s;
		this.nom_s = nom_s;
		this.edad = edad;
		this.fechaIncor = fechaIncor;
		this.pais = pais;
		this.sueldo = sueldo;
		this.cod_e = cod_e;
	}
	
	public Staff() {
		this.cod_s = "";
		this.nom_s = "";
		this.edad = 0;
		this.fechaIncor = LocalDate.now();
		this.pais = "";
		this.sueldo = 0;
		this.cod_e = "";
	}
	
	public String getCod_e() {
		return cod_e;
	}

	public void setCod_e(String cod_e) {
		this.cod_e = cod_e;
	}

	public String getCod_s() {
		return cod_s;
	}

	public void setCod_s(String cod_s) {
		this.cod_s = cod_s;
	}

	public String getNom_s() {
		return nom_s;
	}

	public void setNom_s(String nom_s) {
		this.nom_s = nom_s;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public LocalDate getFechaIncor() {
		return fechaIncor;
	}

	public void setFechaIncor(LocalDate fechaIncor) {
		this.fechaIncor = fechaIncor;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public double getSueldo() {
		return sueldo;
	}

	public void setSueldo(double sueldo) {
		this.sueldo = sueldo;
	}

	public abstract void visualizar();
}
