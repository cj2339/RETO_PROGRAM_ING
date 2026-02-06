package clases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	private String cantico;
	private int totalPuntos;

	public Equipo(String cod_e, String nom_e, LocalDate fechaFundacion, String cantico) {
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

	public String getCantico() {
		return cantico;
	}

	public void setCantico(String cantico) {
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

	    if (this.cantico != null) {
	        File f = new File(this.cantico);
	        if (f.exists()) {
	            System.out.print("Cantico: ");
	            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
	                String linea;
	                while ((linea = br.readLine()) != null) {
	                    System.out.println(linea);
	                }
	            } catch (IOException e) {
	                System.out.println("Error al leer el cántico."); 
	            }
	        } else {
	            System.out.println("Cantico: Fichero no encontrado (" + this.cantico + ")");
	        }
	    } else {
	        System.out.println("Cantico: No asignado (null)");
	    }
	}

	@Override
	public int compareTo(Equipo otro) {
		return Integer.compare(otro.totalPuntos, this.totalPuntos);
	}

}
