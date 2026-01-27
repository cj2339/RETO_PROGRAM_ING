package clases;

import java.io.FileWriter;
import java.time.LocalDate;

public class Equipo {
	private String cod_e;
	private String nom_e;
	private LocalDate fechaFundacion;
	private FileWriter cantico;
	
	
	public Equipo(String cod_e, String nom_e, LocalDate fechaFundacion, FileWriter cantico) {
		this.cod_e = cod_e;
		this.nom_e = nom_e;
		this.fechaFundacion = fechaFundacion;
		this.cantico = cantico;
	}
	
	public Equipo() {
		this.cod_e = "";
		this.nom_e = "";
		this.fechaFundacion = LocalDate.now();
		this.cantico = null;
	}

	public String getCod_e() {
		return cod_e;
	}

	public void setCod_e(String cod_e) {
		this.cod_e = cod_e;
	}

	public String getNom_e() {
		return nom_e;
	}

	public void setNom_e(String nom_e) {
		this.nom_e = nom_e;
	}

	public LocalDate getFechaFundacion() {
		return fechaFundacion;
	}

	public void setFechaFundacion(LocalDate fechaFundacion) {
		this.fechaFundacion = fechaFundacion;
	}

	public FileWriter getCantico() {
		return cantico;
	}

	public void setCantico(FileWriter cantico) {
		this.cantico = cantico;
	}
	

	
}

