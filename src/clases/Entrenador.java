package clases;

import java.time.LocalDate;
import java.util.HashMap;

public class Entrenador extends Staff{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String jugadaBase;
	private HashMap <String, Ejercicio> Ejercicio;
	
	public Entrenador(String cod_s, String nom_s, int edad, LocalDate fechaIncor, String pais, double sueldo, String cod_e, 
			String jugadaBase, HashMap<String, Ejercicio> ejercicio) {
		super(cod_s, nom_s, edad, fechaIncor, pais, sueldo, cod_e);
		this.jugadaBase = jugadaBase;
		this.Ejercicio = ejercicio;
	}

	public String getJugadaBase() {
		return jugadaBase;
	}

	public void setJugadaBase(String jugadaBase) {
		this.jugadaBase = jugadaBase;
	}

	public HashMap<String, Ejercicio> getEjercicio() {
		return Ejercicio;
	}

	public void setEjercicio(HashMap<String, Ejercicio> ejercicio) {
		Ejercicio = ejercicio;
	}

	@Override
	public void visualizar() {
		System.out.println("Codigo: " + getCod_s());
		System.out.println("Nombre: " + getNom_s());
		System.out.println("Edad: " + getEdad());
		System.out.println("Fecha de incorporacion: " + getFechaIncor());
		System.out.println("Pais: " + getPais());
		System.out.println("Sueldo: " + getSueldo());
		System.out.println("Codigo del equipo: " + getCod_e());
		System.out.println("Jugada base: " + getJugadaBase());
		System.out.println("Ejercicios: " + getEjercicio());		
	}	
}
