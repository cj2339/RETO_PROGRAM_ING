package clases;

import java.io.FileWriter;
import java.io.Serializable;
import java.time.LocalDate;

public class Equipo implements Serializable, Comparable<Equipo> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cod_e;
	private String nom_e;
	private LocalDate fechaFundacion;
	private FileWriter cantico;
	private int totalPuntos;

	public Equipo(String cod_e, String nom_e, LocalDate fechaFundacion, FileWriter cantico) {
		this.cod_e = cod_e;
		this.nom_e = nom_e;
		this.fechaFundacion = fechaFundacion;
		this.cantico = cantico;
		this.totalPuntos = 0;
	}

	public Equipo() {
		this.cod_e = "";
		this.nom_e = "";
		this.fechaFundacion = LocalDate.now();
		this.cantico = null;
		this.totalPuntos = 0;
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

	public int getTotalPuntos() {
		return totalPuntos;
	}

	public void setTotalPuntos(int totalPuntos) {
		this.totalPuntos = totalPuntos;
	}

	public void mostrarInfo() {
		System.out.println("Codigo: " + this.cod_e);
		System.out.println("Nombre: " + this.nom_e);
		System.out.println("Fecha de fundacion: " + this.fechaFundacion);
		System.out.println("Cantico: " + this.cantico);
		System.out.println("Total Puntos: " + this.totalPuntos);
	}

	@Override
	public int compareTo(Equipo otro) {
		return Integer.compare(otro.totalPuntos, this.totalPuntos);
	}

}
